/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.nav

import kotlinx.serialization.Serializable
import org.calypsonet.keyple.demo.reload.remote.card.Title

@Serializable data object Home

@Serializable data object Settings

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
  WRITE_TITLE("write-title")
}

@Serializable data class Scan(val action: String = ScanNavArgs.READ_CONTRACTS.value)

@Serializable
data class WriteTitleCard(
    val type: Int,
    val price: Int,
    val quantity: Int = 1,
    val date: String? = null,
    val cardSerial: String
) {
  companion object {
    operator fun invoke(title: Title, cardSerial: String = ""): WriteTitleCard {
      return WriteTitleCard(title.type.ordinal, title.price, title.quantity, title.date, cardSerial)
    }
  }
}

@Serializable data object PersonalizeCard

@Serializable data object ReadCard

@Serializable data object Card

@Serializable data object AppError

@Serializable data object AppSuccess

@Serializable data object ServerConfig
