package org.calypsonet.keyple.demo.reload.remote

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class KeypleDemoApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())
    }
}