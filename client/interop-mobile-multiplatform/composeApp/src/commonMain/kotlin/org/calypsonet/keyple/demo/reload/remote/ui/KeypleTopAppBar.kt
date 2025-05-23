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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import keypleinteropremotedemo.composeapp.generated.resources.Res
import keypleinteropremotedemo.composeapp.generated.resources.ic_logo_keyple
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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
  CenterAlignedTopAppBar(
      title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
          PulsingDot(state = appState, modifier = Modifier.padding(12.dp).size(10.dp))

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

@Composable
fun PulsingDot(state: AppState, modifier: Modifier = Modifier) {
  val dotScale = remember { Animatable(1f) }
  val waves = remember { mutableStateListOf<Wave>() }

  val dotColor = if (state.serverOnline) Color.Green else Color.Red

  LaunchedEffect(state.serverOnline) {
    if (state.serverOnline) {
      while (isActive) {
        // Animate the dot scale
        dotScale.animateTo(
            targetValue = 0.8f, animationSpec = tween(durationMillis = 200, easing = LinearEasing))
        dotScale.animateTo(
            targetValue = 1f, animationSpec = tween(durationMillis = 200, easing = LinearEasing))

        // Create the waves
        waves.add(Wave())

        delay(5000)
      }
    } else {
      // reset the animation to avoid residual effect
      dotScale.snapTo(1f)
      waves.clear()
    }
  }

  // Manage the waves and their animations
  LaunchedEffect(waves.size) {
    if (waves.isNotEmpty()) {
      waves.first().startAnimation()
      // Remove the wave after the animation.
      delay(Wave.ANIMATION_DURATION)
      waves.removeFirst()
    }
  }

  // Drawing the elements
  Box(modifier = modifier.size(48.dp), contentAlignment = Alignment.Center) {

    // Draw the waves
    Canvas(
        modifier =
            Modifier.size(48.dp).drawBehind {
              waves.forEach { wave ->
                drawCircle(
                    color = Color.Green,
                    radius = wave.radius.value,
                    center = Offset(size.width / 2, size.height / 2),
                    alpha = wave.alpha.value,
                    style = Stroke(width = 4.dp.toPx()))
              }
            }) {}

    // Draw the central dot
    Box(
        modifier =
            Modifier.size(24.dp).scale(dotScale.value).clip(CircleShape).background(dotColor))
  }
}

class Wave {
  val radius = Animatable(0f)
  val alpha = Animatable(0.2f)

  suspend fun startAnimation() {
    radius.animateTo(
        targetValue = 30.dp.value,
        animationSpec =
            tween(durationMillis = ANIMATION_DURATION.toInt(), easing = FastOutSlowInEasing))
    alpha.animateTo(
        targetValue = 0f,
        animationSpec = tween(durationMillis = ANIMATION_DURATION.toInt(), easing = LinearEasing))
  }

  companion object {
    const val ANIMATION_DURATION = 200L
  }
}
