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

import kotlinx.serialization.Serializable

@Serializable data class InputDataIncreaseCounter(val counterIncrement: String = "1")

@Serializable class InputData

@Serializable data class GenericSelectAppInputDto(val pluginType: String = "Android NFC")

@Serializable
data class SelectAppAndAnalyzeContractsOutputDto(
    val applicationSerialNumber: String,
    val validContracts: List<ContractInfo>,
    val message: String,
    val statusCode: Int,
)

@Serializable
data class OutputData(
    val items: List<String>?,
    val statusCode: Int,
    val message: String,
)

@Serializable
data class ContractInfo(
    val title: String,
    val description: String,
    val isValid: Boolean,
)
