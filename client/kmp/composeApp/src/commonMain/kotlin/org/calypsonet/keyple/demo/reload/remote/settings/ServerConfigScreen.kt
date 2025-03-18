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
package org.calypsonet.keyple.demo.reload.remote.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.calypsonet.keyple.demo.reload.remote.ui.KeypleTopAppBar

@Composable
fun ServerConfigScreen(
    navController: NavController,
    viewModel: ServerConfigScreenViewModel,
    modifier: Modifier = Modifier,
    appState: AppState
) {
  val state = viewModel.state.collectAsState()

  ServerConfigScreen(
      state = state.value,
      restartServer = viewModel::restartServer,
      navController = navController,
      modifier = modifier,
      appState = appState,
      onHostChanged = viewModel::onHostChanged,
      onPortChanged = viewModel::onPortChanged,
      onProtocolChanged = viewModel::onProtocolChanged,
      onEndpointChanged = viewModel::onEndpointChanged)
}

@Composable
internal fun ServerConfigScreen(
    state: ServerConfigScreenState,
    restartServer: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    appState: AppState,
    onHostChanged: (String) -> Unit,
    onPortChanged: (Int) -> Unit,
    onProtocolChanged: (String) -> Unit,
    onEndpointChanged: (String) -> Unit
) {
  Scaffold(
      topBar = { KeypleTopAppBar(navController = navController, appState = appState) },
      modifier = modifier,
  ) { innerPadding ->
    Column(
        Modifier.padding(innerPadding).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
          OutlinedTextField(
              value = state.serverHost,
              onValueChange = { onHostChanged(it) },
              label = { Text("Server IP") })

          OutlinedTextField(
              value = state.serverPort.toString(),
              onValueChange = { onPortChanged(it.trim().toInt()) },
              label = { Text("Server Port") })

          OutlinedTextField(
              value = state.protocol,
              onValueChange = { onProtocolChanged(it) },
              label = { Text("Protocol") })

          OutlinedTextField(
              value = state.endpoint,
              onValueChange = { onEndpointChanged(it) },
              label = { Text("Endpoint") })

          Spacer(modifier = Modifier.weight(1f))

          Button(onClick = { restartServer() }) { Text("Restart") }
        }
  }
}
