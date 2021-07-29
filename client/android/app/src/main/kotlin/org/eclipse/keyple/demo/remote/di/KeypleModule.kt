/********************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package org.eclipse.keyple.demo.remote.di

import dagger.Module
import dagger.Provides
import org.eclipse.keyple.core.service.SmartCardServiceProvider
import org.eclipse.keyple.demo.remote.di.scopes.AppScoped
import org.eclipse.keyple.demo.remote.manager.KeypleManager
import org.eclipse.keyple.demo.remote.rest.KeypleSyncEndPointClient
import org.eclipse.keyple.distributed.LocalServiceClient
import org.eclipse.keyple.distributed.LocalServiceClientFactoryBuilder

@Module
class KeypleModule {

    @Provides
    @AppScoped
    public fun provideLocalServiceClient(keypleSyncEndPointClient: KeypleSyncEndPointClient): LocalServiceClient {
        if (!SmartCardServiceProvider.getService().isDistributedLocalServiceRegistered("localService")) {
            SmartCardServiceProvider.getService().registerDistributedLocalService(LocalServiceClientFactoryBuilder
                .builder("localService")
                .withSyncNode(keypleSyncEndPointClient)
                .build())
        }
        return SmartCardServiceProvider
            .getService()
            .getDistributedLocalService("localService")
            .getExtension(LocalServiceClient::class.java)
    }

    @Provides
    @AppScoped
    public fun provideKeypleServices(): KeypleManager {
        return KeypleManager
    }
}
