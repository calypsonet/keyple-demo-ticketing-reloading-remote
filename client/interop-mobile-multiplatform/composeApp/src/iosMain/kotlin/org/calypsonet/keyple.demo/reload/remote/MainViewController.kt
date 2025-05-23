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

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.eclipse.keyple.interop.localreader.nfcmobile.api.*
import platform.UIKit.UIDevice

val dataStore = createDataStore(DataStorePathProducer())

val cardRepository = CardRepository()

val remoteService =
    KeypleService(
        reader =
            MultiplatformNfcReader(
                LocalNfcReader() { error ->
                  return@LocalNfcReader "Error: ${error.message}"
                }),
        clientId = UIDevice.currentDevice.identifierForVendor?.UUIDString() ?: "anon",
        cardRepository = cardRepository,
        dataStore = dataStore,
        buzzer = Buzzer(PlatformBuzzer()))

val logger = Napier.base(DebugAntilog())

fun MainViewController() = ComposeUIViewController { App(remoteService, cardRepository) }
