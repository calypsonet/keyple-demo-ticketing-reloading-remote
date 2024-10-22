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

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineUnavailableException
import kotlin.math.sin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

actual class PlatformBuzzer {
  actual fun vibrate() {
    // do nothing on desktop...
  }

  var SAMPLE_RATE: Float = 8000f

  @Throws(LineUnavailableException::class)
  fun tone(hz: Int, msecs: Int) {
    tone(hz, msecs, 1.0)
  }

  @Throws(LineUnavailableException::class)
  fun tone(hz: Int, msecs: Int, vol: Double) {
    GlobalScope.launch(Dispatchers.IO) {
      val buf = ByteArray(1)
      val af =
          AudioFormat(
              SAMPLE_RATE, // sampleRate
              8, // sampleSizeInBits
              1, // channels
              true, // signed
              false) // bigEndian
      val sdl = AudioSystem.getSourceDataLine(af)
      sdl.open(af)
      sdl.start()
      for (i in 0 ..< msecs * 8) {
        val angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI
        buf[0] = (sin(angle) * 127.0 * vol).toInt().toByte()
        sdl.write(buf, 0, 1)
      }
      sdl.drain()
      sdl.stop()
      sdl.close()
    }
  }

  actual fun beep() {
    tone(1200, 100, 1.0)
  }
}
