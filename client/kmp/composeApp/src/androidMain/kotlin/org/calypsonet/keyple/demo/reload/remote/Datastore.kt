package org.calypsonet.keyple.demo.reload.remote

import android.content.Context

actual class DataStorePathProducer(private val context: Context) {
    actual fun producePath(): String {
        return context.filesDir.resolve(dataStoreFileName).absolutePath
    }
}
