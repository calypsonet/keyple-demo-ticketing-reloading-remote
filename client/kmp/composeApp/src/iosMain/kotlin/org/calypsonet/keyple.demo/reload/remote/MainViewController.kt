package org.calypsonet.keyple.demo.reload.remote

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.calypso.keyple.iso7816nfcreader.LocalNfcReader
import platform.UIKit.UIDevice

val dataStrore = createDataStore(
    DataStorePathProducer()
)

val cardRepository = CardRepository()

val remoteService = KeypleService(
    reader = LocalNfcReader() {  error ->
        return@LocalNfcReader "Error: ${error.message}"
    },
    clientId = UIDevice.currentDevice.identifierForVendor?.UUIDString() ?: "anon",
    cardRepository = cardRepository,
    dataStore = dataStrore
)

val logger = Napier.base(DebugAntilog())

fun MainViewController() = ComposeUIViewController {
    App(remoteService, cardRepository)
}