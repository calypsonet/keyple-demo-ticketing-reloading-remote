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
import java.text.SimpleDateFormat
import java.util.Calendar
import org.calypsonet.keyple.demo.common.model.type.PriorityCode
import org.calypsonet.keyple.demo.reload.remote.R
import org.calypsonet.keyple.demo.reload.remote.databinding.ActivityCheckoutBinding

class CheckoutActivity : AbstractDemoActivity() {

  private lateinit var activityCheckoutBinding: ActivityCheckoutBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityCheckoutBinding = ActivityCheckoutBinding.inflate(layoutInflater)
    toolbarBinding = activityCheckoutBinding.appBarLayout
    setContentView(activityCheckoutBinding.root)

    val selectedTicketPriorityCode =
        PriorityCode.findEnumByKey(
            intent.getIntExtra(
                SelectTicketsActivity.SELECTED_TICKET_PRIORITY_CODE, PriorityCode.MULTI_TRIP.key))
    val ticketNumberCount: Int = intent.getIntExtra(SelectTicketsActivity.TICKETS_NUMBER, 0)

    if (selectedTicketPriorityCode == PriorityCode.SEASON_PASS) {
      activityCheckoutBinding.selectionLabel.text = getString(R.string.season_pass_title)
      activityCheckoutBinding.selectionPrice.text = getString(R.string.ticket_price, 20)
    } else {
      activityCheckoutBinding.selectionLabel.text =
          resources.getQuantityString(R.plurals.x_tickets, ticketNumberCount, ticketNumberCount)
      activityCheckoutBinding.selectionPrice.text =
          getString(R.string.ticket_price, ticketNumberCount)
    }
    activityCheckoutBinding.validateBtn.setOnClickListener {
      val intent = Intent(this, PaymentValidatedActivity::class.java)
      intent.putExtras(getIntent())
      startActivity(intent)
      this@CheckoutActivity.finish()
    }

    val now = Calendar.getInstance().time
    val sdf = SimpleDateFormat("MM/yy")
    activityCheckoutBinding.expiryValue.text = sdf.format(now)
  }
}
