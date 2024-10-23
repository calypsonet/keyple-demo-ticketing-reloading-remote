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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import keyplelessremotedemo.composeapp.generated.resources.Res
import keyplelessremotedemo.composeapp.generated.resources.ic_logo_keyple
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeypleTopAppBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    appState: AppState,
    actions: @Composable RowScope.() -> Unit = {},
    showBackArrow: Boolean = true,
    onBack: () -> Unit = { navController.popBackStack() },
) {
  val serverStateColor =
      if (appState.serverOnline) {
        Color.Green
      } else {
        Color.Red
      }

  CenterAlignedTopAppBar(
      title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
              modifier =
                  Modifier.padding(12.dp)
                      .size(10.dp)
                      .clip(CircleShape)
                      .background(serverStateColor))

          Image(
              imageVector = vectorResource(Res.drawable.ic_logo_keyple),
              contentDescription = "Keyple logo",
              modifier = Modifier.size(100.dp),
          )
        }
      },
      navigationIcon = {
        if (showBackArrow) {
          IconButton(onClick = { onBack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
            )
          }
        }
      },
      actions = actions,
      colors =
          TopAppBarDefaults.topAppBarColors(
              containerColor = Color.Transparent,
          ))
}
