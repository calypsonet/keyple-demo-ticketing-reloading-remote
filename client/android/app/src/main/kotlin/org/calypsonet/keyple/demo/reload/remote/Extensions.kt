/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT License which is available at
 * https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

const val KEYPLE_PREFS = "Keyple-prefs"

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
  val divider = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
  val drawable = ContextCompat.getDrawable(this.context, drawableRes)
  drawable?.let {
    divider.setDrawable(it)
    addItemDecoration(divider)
  }
}
