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
package org.calypsonet.keyple.demo.reload.remote.nfc.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import keyplelessremotedemo.composeapp.generated.resources.Res
import keyplelessremotedemo.composeapp.generated.resources.ic_logo_calypso
import keyplelessremotedemo.composeapp.generated.resources.keyple_background
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.calypsonet.keyple.demo.reload.remote.ui.KeypleTopAppBar
import org.calypsonet.keyple.demo.reload.remote.ui.ScreenAnimByPlatform
import org.calypsonet.keyple.demo.reload.remote.ui.blue
import org.calypsonet.keyple.demo.reload.remote.ui.red
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ScanScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    appState: AppState,
    onBack: () -> Unit = { navController.popBackStack() },
    content: @Composable () -> Unit,
) {
  Scaffold(
      modifier = modifier,
  ) { innerPadding ->
    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
      Image(
          imageVector = vectorResource(Res.drawable.keyple_background),
          contentDescription = "Keyple app background",
          contentScale = ContentScale.FillBounds,
          modifier = Modifier.matchParentSize())
      Column(
          Modifier.fillMaxSize().align(Alignment.Center),
          horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        KeypleTopAppBar(
            navController = navController,
            appState = appState,
            onBack = onBack,
        )

        Column(
            modifier = modifier.padding(horizontal = 38.dp).sizeIn(maxWidth = 400.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Text(
              text = "Open Source API for Smart Ticketing",
              modifier = Modifier.widthIn(max = 200.dp),
              color = blue,
              textAlign = TextAlign.Center,
          )

          Spacer(modifier = Modifier.weight(1f))

          content()

          Spacer(modifier = Modifier.weight(1f))

          Image(
              imageVector = vectorResource(Res.drawable.ic_logo_calypso),
              contentDescription = "Keyple logo",
              modifier = Modifier.padding(bottom = 4.dp))
        }
      }
    }
  }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun PresentCardAnimation() {
  val composition by rememberLottieComposition {
    LottieCompositionSpec.JsonString(Res.readBytes("files/anim_card_scan.json").decodeToString())
  }
  val progress by
      animateLottieCompositionAsState(composition, iterations = Compottie.IterateForever)

  ScreenAnimByPlatform("Please present a contactless support", blue, composition, progress)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ScanCardAnimation() {
  val composition by rememberLottieComposition {
    LottieCompositionSpec.JsonString(Res.readBytes("files/anim_loading.json").decodeToString())
  }
  val progress by
      animateLottieCompositionAsState(composition, iterations = Compottie.IterateForever)

  ScreenAnimByPlatform("Read in progress", blue, composition, progress)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ReadingError(errorMessage: String) {
  val composition by rememberLottieComposition {
    LottieCompositionSpec.JsonString(Res.readBytes("files/anim_error.json").decodeToString())
  }
  val progress by
      animateLottieCompositionAsState(
          composition,
      )

  ScreenAnimByPlatform(errorMessage, red, composition, progress)
}
