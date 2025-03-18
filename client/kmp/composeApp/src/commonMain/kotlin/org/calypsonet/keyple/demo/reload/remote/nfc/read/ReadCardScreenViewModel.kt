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
package org.calypsonet.keyple.demo.reload.remote.nfc.read

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.calypsonet.keyple.demo.reload.remote.KeypleService
import org.eclipse.keyple.keypleless.distributed.client.protocol.KeypleResult

sealed class ReadCardScreenState {
  data object WaitForCard : ReadCardScreenState()

  data object ReadingCard : ReadCardScreenState()

  data object ShowCardContent : ReadCardScreenState()

  data class DisplayError(val message: String) : ReadCardScreenState()
}

class ReadCardScreenViewModel(
    private val keypleService: KeypleService,
) : ViewModel() {

  private var _state = MutableStateFlow<ReadCardScreenState>(ReadCardScreenState.WaitForCard)
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
        keypleService.waitForCard {
          keypleService.updateReaderMessage("Stay still...")
          viewModelScope.launch(Dispatchers.IO) {
            readContracts()
          }
        }
        //_state.value = ReadCardScreenState.DisplayError("No card found")
      } catch (e: Exception) {
        _state.value = ReadCardScreenState.DisplayError("Error: ${e.message}")
      }
    }
  }

  private suspend fun readContracts() {
    _state.value = ReadCardScreenState.ReadingCard
    try {
      when (val result = keypleService.selectCardAndAnalyseContracts()) {
        is KeypleResult.Failure -> {
          _state.value = ReadCardScreenState.DisplayError(result.error.message)
        }
        is KeypleResult.Success -> {
          _state.value = ReadCardScreenState.ShowCardContent
        }
      }
    } catch (e: Exception) {
      Napier.e("Error reading card", e)
      _state.value = ReadCardScreenState.DisplayError(e.message ?: "Unknown error")
    }
  }
}
