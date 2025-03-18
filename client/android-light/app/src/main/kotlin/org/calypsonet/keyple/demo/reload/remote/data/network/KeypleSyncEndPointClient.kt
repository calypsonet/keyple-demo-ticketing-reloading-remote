/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT License which is available at
 * https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.data.network

import io.reactivex.Single
import org.eclipse.keyple.distributed.MessageDto
import org.eclipse.keyple.distributed.spi.SyncEndpointClientSpi

/** We have to wrap the retrofit client */
class KeypleSyncEndPointClient(private val restClient: RestClient) : SyncEndpointClientSpi {

  override fun sendRequest(msg: MessageDto?): MutableList<MessageDto> {
    return restClient.sendRequest(msg).blockingGet()
  }

  fun ping(): Single<String> {
    return restClient.ping()
  }
}
