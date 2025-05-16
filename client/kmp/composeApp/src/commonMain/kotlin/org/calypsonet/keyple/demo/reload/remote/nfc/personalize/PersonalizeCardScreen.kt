/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.nfc.personalize

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.calypsonet.keyple.demo.reload.remote.nav.Home
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.PresentCardAnimation
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.ReadingError
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.ScanCardAnimation
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.ScanScreen
import org.calypsonet.keyple.demo.reload.remote.ui.blue

@Composable
fun PersonalizeCardScreen(
    navController: NavController,
    viewModel: PersonalizeCardScreenViewModel,
    modifier: Modifier = Modifier,
    appState: AppState
) {
  val state = viewModel.state.collectAsState()

  PersonalizeCardScreen(
      navController = navController, state = state.value, modifier = modifier, appState = appState)
}

@Composable
internal fun PersonalizeCardScreen(
    navController: NavController,
    state: PersonalizeCardScreenState,
    modifier: Modifier = Modifier,
    appState: AppState
) {
  ScanScreen(
      navController = navController,
      modifier = modifier,
      appState = appState,
      onBack = { navController.navigate(Home) { popUpTo(Home) { inclusive = true } } }) {
        when (state) {
          PersonalizeCardScreenState.WaitForCard -> {
            PresentCardAnimation()
          }
          PersonalizeCardScreenState.WritingToCard -> {
            ScanCardAnimation()
          }
          is PersonalizeCardScreenState.DisplayError -> {
            ReadingError(state.message)
          }
          PersonalizeCardScreenState.DisplaySuccess -> {
            Text(
                text = "Card is now personalized",
                modifier = Modifier.widthIn(max = 200.dp).padding(top = 16.dp),
                color = blue,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
          }
        }
      }
}
