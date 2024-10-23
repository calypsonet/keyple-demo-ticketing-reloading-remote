package org.calypsonet.keyple.demo.reload.remote.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import keyplelessremotedemo.composeapp.generated.resources.Res
import keyplelessremotedemo.composeapp.generated.resources.success_title_loaded
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.calypsonet.keyple.demo.reload.remote.nav.Home
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SuccessScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    appState: AppState,
) {

    Scaffold(
        topBar = {
            KeypleTopAppBar(navController = navController, appState = appState, onBack = {
                navController.navigate(Home) }
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(green),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            DisplaySuccess(
                ErrorDetails(
                    "anim_tick_white.json",
                    stringResource(Res.string.success_title_loaded),
                    ""
                )
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun DisplaySuccess(
    details: ErrorDetails,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/${details.animationFileName}").decodeToString()
        )
    }
    val progress by animateLottieCompositionAsState(
        composition,
    )

    Image(
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        modifier = Modifier.size(200.dp),
        contentDescription = "animation",
    )

    Text(
        text = details.message,
        modifier = Modifier.padding(36.dp),
        color = white,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
    )
}