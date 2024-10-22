package org.calypsonet.keyple.demo.reload.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppState(
    val serverOnline: Boolean = false
)

class AppViewModel(private val keypleService: KeypleService) : ViewModel() {

    private var _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            keypleService.start()
        }

        viewModelScope.launch {
            keypleService.state.collect {
                _state.value = AppState(serverOnline = it.serverReachable)
            }
        }
    }
}