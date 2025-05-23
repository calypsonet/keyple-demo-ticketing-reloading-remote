/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import org.calypsonet.keyple.demo.reload.remote.Application
import org.calypsonet.keyple.demo.reload.remote.di.scopes.AppScoped

@AppScoped
@Component(
    modules =
        [
            AppModule::class,
            UIModule::class,
            AndroidSupportInjectionModule::class,
            DataModule::class,
            RestModule::class,
            ReaderModule::class])
interface AppComponent : AndroidInjector<Application?> {
  @Component.Builder
  interface Builder {
    @BindsInstance fun application(application: Application): Builder

    fun build(): AppComponent
  }
}
