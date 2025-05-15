/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.calypsonet.keyple.demo.reload.remote.CardRepository
import org.calypsonet.keyple.demo.reload.remote.ContractInfo

sealed class CardContentScreenState(val screenTitle: String) {
  data class DisplayContent(val contracts: List<ContractInfo> = emptyList()) :
      CardContentScreenState("Content")

  data class ChooseTitle(val titles: List<Title> = emptyList()) :
      CardContentScreenState("Choose a title")

  data class DisplayBasket(val selectedTitle: Title?) : CardContentScreenState("Basket")
}

class CardContentScreenViewModel(private val cardRepository: CardRepository) : ViewModel() {

  private var _state =
      MutableStateFlow<CardContentScreenState>(
          CardContentScreenState.DisplayContent(cardRepository.getCardContracts()))

  val state =
      _state.stateIn(
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000),
          initialValue = CardContentScreenState.DisplayContent(cardRepository.getCardContracts()))

  fun displayContent() {
    val contracts = cardRepository.getCardContracts()
    _state.value = CardContentScreenState.DisplayContent(contracts)
  }

  fun chooseTitle() {
    _state.value =
        CardContentScreenState.ChooseTitle(
            listOf(
                Title(type = TitleType.SINGLE, price = 1, quantity = 1),
                Title(type = TitleType.SINGLE, price = 2, quantity = 2),
                Title(type = TitleType.SINGLE, price = 3, quantity = 3),
                Title(type = TitleType.SINGLE, price = 4, quantity = 4),
                Title(type = TitleType.SEASON, price = 20, quantity = 1, date = "12/12/24"),
            ))
  }

  fun addToBasket(title: Title) {
    _state.value = CardContentScreenState.DisplayBasket(title)
  }

  fun getCardSerial(): String {
    return cardRepository.getCardSerial()
  }
}
