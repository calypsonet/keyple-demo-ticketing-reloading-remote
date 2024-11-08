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

@Serializable data class WriteTitleCard(val type: Int,
                                        val price: Int,
                                        val quantity: Int = 1,
                                        val date: String? = null) {
  companion object {
    operator fun invoke(title: Title): WriteTitleCard {
      return WriteTitleCard(title.type.ordinal, title.price, title.quantity, title.date)
    }
  }
}

@Serializable data object PersonalizeCard

@Serializable data object ReadCard

@Serializable data object Card

@Serializable data object AppError

@Serializable data object AppSuccess

@Serializable data object ServerConfig
