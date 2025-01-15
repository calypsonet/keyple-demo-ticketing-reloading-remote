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

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.eclipse.keyple.keyplelessreaderlib.LocalNfcReader
import platform.UIKit.UIDevice

val dataStrore = createDataStore(DataStorePathProducer())

val cardRepository = CardRepository()

val remoteService =
    KeypleService(
        reader =
            MultiplatformReader(
                LocalNfcReader() { error ->
                  return@LocalNfcReader "Error: ${error.message}"
                }),
        clientId = UIDevice.currentDevice.identifierForVendor?.UUIDString() ?: "anon",
        cardRepository = cardRepository,
        dataStore = dataStrore)

val logger = Napier.base(DebugAntilog())

fun MainViewController() = ComposeUIViewController { App(remoteService, cardRepository) }
