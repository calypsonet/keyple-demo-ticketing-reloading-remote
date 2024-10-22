package org.calypsonet.keyple.demo.reload.remote.nfc.personalize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.calypso.keyple.remote.protocol.KeypleResult
import org.calypsonet.keyple.demo.reload.remote.KeypleService

sealed class PersonalizeCardScreenState {
    data object WaitForCard : PersonalizeCardScreenState()
    data object WritingToCard : PersonalizeCardScreenState()
    data object DisplaySuccess : PersonalizeCardScreenState()
    data class DisplayError(val message: String) : PersonalizeCardScreenState()
}

class PersonalizeCardScreenViewModel(
    private val keypleService: KeypleService,
) : ViewModel() {
    private var _state = MutableStateFlow<PersonalizeCardScreenState>(PersonalizeCardScreenState.WaitForCard)
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
                    personalizeCard()
                } else {
                    _state.value = PersonalizeCardScreenState.DisplayError("No card found")
                }
            } catch (e: Exception) {
                _state.value = PersonalizeCardScreenState.DisplayError("Error: ${e.message}")
            }
        }
    }

    private suspend fun personalizeCard() {
        _state.value = PersonalizeCardScreenState.WritingToCard
        try {
            when (val result = keypleService.personalizeCard()) {
                is KeypleResult.Failure -> {
                    _state.value = PersonalizeCardScreenState.DisplayError(result.error.message)
                }

                is KeypleResult.Success -> {
                    _state.value = PersonalizeCardScreenState.DisplaySuccess
                }
            }
        } catch (e: Exception) {
            Napier.e("Error personalizing card", e)
            _state.value = PersonalizeCardScreenState.DisplayError(e.message ?: "Unknown error")
        }
    }
}