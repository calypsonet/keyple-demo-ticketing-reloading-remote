/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT License which is available at
 * https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import org.calypsonet.keyple.demo.reload.remote.Application
import org.calypsonet.keyple.demo.reload.remote.di.scopes.AppScoped

@Suppress("unused")
@Module
class DataModule {

  @Provides
  @AppScoped
  fun getSharedPreferences(app: Application): SharedPreferences {
    return app.getSharedPreferences("Keyple-prefs", Context.MODE_PRIVATE)
  }
}
