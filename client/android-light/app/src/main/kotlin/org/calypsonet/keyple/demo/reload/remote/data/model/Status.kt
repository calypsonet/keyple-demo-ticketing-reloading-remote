/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT License which is available at
 * https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.data.model

import java.util.*

enum class Status(private val status: String) {
  LOADING("loading"),
  ERROR("error"),
  TICKETS_FOUND("tickets_found"),
  INVALID_CARD("invalid_card"),
  EMPTY_CARD("empty_card"),
  SUCCESS("success");

  override fun toString(): String {
    return status
  }

  companion object {
    fun getStatus(name: String?): Status {
      return try {
        valueOf(name!!.uppercase(Locale.ROOT))
      } catch (e: Exception) {
        // If the given state does not exist, return the default value.
        ERROR
      }
    }
  }
}
