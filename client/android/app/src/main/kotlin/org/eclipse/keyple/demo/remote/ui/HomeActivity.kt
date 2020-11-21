/*
 * Copyright (c) 2020 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.keyple.demo.remote.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_home.contactlessCardBtn
import kotlinx.android.synthetic.main.activity_home.embeddedElemBtn
import kotlinx.android.synthetic.main.activity_home.simCardBtn
import kotlinx.android.synthetic.main.activity_home.wearableBtn
import kotlinx.android.synthetic.main.toolbar.menuBtn
import org.eclipse.keyple.demo.remote.R
import org.eclipse.keyple.demo.remote.data.SharedPrefData
import org.eclipse.keyple.demo.remote.data.model.DeviceEnum

class HomeActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var prefData: SharedPrefData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        menuBtn.visibility = View.VISIBLE
        menuBtn.setOnClickListener {
            startActivity(Intent(this, SettingsMenuActivity::class.java))
        }
    }

    override fun onResume(){
        super.onResume()

        setupBtn(contactlessCardBtn, prefData.loadContactlessConfigurationVisibility(), DeviceEnum.CONTACTLESS_CARD)
        setupBtn(simCardBtn, prefData.loadSimConfigurationVisibility(), DeviceEnum.SIM)
        setupBtn(wearableBtn, prefData.loadWearableConfigurationVisibility(), DeviceEnum.WEARABLE)
        setupBtn(embeddedElemBtn, prefData.loadEmbeddedConfigurationVisibility(), DeviceEnum.EMBEDDED)
    }

    private fun setupBtn(btn: View, visibility: SharedPrefData.Companion.Visibility, type: DeviceEnum){
        btn.setOnClickListener {
            prefData.saveDeviceType(type.toString())
            startActivity(Intent(this, CardReaderActivity::class.java))
        }

        when (visibility) {
            SharedPrefData.Companion.Visibility.ENABLE -> {
                btn.visibility = View.VISIBLE
                btn.background =
                    ContextCompat.getDrawable(this, R.drawable.white_card)
                btn.isEnabled = true
            }
            SharedPrefData.Companion.Visibility.DISABLE -> {
                btn.visibility = View.VISIBLE
                btn.background =
                    ContextCompat.getDrawable(this, R.drawable.grey_card)
                btn.isEnabled = false
            }
            SharedPrefData.Companion.Visibility.HIDE -> {
                btn.visibility = View.GONE
            }
        }
    }
}
