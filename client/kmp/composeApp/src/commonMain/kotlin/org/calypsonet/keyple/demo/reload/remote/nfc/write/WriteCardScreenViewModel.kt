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
package org.calypsonet.keyple.demo.reload.remote.nfc.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.calypsonet.keyple.demo.reload.remote.KeypleService
import org.calypsonet.keyple.demo.reload.remote.card.TitleType
import org.calypsonet.keyple.demo.reload.remote.nav.WriteTitleCard
import org.eclipse.keyple.distributed.protocol.KeypleResult

sealed class WriteCardScreenState {
  data object WaitForCard : WriteCardScreenState()

  data object WritingToCard : WriteCardScreenState()

  data object ShowTransactionSuccess : WriteCardScreenState()

  data class DisplayError(val message: String) : WriteCardScreenState()
}

class WriteCardScreenViewModel(
  private val keypleService: KeypleService,
  private val title: WriteTitleCard,
) : ViewModel() {
  private var _state = MutableStateFlow<WriteCardScreenState>(WriteCardScreenState.WaitForCard)
  val state = _state.asStateFlow()

  init {
    scan()
  }

  override fun onCleared() {
    super.onCleared()
    keypleService.releaseReader()
  }

  private fun scan() {
    viewModelScope.launch {
      keypleService.updateReaderMessage("Place your card on the top of the iPhone")
      try {
        val cardFound = keypleService.waitCard()
        if (cardFound) {
          keypleService.updateReaderMessage("Stay still...")
          writeTitle(title)
        } else {
          _state.value = WriteCardScreenState.DisplayError("No card found")
        }
      } catch (e: Exception) {
        _state.value = WriteCardScreenState.DisplayError("Error: ${e.message}")
      }
    }
  }

  private suspend fun writeTitle(title: WriteTitleCard) {
    _state.value = WriteCardScreenState.WritingToCard

    try {
      val result = if (title.type == TitleType.SEASON.ordinal) writePassTitle() else writeMultiTripTitle(title.quantity)
      when (result) {
        is KeypleResult.Failure -> {
          _state.value = WriteCardScreenState.DisplayError(result.error.message)
        }
        is KeypleResult.Success -> {
          _state.value = WriteCardScreenState.ShowTransactionSuccess
        }
      }
    } catch (e: Exception) {
      Napier.e("Error writing to card", e)
      _state.value = WriteCardScreenState.DisplayError(e.message ?: "Unknown error")
    }
  }

  private suspend fun writePassTitle() : KeypleResult<String>  {
    return keypleService.selectCardAndWriteContract(ticketNumber = 1, code = PriorityCode.SEASON_PASS)
  }

  private suspend fun writeMultiTripTitle(nbUnits: Int) : KeypleResult<String> {
    return keypleService.selectCardAndWriteContract(ticketNumber = nbUnits, code = PriorityCode.MULTI_TRIP)
  }
}
