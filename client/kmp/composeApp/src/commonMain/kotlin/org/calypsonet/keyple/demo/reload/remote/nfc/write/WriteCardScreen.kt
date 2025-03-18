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
package org.calypsonet.keyple.demo.reload.remote.nfc.write

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.calypsonet.keyple.demo.reload.remote.nav.AppSuccess
import org.calypsonet.keyple.demo.reload.remote.nav.Home
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.PresentCardAnimation
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.ReadingError
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.ScanCardAnimation
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.ScanScreen
import org.calypsonet.keyple.demo.reload.remote.ui.green
import org.calypsonet.keyple.demo.reload.remote.ui.white

@Composable
fun WriteCardScreen(
    navController: NavController,
    viewModel: WriteCardScreenViewModel,
    modifier: Modifier = Modifier,
    appState: AppState
) {
  val state = viewModel.state.collectAsState()

  WriteCardScreen(
      navController = navController, state = state.value, modifier = modifier, appState = appState)
}

@Composable
internal fun WriteCardScreen(
    navController: NavController,
    state: WriteCardScreenState,
    modifier: Modifier = Modifier,
    appState: AppState
) {
  ScanScreen(
      navController = navController,
      modifier = modifier,
      appState = appState,
      onBack = { navController.navigate(Home) { popUpTo(Home) { inclusive = true } } }) {
        when (state) {
          WriteCardScreenState.WaitForCard -> {
            PresentCardAnimation()
          }
          WriteCardScreenState.WritingToCard -> {
            ScanCardAnimation()
          }
          is WriteCardScreenState.DisplayError -> {
            ReadingError(state.message)
          }
          WriteCardScreenState.ShowTransactionSuccess -> {
            navController.navigate(AppSuccess)
          }
        }
      }
}

@Composable
internal fun Success(title: String) {
  Column(
      Modifier.fillMaxSize().background(green),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center) {
        Text(
            text = title,
            color = white,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
      }
}
