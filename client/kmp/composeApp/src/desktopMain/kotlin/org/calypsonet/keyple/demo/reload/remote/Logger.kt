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
