/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
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
    val applicationSerialNumber: String,
    val contractTariff: PriorityCode,
    val pluginType: String = "Android NFC",
    val ticketToLoad: Int
)

@Serializable data class AnalyzeContracts(val pluginType: String = "Android NFC")
