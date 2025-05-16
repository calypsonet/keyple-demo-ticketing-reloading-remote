/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppState(val serverOnline: Boolean = false)

class AppViewModel(private val keypleService: KeypleService) : ViewModel() {

  private var _state = MutableStateFlow(AppState())
  val state = _state.asStateFlow()

  init {
    viewModelScope.launch { keypleService.start() }

    viewModelScope.launch {
      keypleService.state.collect { _state.value = AppState(serverOnline = it.serverReachable) }
    }
  }
}
