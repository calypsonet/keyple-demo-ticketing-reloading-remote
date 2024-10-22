package org.calypsonet.keyple.demo.reload.remote.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import keypleremotemobile.composeapp.generated.resources.Res
import keypleremotemobile.composeapp.generated.resources.basket_title_multi_title
import keypleremotemobile.composeapp.generated.resources.contract_multi_subtitle
import keypleremotemobile.composeapp.generated.resources.contract_multi_title
import keypleremotemobile.composeapp.generated.resources.contract_pass_expired_title
import keypleremotemobile.composeapp.generated.resources.contract_pass_subtitle
import keypleremotemobile.composeapp.generated.resources.contract_pass_title
import org.calypsonet.keyple.demo.reload.remote.AppState
import org.calypsonet.keyple.demo.reload.remote.CardContracts
import org.calypsonet.keyple.demo.reload.remote.Contract
import org.calypsonet.keyple.demo.reload.remote.ui.blue
import org.calypsonet.keyple.demo.reload.remote.ui.grey
import org.calypsonet.keyple.demo.reload.remote.ui.lightBlue
import org.calypsonet.keyple.demo.reload.remote.nav.Home
import org.calypsonet.keyple.demo.reload.remote.nav.WriteCard
import org.calypsonet.keyple.demo.reload.remote.ui.KeypleTopAppBar
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

enum class TitleType {
    SINGLE, SEASON
}

data class Title(
    val type: TitleType, val price: Int, val quantity: Int = 1, val date: String? = null
)

@Composable
fun CardContentScreen(
    navController: NavController,
    appState: AppState,
    viewModel: CardContentScreenViewModel,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            KeypleTopAppBar(
                navController = navController,
                appState = appState,
                onBack = {
                    when (state.value) {
                        is CardContentScreenState.DisplayContent -> navController.navigate(Home)
                        is CardContentScreenState.ChooseTitle -> viewModel.displayContent()
                        is CardContentScreenState.DisplayBasket -> viewModel.chooseTitle()
                    }
                }
            )
        },
        modifier = modifier,
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = state.value.screenTitle,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            when (state.value) {
                is CardContentScreenState.DisplayContent -> {
                    CardContent(
                        contracts = (state.value as CardContentScreenState.DisplayContent).contracts,
                        modifier = modifier,
                        chooseTitle = { viewModel.chooseTitle() }
                    )
                }

                is CardContentScreenState.ChooseTitle -> {
                    TitleList(
                        titles = (state.value as CardContentScreenState.ChooseTitle).titles,
                        addToBasket = viewModel::addToBasket
                    )
                }

                is CardContentScreenState.DisplayBasket -> {
                    Basket(
                        title = (state.value as CardContentScreenState.DisplayBasket).selectedTitle!!,
                        onPay = {
                            navController.navigate(WriteCard(nbTickets = it.quantity))
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun ColumnScope.CardContent(
    contracts: CardContracts,
    chooseTitle: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        items(contracts.contracts) { contract ->
            Card(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightBlue
                ),
            ) {
                Column {
                    Text(
                        text = getContractTitle(contract),
                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                        color = blue,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = getContractSubtitle(contract),
                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                        color = blue,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.weight(1f))

    Button(
        onClick = { chooseTitle() },
        modifier = Modifier.sizeIn(maxWidth = 400.dp, minHeight = 100.dp).padding(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = blue),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            "BUY TITLE",
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
    }
}

@Composable
fun getContractTitle(contract: Contract): String {
    return when (contract.tarif) {
        "MULTI_TRIP" ->  stringResource(Res.string.contract_multi_title)
        "EXPIRED" ->  stringResource(Res.string.contract_pass_expired_title)
        else -> stringResource(Res.string.contract_pass_title)
    }
}

@Composable
fun getContractSubtitle(contract: Contract): String {
    return when (contract.tarif) {
        "MULTI_TRIP" ->  stringResource(Res.string.contract_multi_subtitle, contract.counterValue)
        else -> stringResource(Res.string.contract_pass_subtitle, contract.saleDate, contract.validityEndDate)
    }
}

@Composable
internal fun ColumnScope.Basket(title: Title, onPay: (title: Title) -> Unit) {
    TitleCard(title = title, modifier = Modifier.padding(vertical = 48.dp), onTitleClick = {})

    Box(
        modifier = Modifier.fillMaxWidth().weight(1f).padding(4.dp).background(lightBlue),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CreditCardDetails()

            Button(
                onClick = { onPay(title) },
                modifier = Modifier.sizeIn(maxWidth = 400.dp, minHeight = 100.dp)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = blue),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "PAY",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                )
            }
        }
    }
}

@Composable
internal fun CreditCardDetails() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Credit Card Details",
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )

        Card(
            modifier = Modifier.sizeIn(maxWidth = 400.dp, minHeight = 100.dp)
                .padding(bottom = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Card Number",
                    color = grey
                )
                Text(
                    text = "1111.1111.1111.1111",
                    color = blue,
                )
            }
        }

        Card(
            modifier = Modifier.sizeIn(maxWidth = 400.dp, minHeight = 100.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Expiry",
                )
                Text(
                    text = "10/24",
                    color = blue,
                )
                Text(
                    text = "CVC",
                )
                Text(
                    text = "123",
                    color = blue,
                )
            }
        }
    }
}

@Composable
internal fun TitleList(
    titles: List<Title>, modifier: Modifier = Modifier, addToBasket: (Title) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items(
            titles
        ) { title ->
            TitleCard(title = title, onTitleClick = addToBasket)
        }
    }
}

@Composable
internal fun TitleCard(
    title: Title, modifier: Modifier = Modifier, onTitleClick: (title: Title) -> Unit
) {
    Card(
        modifier = modifier.padding(16.dp).sizeIn(maxWidth = 300.dp, minHeight = 100.dp)
            .clickable {
                onTitleClick(title)
            },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = lightBlue
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = pluralStringResource(Res.plurals.basket_title_multi_title, title.quantity, title.quantity),
                color = blue,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "${title.price},00 €", color = blue,
                textAlign = TextAlign.Center,
            )
        }
    }
}