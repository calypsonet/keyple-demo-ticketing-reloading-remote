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

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.eclipse.keyple.keyplelessreaderlib.LocalNfcReader

fun main() = application {
  initLogger()

  val windowState = rememberWindowState(placement = WindowPlacement.Floating)

  // TODO move all this to a DI module
  val cardRepository = CardRepository()
  val keypleService =
      KeypleService(
          reader = LocalNfcReader(),
          clientId = "SOMEID",
          dataStore = createDataStore(DataStorePathProducer()),
          cardRepository = cardRepository)

  Window(
      // icon = TODO
      title = "Keyple Demo Reload Remote",
      state = windowState,
      onCloseRequest = ::exitApplication) {
        App(keypleService, cardRepository)
      }
}
