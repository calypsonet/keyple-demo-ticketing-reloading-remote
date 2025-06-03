/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.domain

import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject
import kotlin.jvm.Throws
import org.calypsonet.keyple.card.storagecard.StorageCardExtensionService
import org.calypsonet.keyple.demo.reload.remote.data.ReaderRepository
import org.calypsonet.keyple.demo.reload.remote.di.scopes.AppScoped
import org.eclipse.keyple.card.calypso.CalypsoExtensionService
import org.eclipse.keyple.core.service.SmartCardServiceProvider
import org.eclipse.keypop.reader.selection.spi.SmartCard
import org.eclipse.keypop.storagecard.card.ProductType.MIFARE_ULTRALIGHT
import org.eclipse.keypop.storagecard.card.ProductType.ST25_SRT512

@AppScoped
class TicketingService @Inject constructor(private var readerRepository: ReaderRepository) {

  companion object {
    const val ST25_SRT512_LOGICAL_PROTOCOL = "ST25_SRT512"
    const val MIFARE_ULTRALIGHT_LOGICAL_PROTOCOL = "MIFARE_ULTRALIGHT"
  }

  /** Select card and retrieve the active card */
  @Throws(IllegalStateException::class, Exception::class)
  fun getSmartCard(readerName: String, aidEnums: ArrayList<ByteArray>): SmartCard {
    with(ReaderRepository.getReader(readerName)) {
      if (isCardPresent) {
        val smartCardService = SmartCardServiceProvider.getService()

        val readerApiFactory = smartCardService.readerApiFactory

        val reader = ReaderRepository.getReader(readerName)

        /** Get the Calypso card extension service */
        val calypsoExtension = CalypsoExtensionService.getInstance()

        /** Get the Storage card extension service */
        val storageCardExtension = StorageCardExtensionService.getInstance()

        /** Verify that the extension's API level is consistent with the current service. */
        smartCardService.checkCardExtension(calypsoExtension)

        val cardSelectionManager = readerApiFactory.createCardSelectionManager()

        aidEnums.forEach {
          /**
           * Generic selection: configures a CardSelector with all the desired attributes to make
           * the selection and read additional information afterwards
           */
          val calypsoCardSelector = readerApiFactory.createIsoCardSelector().filterByDfName(it)
          cardSelectionManager.prepareSelection(
              calypsoCardSelector,
              calypsoExtension.calypsoCardApiFactory.createCalypsoCardSelectionExtension())
        }
        cardSelectionManager.prepareSelection(
            readerApiFactory
                .createBasicCardSelector()
                .filterByCardProtocol(MIFARE_ULTRALIGHT_LOGICAL_PROTOCOL),
            storageCardExtension.createStorageCardSelectionExtension(MIFARE_ULTRALIGHT))
        cardSelectionManager.prepareSelection(
            readerApiFactory
                .createBasicCardSelector()
                .filterByCardProtocol(ST25_SRT512_LOGICAL_PROTOCOL),
            storageCardExtension.createStorageCardSelectionExtension(ST25_SRT512))

        val selectionResult = cardSelectionManager.processCardSelectionScenario(reader)
        val smartCard = selectionResult.activeSmartCard
        if (smartCard != null) {
          // TODO move this code to the calling method
          //          val calypsoCard = selectionResult.activeSmartCard as CalypsoCard
          //          // check is the DF name is the expected one (Req. TL-SEL-AIDMATCH.1)
          //          if (!CardConstant.aidMatch(
          //              aidEnums[selectionResult.activeSelectionIndex], calypsoCard.dfName)) {
          //            throw IllegalStateException("Unexpected DF name")
          //          }
          return smartCard
        } else {
          throw IllegalStateException("Matching smartcard not found")
        }
      } else {
        throw Exception("Card is not present")
      }
    }
  }
}
