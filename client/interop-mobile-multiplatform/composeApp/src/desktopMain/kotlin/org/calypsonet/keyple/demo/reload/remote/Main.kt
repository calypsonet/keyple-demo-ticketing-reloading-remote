/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.aakira.napier.Napier
import org.eclipse.keyple.interop.localreader.nfcmobile.api.LocalNfcReader
import org.eclipse.keyple.interop.localreader.nfcmobile.api.MultiplatformNfcReader

fun main(args: Array<String>) = application {
  initLogger()
  var filter = "*"

  val windowState =
      rememberWindowState(placement = WindowPlacement.Floating, width = 500.dp, height = 850.dp)
  for (arg: String in args) {
    if (arg.startsWith("-filter=")) {
      filter = arg.removePrefix("-filter=")
      Napier.d("Filter reader with: $filter")
    }
  }

  val cardRepository = CardRepository()
  val keypleService =
      KeypleService(
          reader = MultiplatformNfcReader(LocalNfcReader(filter)),
          clientId = "SOMEID",
          dataStore = createDataStore(DataStorePathProducer()),
          cardRepository = cardRepository,
          buzzer = Buzzer(PlatformBuzzer()))

  Window(
      // icon = TODO
      title = "Keyple Demo Reload Remote",
      state = windowState,
      onCloseRequest = ::exitApplication) {
        App(keypleService, cardRepository)
      }
}
