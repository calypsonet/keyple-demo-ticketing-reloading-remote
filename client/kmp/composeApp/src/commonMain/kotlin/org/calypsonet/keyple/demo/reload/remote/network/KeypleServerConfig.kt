/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.network

data class KeypleServerConfig(
    val host: String,
    val port: Int,
    val endpoint: String,
    val basicAuth: String? = null,
) {
  fun baseUrl() = "${host}:${port}"

  fun serviceUrl() = "${baseUrl()}${endpoint}"
}
