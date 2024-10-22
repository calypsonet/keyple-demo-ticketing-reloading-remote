package org.calypsonet.keyple.demo.reload.remote.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.Url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.calypsonet.keyple.demo.reload.remote.KeypleService

data class ServerConfigScreenState(
    val serverHost: String = "",
    val serverPort: Int = 0,
    val protocol: String = "http",
    val endpoint: String = ""
)

class ServerConfigScreenViewModel(private val keypleService: KeypleService) : ViewModel() {
    private var _state = MutableStateFlow(ServerConfigScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            keypleService.observeServerConfig().collect {
                val hostUrl = Url(it.host)
                _state.value = ServerConfigScreenState(
                    serverHost = hostUrl.host,
                    serverPort = it.port,
                    endpoint = it.endpoint,
                    protocol = hostUrl.protocol.name
                )
            }
        }
    }

    fun restartServer() {
        viewModelScope.launch {
            keypleService.updateServerConfig(
                host = state.value.serverHost,
                port = state.value.serverPort,
                protocol = state.value.protocol,
                endpoint = state.value.endpoint,
            )
            keypleService.start()
        }
    }

    fun onHostChanged(host: String) {
        _state.value = state.value.copy(serverHost = host)
    }

    fun onPortChanged(port: Int) {
        _state.value = state.value.copy(serverPort = port)
    }

    fun onProtocolChanged(protocol: String) {
        _state.value = state.value.copy(protocol = protocol)
    }

    fun onEndpointChanged(endpoint: String) {
        _state.value = state.value.copy(endpoint = endpoint)
    }
}