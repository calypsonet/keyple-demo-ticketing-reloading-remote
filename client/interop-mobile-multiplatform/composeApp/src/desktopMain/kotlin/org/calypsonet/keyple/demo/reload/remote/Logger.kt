/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.SimpleFormatter

fun initLogger() {
  Napier.base(DebugAntilog(handler = listOf(consoleHandler)))
}

private val consoleHandler: ConsoleHandler =
    ConsoleHandler().apply {
      level = Level.ALL
      formatter =
          object : SimpleFormatter() {
            override fun format(logRecord: java.util.logging.LogRecord): String {
              return "${logRecord.message}\n"
            }
          }
    }
