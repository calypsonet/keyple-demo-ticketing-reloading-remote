package org.calypsonet.keyple.demo.reload.remote.nav

import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data object Settings

fun String.toScanNavArgs(): ScanNavArgs {
    return when (this) {
        "read-contracts" -> ScanNavArgs.READ_CONTRACTS
        "personalize-card" -> ScanNavArgs.PERSONALIZE_CARD
        "write-title" -> ScanNavArgs.WRITE_TITLE
        else -> throw IllegalArgumentException()
    }
}

enum class ScanNavArgs(val value: String) {
    READ_CONTRACTS("read-contracts"),
    PERSONALIZE_CARD("personalize-card"),
    WRITE_TITLE("write-title");
}

@Serializable
data class Scan(val action: String = ScanNavArgs.READ_CONTRACTS.value)

@Serializable
data class WriteCard(val nbTickets: Int = 0)

@Serializable
data object PersonalizeCard

@Serializable
data object ReadCard

@Serializable
data object Card

@Serializable
data object AppError

@Serializable
data object AppSuccess

@Serializable
data object ServerConfig