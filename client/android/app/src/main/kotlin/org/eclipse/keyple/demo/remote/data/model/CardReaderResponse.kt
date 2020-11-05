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
package org.eclipse.keyple.demo.remote.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardReaderResponse(
    val status: Status,
    val cardType: String,
    val ticketsNumber: Int,
    val titlesList: ArrayList<CardTitle>,
    val lastValidationsList: ArrayList<Validation>,
    val seasonPassExpiryDate: String
): Parcelable