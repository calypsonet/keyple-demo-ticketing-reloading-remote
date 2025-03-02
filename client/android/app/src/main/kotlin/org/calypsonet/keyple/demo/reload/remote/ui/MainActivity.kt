/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT License which is available at
 * https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.ui

import android.content.Intent
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import java.util.Timer
import java.util.TimerTask
import org.calypsonet.keyple.demo.reload.remote.databinding.ActivityMainBinding

class MainActivity : DaggerAppCompatActivity() {

  private lateinit var activityMainBinding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    // Make sure this is before calling super.onCreate
    super.onCreate(savedInstanceState)
    activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(activityMainBinding.root)
    // Wait for Wizway Device to be connected
    Timer()
        .schedule(
            object : TimerTask() {
              override fun run() {
                if (!isFinishing) {
                  startActivity(Intent(applicationContext, HomeActivity::class.java))
                  finish()
                }
              }
            },
            SPLASH_MAX_DELAY_MS.toLong())
  }

  companion object {
    private const val SPLASH_MAX_DELAY_MS = 6000
  }
}
