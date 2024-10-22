package org.calypsonet.keyple.demo.reload.remote

actual class DataStorePathProducer {
    actual fun producePath(): String {
        return dataStoreFileName
    }
}