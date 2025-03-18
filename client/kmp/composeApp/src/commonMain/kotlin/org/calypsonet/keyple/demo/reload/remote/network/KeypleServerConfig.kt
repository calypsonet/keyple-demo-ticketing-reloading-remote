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
package org.calypsonet.keyple.demo.reload.remote.network

import org.eclipse.keyple.keypleless.distributed.client.protocol.LogLevel

data class KeypleServerConfig(
    val host: String,
    val port: Int,
    val endpoint: String,
    val logLevel: LogLevel = LogLevel.NONE,
    val basicAuth: String? = null,
) {
  fun baseUrl() = "${host}:${port}"

  fun serviceUrl() = "${baseUrl()}${endpoint}"
}
