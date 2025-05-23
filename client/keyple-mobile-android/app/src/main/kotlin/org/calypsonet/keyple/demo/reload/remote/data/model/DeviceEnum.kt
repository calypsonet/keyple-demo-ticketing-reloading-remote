/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.data.model

import java.util.*
import org.calypsonet.keyple.demo.reload.remote.R

enum class DeviceEnum(val textId: Int) {
  CONTACTLESS_CARD(R.string.contactless_card),
  SIM(R.string.sim_card),
  WEARABLE(R.string.wearable),
  EMBEDDED(R.string.embedded_secure_elem);

  companion object {
    @JvmStatic
    fun getDeviceEnum(name: String): DeviceEnum {
      return try {
        valueOf(name.uppercase(Locale.ROOT))
      } catch (e: Exception) {
        // If the given state does not exist, return the default value.
        CONTACTLESS_CARD
      }
    }
  }
}
