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
import org.calypsonet.keyple.demo.common.dto.CardIssuanceInputDto
import org.calypsonet.keyple.demo.common.dto.CardIssuanceOutputDto
import org.calypsonet.keyple.demo.reload.remote.R
import org.calypsonet.keyple.demo.reload.remote.data.model.AppSettings
import org.calypsonet.keyple.demo.reload.remote.data.model.CardReaderResponse
import org.calypsonet.keyple.demo.reload.remote.data.model.DeviceEnum
import org.calypsonet.keyple.demo.reload.remote.data.model.Status
import org.calypsonet.keyple.demo.reload.remote.databinding.ActivityPersonalizationBinding
import org.calypsonet.keyple.demo.reload.remote.di.scopes.ActivityScoped
import org.calypsonet.keyple.demo.reload.remote.domain.TicketingService
import org.eclipse.keyple.core.util.HexUtil
import org.eclipse.keypop.reader.CardReaderEvent
import timber.log.Timber

@ActivityScoped
class PersonalizationActivity : AbstractCardActivity() {

  @Inject lateinit var ticketingService: TicketingService
  private lateinit var activityPersonalizationBinding: ActivityPersonalizationBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityPersonalizationBinding = ActivityPersonalizationBinding.inflate(layoutInflater)
    toolbarBinding = activityPersonalizationBinding.appBarLayout
    setContentView(activityPersonalizationBinding.root)
  }

  override fun initReaders() {
    try {
      if (DeviceEnum.getDeviceEnum(prefData.loadDeviceType()!!) == DeviceEnum.CONTACTLESS_CARD) {
        showPresentNfcCardInstructions()
        initAndActivateAndroidKeypleNfcReader()
      } else {
        showNowPersonalizingInformation()
        initOmapiReader {
          GlobalScope.launch {
            remoteServiceExecution(selectedDeviceReaderName, AppSettings.aidEnums, null)
          }
        }
      }
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  override fun onPause() {
    activityPersonalizationBinding.cardAnimation.cancelAnimation()
    activityPersonalizationBinding.loadingAnimation.cancelAnimation()
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

  private fun showPresentNfcCardInstructions() {
    activityPersonalizationBinding.presentTxt.text =
        getString(R.string.present_card_personalization)
    activityPersonalizationBinding.cardAnimation.visibility = View.VISIBLE
    activityPersonalizationBinding.cardAnimation.playAnimation()
    activityPersonalizationBinding.loadingAnimation.cancelAnimation()
    activityPersonalizationBinding.loadingAnimation.visibility = View.INVISIBLE
  }

  private fun showNowPersonalizingInformation() {
    activityPersonalizationBinding.presentTxt.text = getString(R.string.personalization_in_progress)
    activityPersonalizationBinding.loadingAnimation.visibility = View.VISIBLE
    activityPersonalizationBinding.loadingAnimation.playAnimation()
    activityPersonalizationBinding.cardAnimation.cancelAnimation()
    activityPersonalizationBinding.cardAnimation.visibility = View.INVISIBLE
  }

  override fun changeDisplay(
      cardReaderResponse: CardReaderResponse,
      applicationSerialNumber: String?,
      finishActivity: Boolean?
  ) {
    val intent = Intent(this, ReloadResultActivity::class.java)
    intent.putExtra(ReloadResultActivity.IS_PERSONALIZATION_RESULT, true)
    intent.putExtra(ReloadResultActivity.STATUS, cardReaderResponse.status.name)
    intent.putExtra(ReloadResultActivity.MESSAGE, cardReaderResponse.errorMessage)
    startActivity(intent)
    if (finishActivity == true) {
      finish()
    }
  }

  override fun onReaderEvent(event: CardReaderEvent?) {
    if (event?.type == CardReaderEvent.Type.CARD_INSERTED) {
      runOnUiThread { showNowPersonalizingInformation() }
      GlobalScope.launch {
        remoteServiceExecution(selectedDeviceReaderName, AppSettings.aidEnums, "ISO_14443_4")
      }
    }
  }

  private suspend fun remoteServiceExecution(
      selectedDeviceReaderName: String,
      aidEnums: ArrayList<ByteArray>,
      protocol: String?
  ) {
    withContext(Dispatchers.IO) {
      try {
        val calypsoCard =
            ticketingService.getCalypsoCard(selectedDeviceReaderName, aidEnums, protocol)
        val cardIssuanceInput = CardIssuanceInputDto(pluginType)
        val cardIssuanceOutput =
            localServiceClient.executeRemoteService(
                RemoteServiceId.PERSONALIZE_CARD.name,
                selectedDeviceReaderName,
                calypsoCard,
                cardIssuanceInput,
                CardIssuanceOutputDto::class.java)
        when (cardIssuanceOutput.statusCode) {
          0 -> {
            runOnUiThread {
              changeDisplay(
                  CardReaderResponse(Status.SUCCESS, "", 0, arrayListOf(), arrayListOf(), ""),
                  applicationSerialNumber = HexUtil.toHex(calypsoCard!!.applicationSerialNumber),
                  finishActivity = true)
            }
          } // success,
          1 -> {
            launchServerErrorResponse()
          } // server not ready,
          2 -> {
            launchInvalidCardResponse(
                String.format(
                    getString(R.string.card_invalid_structure),
                    HexUtil.toHex(calypsoCard!!.applicationSubtype)))
          } // card rejected
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
}
