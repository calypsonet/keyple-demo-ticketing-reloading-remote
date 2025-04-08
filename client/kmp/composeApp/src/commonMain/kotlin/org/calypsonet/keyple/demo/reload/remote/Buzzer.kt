/* **************************************************************************************
 * Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
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
