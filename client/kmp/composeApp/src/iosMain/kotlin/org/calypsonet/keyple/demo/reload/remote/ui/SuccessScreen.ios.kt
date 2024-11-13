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
package org.calypsonet.keyple.demo.reload.remote.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
  Text(
      text = message,
      modifier = Modifier.padding(36.dp),
      color = textColor,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
  )
  Spacer(modifier = Modifier.height(130.dp))
  Image(
      painter =
          rememberLottiePainter(
              composition = composition,
              progress = { progress },
          ),
      modifier = Modifier.size(200.dp),
      contentDescription = "animation",
  )
}
