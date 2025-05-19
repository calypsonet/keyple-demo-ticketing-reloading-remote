/* ******************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote

class CardRepository {
  private var cardContracts: List<ContractInfo>? = null
  private var cardSerial = ""

  fun getCardSerial(): String {
    return cardSerial
  }

  fun saveCardSerial(serial: String) {
    cardSerial = serial
  }

  fun getCardContracts(): List<ContractInfo> {
    return cardContracts ?: emptyList()
  }

  fun saveCardContracts(contracts: List<ContractInfo>) {
    cardContracts = contracts
  }

  fun clear() {
    cardContracts = null
  }
}
