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
