package org.calypsonet.keyple.demo.reload.remote

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.calypso.keyple.iso7816nfcreader.LocalNfcReader

fun main() = application {

    initLogger()

    val windowState = rememberWindowState(placement = WindowPlacement.Floating)

    // TODO move all this to a DI module
    val cardRepository = CardRepository()
    val keypleService = KeypleService(
        reader = LocalNfcReader(),
        clientId = "SOMEID",
        dataStore = createDataStore(DataStorePathProducer()),
        cardRepository = cardRepository
    )

    Window(
        //icon = TODO
        title = "Keyple Demo Reload Remote",
        state = windowState,
        onCloseRequest = ::exitApplication
    ) {
        App(keypleService, cardRepository)
    }
}