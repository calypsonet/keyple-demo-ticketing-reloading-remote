/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val blue = Color(0xFF1D87BB)
val red = Color(0xFFB7003A)
val orange = Color(0xFFE47A27)
val green = Color(0xFF1DAB5D)
val accent = Color(0xFFFF4081)
val purple = Color(0xFF303F9F)
val darkBlue = Color(0xFF213b6c)
val grey = Color(0xFF808080)
val lineBlue = Color(0xFF8db9dd)
val lightBlue = Color(0xFFe8f3f9)
val lightGrey = Color(0xFFd3d3d3)
val white = Color(0xFFFFFFFF)

private val LightColors =
    lightColorScheme(
        primary = blue,
        onPrimary = white,
        onSurface = darkBlue,
    )

private val DarkColors =
    lightColorScheme(
        primary = purple,
        onPrimary = darkBlue,
        onSurface = darkBlue,
    )

@Composable
fun KeypleTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
) {
  val colors = if (!useDarkTheme) LightColors else DarkColors

  MaterialTheme(
      colorScheme = colors,
      content = content,
  )
}
