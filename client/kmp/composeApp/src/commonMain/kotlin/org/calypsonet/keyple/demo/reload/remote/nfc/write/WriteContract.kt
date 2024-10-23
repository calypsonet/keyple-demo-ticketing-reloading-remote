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
package org.calypsonet.keyple.demo.reload.remote.nfc.write

import kotlinx.serialization.Serializable

@Serializable
enum class PriorityCode {
  FORBIDDEN,
  SEASON_PASS,
  MULTI_TRIP,
  STORED_VALUE,
  EXPIRED,
  UNKNOWN
}

@Serializable
data class WriteContract(
    val contractTariff: PriorityCode,
    val pluginType: String = "Android NFC",
    val ticketToLoad: Int
)

@Serializable data class AnalyzeContracts(val pluginType: String = "Android NFC")
