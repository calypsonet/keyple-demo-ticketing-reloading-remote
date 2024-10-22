package org.calypsonet.keyple.demo.reload.remote.nfc.write

import kotlinx.serialization.Serializable

@Serializable
enum class PriorityCode {
    FORBIDDEN,
    SEASON_PASS,
    MULTI_TRIP,
    STORED_VALUE,
    EXPIRED,
    UNKNOWN;
}

@Serializable
data class WriteContract(
    val contractTariff: PriorityCode,
    val pluginType: String = "Android NFC",
    val ticketToLoad: Int
)

@Serializable
data class AnalyzeContracts(
    val pluginType: String = "Android NFC"
)