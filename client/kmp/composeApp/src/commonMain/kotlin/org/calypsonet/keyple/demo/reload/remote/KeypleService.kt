/* **************************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
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
import org.calypso.keyple.iso7816nfcreader.LocalNfcReader
import org.calypso.keyple.remote.network.KeypleServerConfig
import org.calypso.keyple.remote.network.ServerIOException
import org.calypso.keyple.remote.protocol.KeypleError
import org.calypso.keyple.remote.protocol.KeypleRemoteService
import org.calypso.keyple.remote.protocol.KeypleResult
import org.calypso.keyple.remote.protocol.LogLevel
import org.calypsonet.keyple.demo.reload.remote.nfc.write.AnalyzeContracts
import org.calypsonet.keyple.demo.reload.remote.nfc.write.PriorityCode
import org.calypsonet.keyple.demo.reload.remote.nfc.write.WriteContract

const val SELECT_APP_AND_READ_CONTRACTS = "SELECT_APP_AND_READ_CONTRACTS"
const val SELECT_APP_AND_INCREASE_CONTRACT_COUNTER = "SELECT_APP_AND_INCREASE_CONTRACT_COUNTER"
const val PERSONALIZE_CARD = "PERSONALIZE_CARD"
const val READ_CARD_AND_ANALYZE_CONTRACTS = "READ_CARD_AND_ANALYZE_CONTRACTS"
const val READ_CARD_AND_WRITE_CONTRACT = "READ_CARD_AND_WRITE_CONTRACT"

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
    private val reader: LocalNfcReader,
    private val clientId: String,
    private val dataStore: DataStore<Preferences>,
    private val cardRepository: CardRepository
) {

  private var _state: MutableStateFlow<KeypleServiceState> = MutableStateFlow(KeypleServiceState())
  val state = _state.asStateFlow()

  private var serverConfig: KeypleServerConfig? = null
  private var remoteService: KeypleRemoteService? = null
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
    remoteService =
        KeypleRemoteService(
            localNfcReader = reader,
            clientId = clientId,
            config = serverConfig!!,
        )

    launchPingJob()
  }

  // TODO maybe move this to a dedicated repository
  private fun loadServerConfig() {
    runBlocking {
      val preferences = dataStore.data.first()
      val serverIp = preferences[SERVER_IP_KEY] ?: "82.96.147.205"
      val serverPort = preferences[SERVER_PORT_KEY] ?: 42080
      val serverProtocol = preferences[SERVER_PROTOCOL_KEY] ?: "http"
      val serverEndpoint = preferences[SERVER_ENDPOINT_KEY] ?: "/card/remote-plugin"
      val basicAuth = preferences[SERVER_BASIC_AUTH] ?: ""

      val logLevel = LogLevel.DEBUG

      serverConfig =
          KeypleServerConfig(
              "$serverProtocol://$serverIp",
              serverPort,
              serverEndpoint,
              logLevel,
              basicAuth = basicAuth)
    }
  }

  fun observeServerConfig(): Flow<KeypleServerConfig> {
    return dataStore.data.map { preferences ->
      val serverIp = preferences[SERVER_IP_KEY] ?: "82.96.147.205"
      val serverPort = preferences[SERVER_PORT_KEY] ?: 42080
      val serverProtocol = preferences[SERVER_PROTOCOL_KEY] ?: "http"
      val serverEndpoint = preferences[SERVER_ENDPOINT_KEY] ?: "/card/remote-plugin"
      val logLevel = LogLevel.DEBUG

      KeypleServerConfig("$serverProtocol://$serverIp", serverPort, serverEndpoint, logLevel)
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
          // TODO setup a proper app scope for this or maybe dont do it here?
          GlobalScope.launch {
            while (true) {
              try {
                val response = pingServer()
                _state.value = _state.value.copy(serverReachable = true)
                Napier.d("Ping response: $response")
              } catch (e: Exception) {
                Napier.e("Error pinging server: ${e.message}")
                _state.value = _state.value.copy(serverReachable = true)
              }
              kotlinx.coroutines.delay(5000)
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

  suspend fun waitCard(): Boolean {
    return withContext(Dispatchers.IO) {
      return@withContext remoteService?.waitForCard() ?: false
    }
  }

  fun updateReaderMessage(msg: String) {
    remoteService?.setScanMessage(msg)
  }

  fun releaseReader() {
    remoteService?.releaseReader()
  }

  suspend fun selectCardAndReadContracts(): KeypleResult<CardContracts> {
    return withContext(Dispatchers.IO) {
      cardRepository.clear()
      remoteService?.let { service ->
        val result: KeypleResult<String> =
            executeService(
                service, SELECT_APP_AND_READ_CONTRACTS, InputData(), InputData.serializer())

        when (result) {
          is KeypleResult.Failure -> {
            return@withContext KeypleResult.Failure(result.error)
          }
          is KeypleResult.Success -> {
            val contracts = CardContractsBuilder().build(result.data)
            cardRepository.saveCardContracts(contracts)
            return@withContext KeypleResult.Success(contracts)
          }
        }
      } ?: throw IllegalStateException("Remote service not initialized")
    }
  }

  suspend fun readCardAndAnalyzeContracts(payload: AnalyzeContracts): KeypleResult<CardContracts> {
    return withContext(Dispatchers.IO) {
      cardRepository.clear()
      remoteService?.let { service ->
        val result =
            executeService(
                service, READ_CARD_AND_ANALYZE_CONTRACTS, payload, AnalyzeContracts.serializer())

        when (result) {
          is KeypleResult.Failure -> {
            return@withContext KeypleResult.Failure(result.error)
          }
          is KeypleResult.Success -> {
            val contracts = CardContractsBuilder().build(result.data)

            cardRepository.saveCardContracts(contracts)
            return@withContext KeypleResult.Success(contracts)
          }
        }
      } ?: throw IllegalStateException("Remote service not initialized")
    }
  }

  // TODO define what we want to return here?
  // TODO check how we should pass inputdata?
  suspend fun selectCardAndIncreaseContractCounter(nbUnits: Int): KeypleResult<String> {
    return withContext(Dispatchers.IO) {
      remoteService?.let {
        val result =
            executeService(
                it,
                SELECT_APP_AND_INCREASE_CONTRACT_COUNTER,
                InputDataIncreaseCounter(nbUnits.toString()),
                InputDataIncreaseCounter.serializer())
        when (result) {
          is KeypleResult.Failure -> {
            return@withContext KeypleResult.Failure(result.error)
          }
          is KeypleResult.Success -> {
            return@withContext KeypleResult.Success("Success")
          }
        }
      } ?: throw IllegalStateException("Remote service not initialized")
    }
  }

  suspend fun personalizeCard(): KeypleResult<String> {
    return withContext(Dispatchers.IO) {
      remoteService?.let {
        val result = executeService(it, PERSONALIZE_CARD, InputData(), InputData.serializer())
        when (result) {
          is KeypleResult.Failure -> {
            return@withContext KeypleResult.Failure(result.error)
          }
          is KeypleResult.Success -> {
            return@withContext KeypleResult.Success("Success")
          }
        }
      } ?: throw IllegalStateException("Remote service not initialized")
    }
  }

  suspend fun readCardAndWriteContracts(
      ticketNumber: Int,
      code: PriorityCode
  ): KeypleResult<String> {
    return withContext(Dispatchers.IO) {
      remoteService?.let {
        val result =
            executeService(
                it,
                READ_CARD_AND_WRITE_CONTRACT,
                WriteContract(contractTariff = code, ticketToLoad = ticketNumber),
                WriteContract.serializer())
        when (result) {
          is KeypleResult.Failure -> {
            return@withContext KeypleResult.Failure(result.error)
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
            executeService(
                it,
                SELECT_APP_AND_INCREASE_CONTRACT_COUNTER,
                WriteContract(contractTariff = code, ticketToLoad = ticketNumber),
                WriteContract.serializer())
        when (result) {
          is KeypleResult.Failure -> {
            return@withContext KeypleResult.Failure(result.error)
          }
          is KeypleResult.Success -> {
            return@withContext KeypleResult.Success("Success")
          }
        }
      } ?: throw IllegalStateException("Remote service not initialized")
    }
  }

  private suspend fun <T> executeService(
      remote: KeypleRemoteService,
      service: String,
      inputData: T? = null,
      inputSerializer: KSerializer<T>
  ): KeypleResult<String> {
    when (val result =
        remote.executeRemoteService(service, inputData, inputSerializer, OutputData.serializer())) {
      is KeypleResult.Failure -> {
        Napier.e(tag = TAG, message = "Error executing service: ${result.error}")
        return KeypleResult.Failure(result.error)
      }
      is KeypleResult.Success -> {
        if (result.data != null) {
          if (result.data!!.statusCode != 0) {
            Napier.i(tag = TAG, message = "Output = ${result.data!!.message}")
            return KeypleResult.Failure(
                KeypleError(statusCode = result.data!!.statusCode, message = result.data!!.message))
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
}
