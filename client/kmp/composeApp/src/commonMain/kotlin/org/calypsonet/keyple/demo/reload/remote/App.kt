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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.calypsonet.keyple.demo.reload.remote.card.CardContentScreen
import org.calypsonet.keyple.demo.reload.remote.card.CardContentScreenViewModel
import org.calypsonet.keyple.demo.reload.remote.nav.AppError
import org.calypsonet.keyple.demo.reload.remote.nav.AppSuccess
import org.calypsonet.keyple.demo.reload.remote.nav.Card
import org.calypsonet.keyple.demo.reload.remote.nav.Home
import org.calypsonet.keyple.demo.reload.remote.nav.PersonalizeCard
import org.calypsonet.keyple.demo.reload.remote.nav.ReadCard
import org.calypsonet.keyple.demo.reload.remote.nav.ServerConfig
import org.calypsonet.keyple.demo.reload.remote.nav.Settings
import org.calypsonet.keyple.demo.reload.remote.nav.WriteTitleCard
import org.calypsonet.keyple.demo.reload.remote.nfc.personalize.PersonalizeCardScreen
import org.calypsonet.keyple.demo.reload.remote.nfc.personalize.PersonalizeCardScreenViewModel
import org.calypsonet.keyple.demo.reload.remote.nfc.read.ReadCardScreen
import org.calypsonet.keyple.demo.reload.remote.nfc.read.ReadCardScreenViewModel
import org.calypsonet.keyple.demo.reload.remote.nfc.write.WriteCardScreen
import org.calypsonet.keyple.demo.reload.remote.nfc.write.WriteCardScreenViewModel
import org.calypsonet.keyple.demo.reload.remote.settings.ServerConfigScreen
import org.calypsonet.keyple.demo.reload.remote.settings.ServerConfigScreenViewModel
import org.calypsonet.keyple.demo.reload.remote.settings.SettingsScreen
import org.calypsonet.keyple.demo.reload.remote.ui.ErrorScreen
import org.calypsonet.keyple.demo.reload.remote.ui.HomeScreen
import org.calypsonet.keyple.demo.reload.remote.ui.KeypleTheme
import org.calypsonet.keyple.demo.reload.remote.ui.SuccessScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(service: KeypleService, cardRepository: CardRepository) {
  val vm = viewModel<AppViewModel> { AppViewModel(keypleService = service) }
  val serverConfigVm =
      viewModel<ServerConfigScreenViewModel> {
        ServerConfigScreenViewModel(keypleService = service)
      }

  val navController = rememberNavController()

  val state = vm.state.collectAsState()

  KeypleTheme {
    NavHost(navController = navController, startDestination = Home) {
      composable<Home> { HomeScreen(navController = navController, appState = state.value) }

      composable<Settings> { SettingsScreen(navController = navController, appState = state.value) }

      composable<ReadCard> {
        ReadCardScreen(
            navController = navController,
            viewModel =
                viewModel<ReadCardScreenViewModel> {
                  ReadCardScreenViewModel(
                      keypleService = service,
                  )
                },
            appState = state.value)
      }

      composable<WriteTitleCard> { backStackEntry ->
        val route = backStackEntry.toRoute<WriteTitleCard>()

        WriteCardScreen(
            navController = navController,
            viewModel =
                viewModel<WriteCardScreenViewModel> {
                  WriteCardScreenViewModel(keypleService = service, title = route)
                },
            appState = state.value)
      }

      composable<PersonalizeCard> {
        PersonalizeCardScreen(
            navController = navController,
            viewModel =
                viewModel<PersonalizeCardScreenViewModel> {
                  PersonalizeCardScreenViewModel(
                      keypleService = service,
                  )
                },
            appState = state.value)
      }

      composable<Card> {
        CardContentScreen(
            navController = navController,
            appState = state.value,
            viewModel =
                viewModel<CardContentScreenViewModel> {
                  CardContentScreenViewModel(cardRepository = cardRepository)
                })
      }

      composable<AppError> { ErrorScreen(navController = navController, appState = state.value) }

      composable<AppSuccess> {
        SuccessScreen(navController = navController, appState = state.value)
      }

      composable<ServerConfig> {
        ServerConfigScreen(
            navController = navController, viewModel = serverConfigVm, appState = state.value)
      }
    }
  }
}
