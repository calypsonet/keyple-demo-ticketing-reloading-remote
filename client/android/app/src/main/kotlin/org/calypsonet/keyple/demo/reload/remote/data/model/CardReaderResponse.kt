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
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardReaderResponse(
    val status: Status,
    val cardType: String,
    val ticketsNumber: Int,
    val titlesList: List<CardTitle>,
    val lastValidationsList: ArrayList<Validation>,
    val seasonPassExpiryDate: String,
    val errorMessage: String? = null
) : Parcelable
