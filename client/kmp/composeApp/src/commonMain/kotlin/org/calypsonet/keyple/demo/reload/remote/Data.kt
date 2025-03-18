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
