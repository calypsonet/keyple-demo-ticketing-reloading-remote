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
import android.nfc.NfcManager
import android.os.Bundle
import android.view.View
import java.lang.IllegalStateException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.calypsonet.keyple.demo.common.constant.RemoteServiceId
import org.calypsonet.keyple.demo.common.dto.SelectAppAndAnalyzeContractsInputDto
import org.calypsonet.keyple.demo.common.dto.SelectAppAndAnalyzeContractsOutputDto
import org.calypsonet.keyple.demo.reload.remote.R
import org.calypsonet.keyple.demo.reload.remote.data.model.*
import org.calypsonet.keyple.demo.reload.remote.databinding.ActivityCardReaderBinding
import org.calypsonet.keyple.demo.reload.remote.di.scopes.ActivityScoped
import org.calypsonet.keyple.demo.reload.remote.ui.cardsummary.CardSummaryActivity
import org.eclipse.keyple.core.service.KeyplePluginException
import org.eclipse.keypop.reader.CardReaderEvent
import org.eclipse.keypop.reader.ReaderCommunicationException
import timber.log.Timber

@ActivityScoped
class CardReaderActivity : AbstractCardActivity() {

  private lateinit var activityCardReaderBinding: ActivityCardReaderBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityCardReaderBinding = ActivityCardReaderBinding.inflate(layoutInflater)
    toolbarBinding = activityCardReaderBinding.appBarLayout
    setContentView(activityCardReaderBinding.root)
  }

  override fun initReaders() {
    try {
      when (device) {
        DeviceEnum.CONTACTLESS_CARD -> {
          val nfcManager = getSystemService(NFC_SERVICE) as NfcManager
          if (nfcManager.defaultAdapter?.isEnabled == true) {
            showPresentNfcCardInstructions()
            initAndActivateAndroidKeypleNfcReader()
          } else {
            launchExceptionResponse(
                IllegalStateException("NFC not activated"), finishActivity = true)
          }
        }
        DeviceEnum.SIM -> {
          showNowLoadingInformation()
          initOmapiReader {
            GlobalScope.launch { remoteServiceExecution(selectedDeviceReaderName, pluginType) }
          }
        }
        DeviceEnum.WEARABLE -> {
          throw KeyplePluginException("Wearable")
        }
        DeviceEnum.EMBEDDED -> {
          throw KeyplePluginException("Embedded")
        }
      }
    } catch (e: ReaderCommunicationException) {
      Timber.e(e)
      launchExceptionResponse(e, true)
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  override fun onPause() {
    activityCardReaderBinding.cardAnimation.cancelAnimation()
    activityCardReaderBinding.loadingAnimation.cancelAnimation()
    try {
      if (DeviceEnum.getDeviceEnum(prefData.loadDeviceType()!!) == DeviceEnum.CONTACTLESS_CARD) {
        deactivateAndClearAndroidKeypleNfcReader()
      } else {
        deactivateAndClearOmapiReader()
      }
    } catch (e: Exception) {
      Timber.e(e)
    }
    super.onPause()
  }

  override fun onReaderEvent(event: CardReaderEvent?) {
    if (event?.type == CardReaderEvent.Type.CARD_INSERTED) {
      // We'll select Card when SmartCard is presented in field
      // Method handlePo is described below
      runOnUiThread { showNowLoadingInformation() }
      GlobalScope.launch { remoteServiceExecution(selectedDeviceReaderName, pluginType) }
    }
  }

  private suspend fun remoteServiceExecution(selectedDeviceReaderName: String, pluginType: String) {
    withContext(Dispatchers.IO) {
      try {
        val selectAppAndAnalyzeContractsInputDto = SelectAppAndAnalyzeContractsInputDto(pluginType)

        val selectAppAndAnalyzeContractsOutputDto =
            localServiceClient.executeRemoteService(
                RemoteServiceId.SELECT_APP_AND_ANALYZE_CONTRACTS.name,
                selectedDeviceReaderName,
                null,
                selectAppAndAnalyzeContractsInputDto,
                SelectAppAndAnalyzeContractsOutputDto::class.java)

        when (selectAppAndAnalyzeContractsOutputDto.statusCode) {
          0 -> {
            runOnUiThread {
              val contracts = selectAppAndAnalyzeContractsOutputDto.validContracts
              val status = if (contracts.isNotEmpty()) Status.TICKETS_FOUND else Status.EMPTY_CARD
              val finishActivity =
                  device !=
                      DeviceEnum
                          .CONTACTLESS_CARD // Only with NFC we can come back to 'wait for device
              // screen'

              changeDisplay(
                  CardReaderResponse(
                      status,
                      "",
                      selectAppAndAnalyzeContractsOutputDto.validContracts.size,
                      buildCardTitles(selectAppAndAnalyzeContractsOutputDto.validContracts),
                      arrayListOf(),
                      ""),
                  selectAppAndAnalyzeContractsOutputDto.applicationSerialNumber,
                  finishActivity)
            }
          } // success,
          1 -> {
            launchServerErrorResponse()
          } // server not ready,
          in 2..4 -> {
            launchInvalidCardResponse(selectAppAndAnalyzeContractsOutputDto.message)
          } // card rejected, not personalized, expired environment
        }
      } catch (e: IllegalStateException) {
        Timber.e(e)
        launchInvalidCardResponse(e.message!!)
      } catch (e: Exception) {
        Timber.e(e)
        val finishActivity =
            device !=
                DeviceEnum
                    .CONTACTLESS_CARD // Only with NFC we can come back to 'wait for device screen'
        launchExceptionResponse(
            IllegalStateException("Server error:\n" + e.message), finishActivity)
      }
    }
  }

  private fun buildCardTitles(
      contractInfos: List<SelectAppAndAnalyzeContractsOutputDto.ContractInfo>?
  ): List<CardTitle> {
    val cardTitles = contractInfos?.map { CardTitle(it.title, it.description, it.isValid) }
    return cardTitles ?: arrayListOf()
  }

  override fun changeDisplay(
      cardReaderResponse: CardReaderResponse,
      applicationSerialNumber: String?,
      finishActivity: Boolean?
  ) {
    activityCardReaderBinding.loadingAnimation.cancelAnimation()
    activityCardReaderBinding.cardAnimation.cancelAnimation()
    val intent = Intent(this, CardSummaryActivity::class.java)
    intent.putExtra(CARD_CONTENT, cardReaderResponse)
    intent.putExtra(CARD_APPLICATION_NUMBER, applicationSerialNumber)
    startActivity(intent)
    if (finishActivity == true) {
      finish()
    }
  }

  private fun showPresentNfcCardInstructions() {
    activityCardReaderBinding.presentTxt.text = getString(R.string.present_travel_card_label)
    activityCardReaderBinding.cardAnimation.visibility = View.VISIBLE
    activityCardReaderBinding.cardAnimation.playAnimation()
    activityCardReaderBinding.loadingAnimation.cancelAnimation()
    activityCardReaderBinding.loadingAnimation.visibility = View.INVISIBLE
  }

  private fun showNowLoadingInformation() {
    activityCardReaderBinding.presentTxt.text = getString(R.string.read_in_progress)
    activityCardReaderBinding.loadingAnimation.visibility = View.VISIBLE
    activityCardReaderBinding.loadingAnimation.playAnimation()
    activityCardReaderBinding.cardAnimation.cancelAnimation()
    activityCardReaderBinding.cardAnimation.visibility = View.INVISIBLE
  }
}
