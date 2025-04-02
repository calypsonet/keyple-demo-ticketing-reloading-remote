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
@file:OptIn(ExperimentalEncodingApi::class)

package org.calypsonet.keyple.demo.reload.remote.network

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.json.Json
import org.eclipse.keyple.keypleless.distributed.client.protocol.LogLevel
import org.eclipse.keyple.keypleless.distributed.client.protocol.MessageDTO
import org.eclipse.keyple.keypleless.distributed.client.spi.ServerIOException
import org.eclipse.keyple.keypleless.distributed.client.spi.SyncNetworkClient

class SimpleHttpNetworkClient(val config: KeypleServerConfig, val httpClient: HttpClient) :
    SyncNetworkClient {

  var basicAuth: String? = config.basicAuth

  override suspend fun sendRequest(message: MessageDTO): List<MessageDTO> {
    return try {
      val json: List<MessageDTO> =
          httpClient
              .post(config.serviceUrl()) {
                headers {
                  append(HttpHeaders.ContentType, "application/json")
                  basicAuth?.let {
                    append(
                        HttpHeaders.Authorization,
                        "Basic " + Base64.encode(basicAuth!!.encodeToByteArray()))
                  }
                }
                setBody(message)
              }
              .body()
      json
    } catch (e: Exception) {
      throw ServerIOException("Comm error: ${e.message}")
    }
  }
}

fun buildHttpClient(debugLog: LogLevel): HttpClient {
  return HttpClient {
    install(ContentNegotiation) {
      json(
          Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
          })
    }
    if (debugLog != LogLevel.NONE) {
      install(Logging) {
        logger =
            object : Logger {
              override fun log(message: String) {
                Napier.d(tag = "HTTP", message = message)
              }
            }
        level = makeKtorLogLevel(debugLog)
      }
    }
    install(HttpTimeout) {
      requestTimeoutMillis = 35000
      socketTimeoutMillis = 36000
    }
    expectSuccess = true
    followRedirects = true
    install(HttpCookies)
  }
}

private fun makeKtorLogLevel(log: LogLevel): io.ktor.client.plugins.logging.LogLevel {
  return when (log) {
    LogLevel.DEBUG -> io.ktor.client.plugins.logging.LogLevel.ALL
    LogLevel.INFO -> io.ktor.client.plugins.logging.LogLevel.INFO
    else -> io.ktor.client.plugins.logging.LogLevel.NONE
  }
}
