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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import keypleinteropremotedemo.composeapp.generated.resources.Res
import keypleinteropremotedemo.composeapp.generated.resources.ic_contactless_card
import keypleinteropremotedemo.composeapp.generated.resources.ic_logo_calypso
import keypleinteropremotedemo.composeapp.generated.resources.ic_settings
import keypleinteropremotedemo.composeapp.generated.resources.keyple_background
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.calypsonet.keyple.demo.reload.remote.nav.ReadCard
import org.calypsonet.keyple.demo.reload.remote.nav.Settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier, appState: AppState) {
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
          Modifier.align(Alignment.Center),
          horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        KeypleTopAppBar(
            navController = navController,
            appState = appState,
            showBackArrow = false,
            actions = {
              IconButton(onClick = { navController.navigate(route = Settings) }) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_settings),
                    contentDescription = "Go to settings action",
                    tint = Color.White)
              }
            })

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

          HomeCard(
              iconRes = Res.drawable.ic_contactless_card,
              title = "Contactless support",
              onClick = { navController.navigate(route = ReadCard) })

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

@Composable
internal fun HomeCard(
    modifier: Modifier = Modifier,
    iconRes: DrawableResource,
    title: String,
    onClick: () -> Unit
) {
  Card(
      modifier = modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { onClick() },
      elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
  ) {
    Column(
        modifier = Modifier.padding(8.dp).padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Image(
          imageVector = vectorResource(iconRes),
          modifier = Modifier.size(64.dp),
          contentDescription = "icon for card $title",
      )
      Text(
          text = title,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
          color = darkBlue)
    }
  }
}
