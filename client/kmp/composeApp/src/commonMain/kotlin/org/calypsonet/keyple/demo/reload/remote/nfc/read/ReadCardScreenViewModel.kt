package org.calypsonet.keyple.demo.reload.remote.nfc.read

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.calypso.keyple.remote.protocol.KeypleResult
import org.calypsonet.keyple.demo.reload.remote.KeypleService

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
                val cardFound = keypleService.waitCard()
                if (cardFound) {
                    keypleService.updateReaderMessage("Stay still...")
                    readContracts()
                } else {
                    _state.value = ReadCardScreenState.DisplayError("No card found")
                }
            } catch (e: Exception) {
                _state.value = ReadCardScreenState.DisplayError("Error: ${e.message}")
            }
        }
    }

    private suspend fun readContracts() {
//        _state.value = ReadCardScreenState.ShowCardContent
//        return

        _state.value = ReadCardScreenState.ReadingCard
        try {
            when (val result = keypleService.selectCardAndReadContracts()) {
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