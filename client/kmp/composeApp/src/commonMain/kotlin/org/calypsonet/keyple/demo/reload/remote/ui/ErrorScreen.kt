package org.calypsonet.keyple.demo.reload.remote.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import keypleremotemobile.composeapp.generated.resources.Res
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.jetbrains.compose.resources.ExperimentalResourceApi

internal enum class Error {
    FATAL,
    WARNING,
    INFO
}

internal data class ErrorDetails(
    val animationFileName: String,
    val message: String,
    val error: String
)

@Composable
fun ErrorScreen(navController: NavController, modifier: Modifier = Modifier, appState: AppState) {

    val error = Error.WARNING

    Scaffold(
        topBar = {
            KeypleTopAppBar(navController = navController, appState = appState)
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (error) {
                Error.FATAL -> DisplayError(
                    ErrorDetails(
                        "anim_error.json",
                        "Fatal Error",
                        "An error occurred, please try again later"
                    )
                )

                Error.WARNING -> DisplayError(
                    ErrorDetails(
                        "anim_warning.json",
                        "Not valid on this network",
                        "An error occurred, please try again later"
                    )
                )

                Error.INFO -> DisplayError(
                    ErrorDetails(
                        "anim_error_white.json",
                        "Fatal Error",
                        "An error occurred, please try again later"
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun DisplayError(
    errorDetails: ErrorDetails,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/${errorDetails.animationFileName}").decodeToString()
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
        contentDescription = "error animation",
    )

    Text(
        text = errorDetails.message,
        modifier = Modifier.widthIn(max = 200.dp).padding(top = 16.dp),
        color = blue,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
    )

    Text(
        text = errorDetails.error,
        modifier = Modifier.widthIn(max = 200.dp).padding(top = 16.dp),
        color = blue,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
    )
}