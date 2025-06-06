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
import android.view.View
import java.lang.Exception
import java.lang.IllegalStateException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.calypsonet.keyple.demo.common.constant.RemoteServiceId
import org.calypsonet.keyple.demo.common.dto.AnalyzeContractsInputDto
import org.calypsonet.keyple.demo.common.dto.AnalyzeContractsOutputDto
import org.calypsonet.keyple.demo.common.dto.WriteContractInputDto
import org.calypsonet.keyple.demo.common.dto.WriteContractOutputDto
import org.calypsonet.keyple.demo.common.model.type.PriorityCode
import org.calypsonet.keyple.demo.reload.remote.R
import org.calypsonet.keyple.demo.reload.remote.data.model.AppSettings
import org.calypsonet.keyple.demo.reload.remote.data.model.CardReaderResponse
import org.calypsonet.keyple.demo.reload.remote.data.model.DeviceEnum
import org.calypsonet.keyple.demo.reload.remote.data.model.Status
import org.calypsonet.keyple.demo.reload.remote.databinding.ActivityCardReaderBinding
import org.calypsonet.keyple.demo.reload.remote.di.scopes.ActivityScoped
import org.calypsonet.keyple.demo.reload.remote.domain.TicketingService
import org.eclipse.keyple.core.util.HexUtil
import org.eclipse.keypop.calypso.card.card.CalypsoCard
import org.eclipse.keypop.reader.CardReaderEvent
import org.eclipse.keypop.storagecard.card.StorageCard
import timber.log.Timber

@ActivityScoped
class ReloadActivity : AbstractCardActivity() {

  @Inject lateinit var ticketingService: TicketingService
  private lateinit var activityCardReaderBinding: ActivityCardReaderBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityCardReaderBinding = ActivityCardReaderBinding.inflate(layoutInflater)
    toolbarBinding = activityCardReaderBinding.appBarLayout
    setContentView(activityCardReaderBinding.root)
  }

  override fun initReaders() {
    try {
      if (DeviceEnum.getDeviceEnum(prefData.loadDeviceType()!!) == DeviceEnum.CONTACTLESS_CARD) {
        showPresentNfcCardInstructions()
        initAndActivateCardReader()
      } else {
        showNowLoadingInformation()
        initOmapiReader {
          GlobalScope.launch {
            remoteServiceExecution(
                selectedDeviceReaderName, pluginType, AppSettings.aidEnums, protocol = null)
          }
        }
      }
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  override fun onPause() {
    activityCardReaderBinding.cardAnimation.cancelAnimation()
    activityCardReaderBinding.loadingAnimation.cancelAnimation()
    try {
      if (DeviceEnum.getDeviceEnum(prefData.loadDeviceType()!!) == DeviceEnum.CONTACTLESS_CARD) {
        deactivateAndClearCardReader()
      }
    } catch (e: Exception) {
      Timber.e(e)
    }
    super.onPause()
  }

  override fun onReaderEvent(event: CardReaderEvent?) {
    if (event?.type == CardReaderEvent.Type.CARD_INSERTED) {
      runOnUiThread { showNowLoadingInformation() }
      GlobalScope.launch {
        remoteServiceExecution(
            selectedDeviceReaderName,
            pluginType,
            AppSettings.aidEnums,
            "ISO_14443_4_LOGICAL_PROTOCOL")
      }
    }
  }

  private suspend fun remoteServiceExecution(
      selectedDeviceReaderName: String,
      pluginType: String,
      aidEnums: ArrayList<ByteArray>,
      protocol: String?
  ) {
    withContext(Dispatchers.IO) {
      try {
        val readCardUniqueIdentifier = intent.getStringExtra(CARD_APPLICATION_NUMBER)
        val smartCard = ticketingService.getSmartCard(selectedDeviceReaderName, aidEnums)
        when (smartCard) {
          is CalypsoCard -> {
            if (HexUtil.toHex(smartCard!!.applicationSerialNumber) != readCardUniqueIdentifier) {
              // Ticket would have been bought for the Card read at step one.
              // To avoid swapping we check thant loading is done on the same card
              throw IllegalStateException("Not the same card")
            }
          }
          is StorageCard -> {
            if (HexUtil.toHex(smartCard!!.uid) != readCardUniqueIdentifier) {
              // Ticket would have been bought for the Card read at step one.
              // To avoid swapping we check thant loading is done on the same card
              throw IllegalStateException("Not the same card")
            }
          }
        }

        val analyseContractsInput = AnalyzeContractsInputDto(pluginType)
        // un-mock for run
        localServiceClient.executeRemoteService(
            RemoteServiceId.READ_CARD_AND_ANALYZE_CONTRACTS.name,
            selectedDeviceReaderName,
            smartCard,
            analyseContractsInput,
            AnalyzeContractsOutputDto::class.java)

        val contractTariff =
            PriorityCode.findEnumByKey(
                intent.getIntExtra(SelectTicketsActivity.SELECTED_TICKET_PRIORITY_CODE, 0))
        val ticketToBeLoaded = intent.getIntExtra(SelectTicketsActivity.TICKETS_NUMBER, 0)

        val writeContractInputDto =
            WriteContractInputDto(contractTariff, ticketToBeLoaded, pluginType)

        val writeTitleOutput =
            localServiceClient.executeRemoteService(
                RemoteServiceId.READ_CARD_AND_WRITE_CONTRACT.name,
                selectedDeviceReaderName,
                smartCard,
                writeContractInputDto,
                WriteContractOutputDto::class.java)

        when (writeTitleOutput.statusCode) {
          0 -> {
            runOnUiThread {
              changeDisplay(
                  CardReaderResponse(
                      Status.SUCCESS, "", ticketToBeLoaded, arrayListOf(), arrayListOf(), ""),
                  finishActivity = true)
            }
          }
          1 -> {
            launchServerErrorResponse()
          } // server not ready,
          2 -> {
            when (smartCard) {
              is CalypsoCard -> {
                launchInvalidCardResponse(
                    String.format(
                        getString(R.string.card_invalid_structure),
                        HexUtil.toHex(smartCard!!.applicationSubtype)))
              }
              is StorageCard -> {
                launchInvalidCardResponse(getString(R.string.storage_card_invalid))
              }
              else -> {}
            } // card rejected
          }
        }
      } catch (e: IllegalStateException) {
        Timber.e(e)
        launchInvalidCardResponse(e.message!!)
      } catch (e: Exception) {
        Timber.e(e)
        launchExceptionResponse(e)
      }
    }
  }

  override fun changeDisplay(
      cardReaderResponse: CardReaderResponse,
      applicationSerialNumber: String?,
      finishActivity: Boolean?
  ) {
    activityCardReaderBinding.loadingAnimation.cancelAnimation()
    activityCardReaderBinding.cardAnimation.cancelAnimation()
    val intent = Intent(this, ReloadResultActivity::class.java)
    intent.putExtra(ReloadResultActivity.TICKETS_NUMBER, 0)
    intent.putExtra(ReloadResultActivity.STATUS, cardReaderResponse.status.toString())
    intent.putExtra(ReloadResultActivity.MESSAGE, cardReaderResponse.errorMessage)
    startActivity(intent)
    if (finishActivity == true) {
      finish()
    }
  }

  private fun showPresentNfcCardInstructions() {
    activityCardReaderBinding.presentTxt.text = getString(R.string.present_card)
    activityCardReaderBinding.cardAnimation.visibility = View.VISIBLE
    activityCardReaderBinding.cardAnimation.playAnimation()
    activityCardReaderBinding.loadingAnimation.cancelAnimation()
    activityCardReaderBinding.loadingAnimation.visibility = View.INVISIBLE
  }

  private fun showNowLoadingInformation() {
    activityCardReaderBinding.presentTxt.text = getString(R.string.loading_in_progress)
    activityCardReaderBinding.loadingAnimation.visibility = View.VISIBLE
    activityCardReaderBinding.loadingAnimation.playAnimation()
    activityCardReaderBinding.cardAnimation.cancelAnimation()
    activityCardReaderBinding.cardAnimation.visibility = View.INVISIBLE
  }
}
