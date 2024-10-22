package org.calypsonet.keyple.demo.reload.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal const val dataStoreFileName = "keyple_client.preferences_pb"

expect class DataStorePathProducer {
    fun producePath(): String
}

fun createDataStore(pathProducer: DataStorePathProducer): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { pathProducer.producePath().toPath() }
    )
