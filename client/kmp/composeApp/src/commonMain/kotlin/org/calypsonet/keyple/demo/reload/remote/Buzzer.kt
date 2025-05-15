/* ******************************************************************************
 * Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote

class Buzzer(val buzzer: PlatformBuzzer) {
  fun vibrate() {
    buzzer.vibrate()
  }

  fun beep() {
    buzzer.beep()
  }

  fun beepAndVibrate() {
    vibrate()
    beep()
  }
}

expect class PlatformBuzzer {
  fun vibrate()

  fun beep()
}
