/* **************************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import java.lang.Exception
import java.lang.IllegalStateException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.calypsonet.keyple.demo.common.constant.RemoteServiceId
import org.calypsonet.keyple.demo.common.dto.SelectAppAndLoadContractInputDto
import org.calypsonet.keyple.demo.common.dto.SelectAppAndLoadContractOutputDto
import org.calypsonet.keyple.demo.common.model.type.PriorityCode
import org.calypsonet.keyple.demo.reload.remote.R
import org.calypsonet.keyple.demo.reload.remote.data.model.CardReaderResponse
import org.calypsonet.keyple.demo.reload.remote.data.model.DeviceEnum
import org.calypsonet.keyple.demo.reload.remote.data.model.Status
import org.calypsonet.keyple.demo.reload.remote.databinding.ActivityCardReaderBinding
import org.calypsonet.keyple.demo.reload.remote.di.scopes.ActivityScoped
import org.eclipse.keypop.reader.CardReaderEvent
import timber.log.Timber

@ActivityScoped
class ReloadActivity : AbstractCardActivity() {

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
        initAndActivateAndroidKeypleNfcReader()
      } else {
        showNowLoadingInformation()
        initOmapiReader {
          GlobalScope.launch { remoteServiceExecution(selectedDeviceReaderName, pluginType) }
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
        deactivateAndClearAndroidKeypleNfcReader()
      }
    } catch (e: Exception) {
      Timber.e(e)
    }
    super.onPause()
  }

  override fun onReaderEvent(event: CardReaderEvent?) {
    if (event?.type == CardReaderEvent.Type.CARD_INSERTED) {
      runOnUiThread { showNowLoadingInformation() }
      GlobalScope.launch { remoteServiceExecution(selectedDeviceReaderName, pluginType) }
    }
  }

  private suspend fun remoteServiceExecution(selectedDeviceReaderName: String, pluginType: String) {
    withContext(Dispatchers.IO) {
      try {
        val readCardSerialNumber = intent.getStringExtra(CARD_APPLICATION_NUMBER)
        val contractTariff =
            PriorityCode.findEnumByKey(
                intent.getIntExtra(SelectTicketsActivity.SELECTED_TICKET_PRIORITY_CODE, 0))
        val ticketToBeLoaded = intent.getIntExtra(SelectTicketsActivity.TICKETS_NUMBER, 0)

        val selectAppAndLoadContractInputDto =
            SelectAppAndLoadContractInputDto(
                readCardSerialNumber!!, contractTariff, ticketToBeLoaded, pluginType)

        val selectAppAndLoadContractOutputDto =
            localServiceClient.executeRemoteService(
                RemoteServiceId.SELECT_APP_AND_LOAD_CONTRACT.name,
                selectedDeviceReaderName,
                null,
                selectAppAndLoadContractInputDto,
                SelectAppAndLoadContractOutputDto::class.java)

        when (selectAppAndLoadContractOutputDto.statusCode) {
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
            launchInvalidCardResponse(selectAppAndLoadContractOutputDto.message)
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
