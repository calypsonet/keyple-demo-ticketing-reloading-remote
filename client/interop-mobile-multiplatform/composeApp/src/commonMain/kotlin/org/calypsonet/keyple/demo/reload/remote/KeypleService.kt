/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.calypsonet.keyple.demo.reload.remote.network.KeypleServerConfig
import org.calypsonet.keyple.demo.reload.remote.network.SimpleHttpNetworkClient
import org.calypsonet.keyple.demo.reload.remote.network.buildHttpClient
import org.calypsonet.keyple.demo.reload.remote.nfc.write.PriorityCode
import org.calypsonet.keyple.demo.reload.remote.nfc.write.WriteContract
import org.eclipse.keyple.interop.jsonapi.client.api.KeypleResult
import org.eclipse.keyple.interop.jsonapi.client.api.KeypleTerminal
import org.eclipse.keyple.interop.jsonapi.client.api.ServerIOException
import org.eclipse.keyple.interop.jsonapi.client.api.Status
import org.eclipse.keyple.interop.jsonapi.client.spi.LocalReader

const val SELECT_APP_AND_PERSONALIZE_CARD = "SELECT_APP_AND_PERSONALIZE_CARD"
const val SELECT_APP_AND_ANALYZE_CONTRACTS = "SELECT_APP_AND_ANALYZE_CONTRACTS"
const val SELECT_APP_AND_LOAD_CONTRACT = "SELECT_APP_AND_LOAD_CONTRACT"

private val SERVER_IP_KEY = stringPreferencesKey("server_ip_key")
private val SERVER_PORT_KEY = intPreferencesKey("server_port_key")
private val SERVER_PROTOCOL_KEY = stringPreferencesKey("server_protocol_key")
private val SERVER_ENDPOINT_KEY = stringPreferencesKey("server_endpoint_key")
private val SERVER_BASIC_AUTH = stringPreferencesKey("server_basicauth_key")

data class KeypleServiceState(
    val serverReachable: Boolean = false,
    val outputData: OutputData? = null
)

private const val TAG = "KeypleService"

class KeypleService(
    private val reader: LocalReader,
    private val clientId: String,
    private val dataStore: DataStore<Preferences>,
    private val cardRepository: CardRepository,
    private val buzzer: Buzzer
) {

  private var _state: MutableStateFlow<KeypleServiceState> = MutableStateFlow(KeypleServiceState())
  val state = _state.asStateFlow()

  private var serverConfig: KeypleServerConfig? = null
  private var remoteService: KeypleTerminal? = null
  private val httpClient = HttpClient {
    install(ContentNegotiation) {
      json(
          Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
          })
    }
    install(HttpTimeout) {
      requestTimeoutMillis = 15000
      socketTimeoutMillis = 16000
    }
    expectSuccess = true
    followRedirects = true
  }
  private var pingJob: Job? = null

  fun start() {
    loadServerConfig()

    val server = SimpleHttpNetworkClient(serverConfig!!, buildHttpClient(LogLevel.ALL))

    remoteService = KeypleTerminal(reader = reader, clientId = clientId, networkClient = server)

    launchPingJob()
    launchGetCardSelectionScenarioJob()
  }

  private fun loadServerConfig() {
    runBlocking {
      val preferences = dataStore.data.first()
      val serverIp = preferences[SERVER_IP_KEY] ?: "82.96.147.205"
      val serverPort = preferences[SERVER_PORT_KEY] ?: 42080
      val serverProtocol = preferences[SERVER_PROTOCOL_KEY] ?: "http"
      val serverEndpoint = preferences[SERVER_ENDPOINT_KEY] ?: "/card/remote-plugin"
      val basicAuth = preferences[SERVER_BASIC_AUTH] ?: ""

      serverConfig =
          KeypleServerConfig(
              "$serverProtocol://$serverIp", serverPort, serverEndpoint, basicAuth = basicAuth)
    }
  }

  fun observeServerConfig(): Flow<KeypleServerConfig> {
    return dataStore.data.map { preferences ->
      val serverIp = preferences[SERVER_IP_KEY] ?: "82.96.147.205"
      val serverPort = preferences[SERVER_PORT_KEY] ?: 42080
      val serverProtocol = preferences[SERVER_PROTOCOL_KEY] ?: "http"
      val serverEndpoint = preferences[SERVER_ENDPOINT_KEY] ?: "/card/remote-plugin"

      KeypleServerConfig("$serverProtocol://$serverIp", serverPort, serverEndpoint)
    }
  }

  suspend fun updateServerConfig(host: String, port: Int, protocol: String, endpoint: String) {
    dataStore.edit { preferences ->
      preferences[SERVER_IP_KEY] = host
      preferences[SERVER_PORT_KEY] = port
      preferences[SERVER_PROTOCOL_KEY] = protocol
      preferences[SERVER_ENDPOINT_KEY] = endpoint
    }
  }

  private fun launchPingJob() {
    pingJob?.cancel()
    pingJob =
        remoteService?.let {
          GlobalScope.launch {
            while (true) {
              try {
                val response = pingServer()
                _state.value = _state.value.copy(serverReachable = true)
                Napier.d("Ping response: $response")
              } catch (e: Exception) {
                Napier.e("Error pinging server: ${e.message}")
                _state.value = _state.value.copy(serverReachable = false)
              }
              kotlinx.coroutines.delay(5000)
            }
          }
        }
  }

  private fun launchGetCardSelectionScenarioJob() {
    remoteService?.let {
      GlobalScope.launch {
        try {
          val scenarioJsonString = retrieveSelectionScenarioJson()
          Napier.d("Card Selection Scenario retrieved: $scenarioJsonString")
          remoteService?.setCardSelectionScenarioJsonString(scenarioJsonString)
        } catch (e: Exception) {
          Napier.e("Error getting card selection scenario: ${e.message}")
        }
      }
    }
  }

  private suspend fun pingServer(): String {
    return try {
      httpClient.get("${serverConfig?.baseUrl()}/card/sam-status") {}.body<String>()
    } catch (e: Exception) {
      throw ServerIOException("Comm error: ${e.message}")
    }
  }

  private suspend fun retrieveSelectionScenarioJson(): String {
    return try {
      httpClient
          .get("${serverConfig?.baseUrl()}/card/export-card-selection-scenario") {}
          .body<String>()
    } catch (e: Exception) {
      throw ServerIOException("Comm error: ${e.message}")
    }
  }

  fun buzzer() {
    buzzer.beepAndVibrate()
  }

  suspend fun waitCard(): Boolean {
    return withContext(Dispatchers.IO) {
      val result = remoteService?.waitForCard() ?: false
      buzzer()
      return@withContext result
    }
  }

  fun waitForCard(cardConnected: () -> Unit) {
    this.remoteService?.waitForCard(cardConnected)
  }

  fun updateReaderMessage(msg: String) {
    remoteService?.setScanMessage(msg)
  }

  fun releaseReader() {
    remoteService?.releaseReader()
  }

  suspend fun selectCardAndAnalyseContracts(): KeypleResult<SelectAppAndAnalyzeContractsOutputDto> {
    return withContext(Dispatchers.IO) {
      cardRepository.clear()
      remoteService?.let { service ->
        val result: KeypleResult<SelectAppAndAnalyzeContractsOutputDto> =
            executeService(
                service,
                SELECT_APP_AND_ANALYZE_CONTRACTS,
                GenericSelectAppInputDto(),
                GenericSelectAppInputDto.serializer(),
                SelectAppAndAnalyzeContractsOutputDto.serializer())

        when (result) {
          is KeypleResult.Failure -> {
            return@withContext result
          }
          is KeypleResult.Success -> {
            cardRepository.saveCardSerial(result.data.applicationSerialNumber)
            cardRepository.saveCardContracts(result.data.validContracts)
            return@withContext KeypleResult.Success(result.data)
          }
        }
      } ?: throw IllegalStateException("Remote service not initialized")
    }
  }

  suspend fun personalizeCard(): KeypleResult<String> {
    return withContext(Dispatchers.IO) {
      remoteService?.let {
        val result =
            executeServiceWithGenericOutput(
                it,
                SELECT_APP_AND_PERSONALIZE_CARD,
                GenericSelectAppInputDto(),
                GenericSelectAppInputDto.serializer())
        when (result) {
          is KeypleResult.Failure -> {
            return@withContext result
          }
          is KeypleResult.Success -> {
            return@withContext KeypleResult.Success("Success")
          }
        }
      } ?: throw IllegalStateException("Remote service not initialized")
    }
  }

  suspend fun selectCardAndWriteContract(
      ticketNumber: Int,
      code: PriorityCode
  ): KeypleResult<String> {
    return withContext(Dispatchers.IO) {
      remoteService?.let {
        val result =
            executeServiceWithGenericOutput(
                it,
                SELECT_APP_AND_LOAD_CONTRACT,
                WriteContract(
                    applicationSerialNumber = cardRepository.getCardSerial(),
                    contractTariff = code,
                    ticketToLoad = ticketNumber),
                WriteContract.serializer())
        when (result) {
          is KeypleResult.Failure -> {
            return@withContext result
          }
          is KeypleResult.Success -> {
            return@withContext KeypleResult.Success("Success")
          }
        }
      } ?: throw IllegalStateException("Remote service not initialized")
    }
  }

  private suspend fun <T> executeServiceWithGenericOutput(
      remote: KeypleTerminal,
      service: String,
      inputData: T? = null,
      inputSerializer: KSerializer<T>,
  ): KeypleResult<String> {
    when (val result =
        remote.executeRemoteService(service, inputData, inputSerializer, OutputData.serializer())) {
      is KeypleResult.Failure -> {
        Napier.e(
            tag = TAG,
            message = "Error executing service: ${result.status} ${result.message} ${result.data}")
        return KeypleResult.Failure(
            status = result.status,
            message =
                "Error is ${result.status} - Server side: ${result.data?.statusCode} / ${result.data?.message}")
      }
      is KeypleResult.Success -> {
        if (result.data != null) {
          if (result.data!!.statusCode != 0) {
            Napier.i(tag = TAG, message = "Output = ${result.data!!.message}")
            return KeypleResult.Failure(
                status = Status.SERVER_ERROR,
                message =
                    "Server side error: ${result.data!!.statusCode} / ${result.data!!.message}")
          } else {
            val res = Json.encodeToString(result.data?.items ?: emptyList())
            Napier.i(tag = TAG, message = "Output = $res")
            return KeypleResult.Success(res)
          }
        } else {
          return KeypleResult.Success("")
        }
      }
    }
  }

  private val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
  }

  private suspend fun <T, R> executeService(
      remote: KeypleTerminal,
      service: String,
      inputData: T? = null,
      inputSerializer: KSerializer<T>,
      outputSerializer: KSerializer<R>,
  ): KeypleResult<R> {

    var inputDataStr: String? = null
    inputData?.let { inputDataStr = json.encodeToString(inputSerializer, inputData) }

    when (val result = remote.executeRemoteService(service, inputDataStr)) {
      is KeypleResult.Failure -> {
        Napier.e(
            tag = TAG,
            message = "Error executing service: ${result.status} ${result.message}, ${result.data}")
        return KeypleResult.Failure(result.status, result.message)
      }
      is KeypleResult.Success -> {
        val resStr: String = result.data!!
        val res = Json.decodeFromString(outputSerializer, resStr)
        return KeypleResult.Success(res)
      }
    }
  }
}
