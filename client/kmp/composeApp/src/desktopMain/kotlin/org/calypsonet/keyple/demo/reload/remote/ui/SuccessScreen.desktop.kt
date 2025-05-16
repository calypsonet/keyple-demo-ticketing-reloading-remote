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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

@Composable
actual fun ScreenAnimByPlatform(
    message: String,
    textColor: Color,
    composition: LottieComposition?,
    progress: Float
) {
  Image(
      painter =
          rememberLottiePainter(
              composition = composition,
              progress = { progress },
          ),
      modifier = Modifier.size(200.dp),
      contentDescription = "animation",
  )

  Text(
      text = message,
      modifier = Modifier.padding(36.dp),
      color = textColor,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
  )
}
