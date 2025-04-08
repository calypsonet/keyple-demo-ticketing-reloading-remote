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
package org.calypsonet.keyple.demo.reload.remote

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.aakira.napier.Napier
import org.eclipse.keyple.keypleless.reader.nfcmobile.LocalNfcReader
import org.eclipse.keyple.keypleless.reader.nfcmobile.MultiplatformNfcReader

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

  // TODO move all this to a DI module
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
