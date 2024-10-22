package org.calypsonet.keyple.demo.reload.remote

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.calypso.keyple.iso7816nfcreader.LocalNfcReader

class MainActivity : ComponentActivity() {
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO move all this to a DI module
        val cardRepository = CardRepository()
        val keypleService = KeypleService(
            reader = LocalNfcReader(this),
            clientId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),
            dataStore = createDataStore(DataStorePathProducer(applicationContext)),
            cardRepository = cardRepository
        )

        setContent {
            App(keypleService, cardRepository)
        }
    }
}