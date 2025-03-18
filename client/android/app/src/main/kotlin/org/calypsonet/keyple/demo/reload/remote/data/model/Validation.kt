/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT License which is available at
 * https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.data.model

import android.os.Parcelable
import java.util.Date
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Validation(val name: String, val location: String, val date: Date) : Parcelable
