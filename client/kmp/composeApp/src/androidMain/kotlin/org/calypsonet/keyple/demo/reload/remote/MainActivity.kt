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

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.eclipse.keyple.interop.localreader.nfcmobile.LocalNfcReader
import org.eclipse.keyple.interop.localreader.nfcmobile.MultiplatformNfcReader

class MainActivity : ComponentActivity() {
  @SuppressLint("HardwareIds")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val cardRepository = CardRepository()
    val keypleService =
        KeypleService(
            reader = MultiplatformNfcReader(LocalNfcReader(this)),
            clientId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),
            dataStore = createDataStore(DataStorePathProducer(applicationContext)),
            cardRepository = cardRepository,
            buzzer = Buzzer(PlatformBuzzer(applicationContext)))

    setContent { App(keypleService, cardRepository) }
  }
}
