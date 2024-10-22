package org.calypsonet.keyple.demo.reload.remote

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.SimpleFormatter

fun initLogger() {
    Napier.base(DebugAntilog(handler = listOf(consoleHandler)))
}

private val consoleHandler: ConsoleHandler = ConsoleHandler().apply {
    level = Level.ALL
    formatter = object : SimpleFormatter() {
        override fun format(logRecord: java.util.logging.LogRecord): String {
            return "${logRecord.message}\n"
        }
    }
}