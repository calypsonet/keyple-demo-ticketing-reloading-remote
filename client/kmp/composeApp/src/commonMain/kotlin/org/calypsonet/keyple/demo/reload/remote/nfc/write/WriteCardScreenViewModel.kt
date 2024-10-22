package org.calypsonet.keyple.demo.reload.remote.nfc.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.calypso.keyple.remote.protocol.KeypleResult
import org.calypsonet.keyple.demo.reload.remote.KeypleService

sealed class WriteCardScreenState {
    data object WaitForCard : WriteCardScreenState()
    data object WritingToCard : WriteCardScreenState()
    data object ShowTransactionSuccess : WriteCardScreenState()
    data class DisplayError(val message: String) : WriteCardScreenState()
}

class WriteCardScreenViewModel(
    private val keypleService: KeypleService,
    private val nbTickets: Int,
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
                    writeTitle(nbTickets)
                } else {
                    _state.value = WriteCardScreenState.DisplayError("No card found")
                }
            } catch (e: Exception) {
                _state.value = WriteCardScreenState.DisplayError("Error: ${e.message}")
            }
        }
    }

    private suspend fun personalizeCard() {
        _state.value = WriteCardScreenState.WritingToCard
        try {
            when (val result = keypleService.personalizeCard()) {
                is KeypleResult.Failure -> {
                    _state.value = WriteCardScreenState.DisplayError(result.error.message)
                }

                is KeypleResult.Success -> {
                    //_state.value = WriteCardScreenState.ShowCardContent
                }
            }
        } catch (e: Exception) {
            Napier.e("Error personalizing card", e)
            _state.value = WriteCardScreenState.DisplayError(e.message ?: "Unknown error")
        }
    }

    private suspend fun writeTitle(nbUnits: Int) {
        _state.value = WriteCardScreenState.WritingToCard

        try {
            when (val result = keypleService.selectCardAndIncreaseContractCounter(nbUnits)) {
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
}