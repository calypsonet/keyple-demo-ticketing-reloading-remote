/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.ui

import android.content.Intent
import android.os.Bundle
import org.calypsonet.keyple.demo.reload.remote.BuildConfig
import org.calypsonet.keyple.demo.reload.remote.R
import org.calypsonet.keyple.demo.reload.remote.databinding.ActivitySettingsMenuBinding

class SettingsMenuActivity : AbstractDemoActivity() {
  private lateinit var activitySettingsMenuBinding: ActivitySettingsMenuBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activitySettingsMenuBinding = ActivitySettingsMenuBinding.inflate(layoutInflater)
    toolbarBinding = activitySettingsMenuBinding.appBarLayout
    setContentView(activitySettingsMenuBinding.root)

    activitySettingsMenuBinding.serverBtn.setOnClickListener {
      val intent = Intent(this, ServerSettingsActivity::class.java)
      startActivity(intent)
    }

    activitySettingsMenuBinding.configurationBtn.setOnClickListener {
      val intent = Intent(this, ConfigurationSettingsActivity::class.java)
      startActivity(intent)
    }

    activitySettingsMenuBinding.personalizationBtn.setOnClickListener {
      val intent = Intent(this, HomeActivity::class.java)
      intent.putExtra(HomeActivity.CHOOSE_DEVICE_FOR_PERSO, true)
      startActivity(intent)
    }

    activitySettingsMenuBinding.versionName.text =
        getString(R.string.version, BuildConfig.VERSION_NAME)
  }
}
