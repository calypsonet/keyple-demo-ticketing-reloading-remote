package org.calypsonet.keyple.demo.reload.remote.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import keypleremotemobile.composeapp.generated.resources.Res
import keypleremotemobile.composeapp.generated.resources.ic_arrow_right
import keypleremotemobile.composeapp.generated.resources.ic_server
import keypleremotemobile.composeapp.generated.resources.ic_sim_card
import keypleremotemobile.composeapp.generated.resources.ic_tools
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.calypsonet.keyple.demo.reload.remote.ui.darkBlue
import org.calypsonet.keyple.demo.reload.remote.nav.PersonalizeCard
import org.calypsonet.keyple.demo.reload.remote.nav.ServerConfig
import org.calypsonet.keyple.demo.reload.remote.ui.KeypleTopAppBar
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    appState: AppState
) {
    Scaffold(
        topBar = {
            KeypleTopAppBar(navController = navController, appState = appState)
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Settings",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )

            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 64.dp),
                text = "version 1.0.0",
                textAlign = TextAlign.Center,
            )

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp
            )

            SettingRow(
                title = "Server",
                iconRes = Res.drawable.ic_server,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate(route = ServerConfig)
                }
            )

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp
            )

            SettingRow(
                title = "Configuration",
                iconRes = Res.drawable.ic_tools,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    //TODO
                    //navController.navigate("serverConfig")
                }
            )

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp
            )

            SettingRow(
                title = "Personalization",
                iconRes = Res.drawable.ic_sim_card,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate(PersonalizeCard)
                }
            )

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp
            )
        }
    }
}

@Composable
internal fun SettingRow(
    title: String,
    iconRes: DrawableResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = modifier.padding(horizontal = 32.dp).padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                imageVector = vectorResource(iconRes),
                modifier = Modifier.size(24.dp),
                contentDescription = "icon for card $title",
            )
            Text(
                text = title,
                modifier = Modifier.padding(start = 16.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = darkBlue
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                imageVector = vectorResource(Res.drawable.ic_arrow_right),
                contentDescription = "arrow right",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}