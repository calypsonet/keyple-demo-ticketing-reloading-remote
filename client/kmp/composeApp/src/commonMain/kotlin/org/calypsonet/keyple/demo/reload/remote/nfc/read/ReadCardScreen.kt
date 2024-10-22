package org.calypsonet.keyple.demo.reload.remote.nfc.read

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.calypsonet.keyple.demo.reload.remote.nav.Card
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.PresentCardAnimation
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.ReadingError
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.ScanCardAnimation
import org.calypsonet.keyple.demo.reload.remote.nfc.ui.ScanScreen

@Composable
fun ReadCardScreen(
    navController: NavController,
    viewModel: ReadCardScreenViewModel,
    modifier: Modifier = Modifier,
    appState: AppState
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(state.value) {
        if (state.value is ReadCardScreenState.ShowCardContent) {
            navController.navigate(Card)
        }
    }

    ReadCardScreen(
        navController = navController,
        state = state.value,
        modifier = modifier,
        appState = appState
    )
}

@Composable
internal fun ReadCardScreen(
    navController: NavController,
    state: ReadCardScreenState,
    modifier: Modifier = Modifier,
    appState: AppState
) {
    ScanScreen(
        navController = navController,
        modifier = modifier,
        appState = appState
    ) {
        when (state) {
            ReadCardScreenState.WaitForCard -> {
                PresentCardAnimation()
            }

            ReadCardScreenState.ReadingCard -> {
                ScanCardAnimation()
            }

            is ReadCardScreenState.DisplayError -> {
                ReadingError(state.message)
            }

            ReadCardScreenState.ShowCardContent -> {
                //no-op
            }
        }
    }
}