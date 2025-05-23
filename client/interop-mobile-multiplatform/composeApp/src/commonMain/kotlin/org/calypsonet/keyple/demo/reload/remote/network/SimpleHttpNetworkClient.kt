/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
@file:OptIn(ExperimentalEncodingApi::class)

package org.calypsonet.keyple.demo.reload.remote.network

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
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
import org.eclipse.keyple.interop.jsonapi.client.api.MessageDto
import org.eclipse.keyple.interop.jsonapi.client.api.ServerIOException
import org.eclipse.keyple.interop.jsonapi.client.spi.SyncNetworkClient

class SimpleHttpNetworkClient(val config: KeypleServerConfig, val httpClient: HttpClient) :
    SyncNetworkClient {

  var basicAuth: String? = config.basicAuth

  override suspend fun sendRequest(message: MessageDto): List<MessageDto> {
    return try {
      val json: List<MessageDto> =
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
        level = debugLog
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
