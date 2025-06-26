/* ******************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.ui.cardsummary

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.calypsonet.keyple.demo.reload.remote.R
import org.calypsonet.keyple.demo.reload.remote.data.model.CardReaderResponse
import org.calypsonet.keyple.demo.reload.remote.data.model.Status
import org.calypsonet.keyple.demo.reload.remote.databinding.ActivityCardSummaryBinding
import org.calypsonet.keyple.demo.reload.remote.ui.AbstractCardActivity
import org.calypsonet.keyple.demo.reload.remote.ui.AbstractDemoActivity
import org.calypsonet.keyple.demo.reload.remote.ui.SelectTicketsActivity

class CardSummaryActivity : AbstractDemoActivity() {

  private lateinit var titleLinearLayoutManager: LinearLayoutManager
  private lateinit var titlesAdapter: TitlesRecyclerAdapter
  private lateinit var activityCardSummaryBinding: ActivityCardSummaryBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityCardSummaryBinding = ActivityCardSummaryBinding.inflate(layoutInflater)
    toolbarBinding = activityCardSummaryBinding.appBarLayout
    setContentView(activityCardSummaryBinding.root)

    val cardContent: CardReaderResponse =
        intent.getParcelableExtra(AbstractCardActivity.CARD_CONTENT)!!

    titleLinearLayoutManager = LinearLayoutManager(this)
    activityCardSummaryBinding.titlesList.layoutManager = titleLinearLayoutManager

    titlesAdapter = TitlesRecyclerAdapter(cardContent.titlesList)
    activityCardSummaryBinding.titlesList.adapter = titlesAdapter

    when (cardContent.status) {
      Status.INVALID_CARD -> {
        activityCardSummaryBinding.animation.setAnimation("error_orange_anim.json")
        activityCardSummaryBinding.animation.playAnimation()
        activityCardSummaryBinding.bigText.setText(R.string.card_invalid_label)
        activityCardSummaryBinding.bigText.setTextColor(resources.getColor(R.color.orange))
        activityCardSummaryBinding.smallDesc.text = cardContent.errorMessage
        activityCardSummaryBinding.smallDesc.setTextColor(resources.getColor(R.color.orange))
        activityCardSummaryBinding.buyBtn.visibility = View.INVISIBLE
        activityCardSummaryBinding.titlesList.visibility = View.GONE
        activityCardSummaryBinding.lastValidationContent.visibility = View.GONE
        activityCardSummaryBinding.contentTitle.visibility = View.GONE
      }
      Status.TICKETS_FOUND,
      Status.SUCCESS -> {
        activityCardSummaryBinding.titlesList.visibility = View.VISIBLE
        activityCardSummaryBinding.animation.visibility = View.GONE
        activityCardSummaryBinding.bigText.visibility = View.GONE
        activityCardSummaryBinding.smallDesc.visibility = View.INVISIBLE
        activityCardSummaryBinding.buyBtn.visibility = View.VISIBLE
        activityCardSummaryBinding.lastValidationContent.visibility = View.VISIBLE
        activityCardSummaryBinding.contentTitle.visibility = View.VISIBLE
      }
      Status.EMPTY_CARD -> {
        activityCardSummaryBinding.animation.setAnimation("error_anim.json")
        activityCardSummaryBinding.animation.playAnimation()
        activityCardSummaryBinding.bigText.text = getString(R.string.no_valid_label)
        activityCardSummaryBinding.bigText.setTextColor(resources.getColor(R.color.red))
        activityCardSummaryBinding.smallDesc.visibility = View.VISIBLE
        activityCardSummaryBinding.smallDesc.setTextColor(resources.getColor(R.color.red))
        activityCardSummaryBinding.smallDesc.text = getString(R.string.no_valid_desc)
        activityCardSummaryBinding.buyBtn.visibility = View.VISIBLE
        activityCardSummaryBinding.titlesList.visibility = View.GONE
        activityCardSummaryBinding.lastValidationContent.visibility = View.VISIBLE
        activityCardSummaryBinding.contentTitle.visibility = View.GONE
      }
      Status.ERROR -> {
        activityCardSummaryBinding.animation.setAnimation("error_anim.json")
        activityCardSummaryBinding.animation.playAnimation()
        if (cardContent.errorMessage != null)
            activityCardSummaryBinding.bigText.text = cardContent.errorMessage
        else activityCardSummaryBinding.bigText.setText(R.string.error_label)
        activityCardSummaryBinding.bigText.setTextColor(resources.getColor(R.color.red))
        activityCardSummaryBinding.smallDesc.visibility = View.INVISIBLE
        activityCardSummaryBinding.buyBtn.visibility = View.INVISIBLE
        activityCardSummaryBinding.titlesList.visibility = View.GONE
        activityCardSummaryBinding.lastValidationContent.visibility = View.GONE
        activityCardSummaryBinding.contentTitle.visibility = View.GONE
      }
      else -> {
        activityCardSummaryBinding.animation.setAnimation("error_anim.json")
        activityCardSummaryBinding.animation.playAnimation()
        activityCardSummaryBinding.bigText.setText(R.string.error_label)
        activityCardSummaryBinding.bigText.setTextColor(resources.getColor(R.color.red))
        activityCardSummaryBinding.smallDesc.visibility = View.INVISIBLE
        activityCardSummaryBinding.buyBtn.visibility = View.INVISIBLE
        activityCardSummaryBinding.titlesList.visibility = View.GONE
        activityCardSummaryBinding.lastValidationContent.visibility = View.GONE
        activityCardSummaryBinding.contentTitle.visibility = View.GONE
      }
    }

    if (!cardContent.cardType.isNullOrBlank()) {
      activityCardSummaryBinding.cardTypeLabel.visibility = View.VISIBLE
      activityCardSummaryBinding.cardTypeLabel.text = getString(R.string.card_type, cardContent.cardType)
    }

    activityCardSummaryBinding.animation.playAnimation()

    // Play sound
    val mp: MediaPlayer = MediaPlayer.create(this, R.raw.reading_sound)
    mp.start()
    activityCardSummaryBinding.buyBtn.setOnClickListener {
      val intent = Intent(this, SelectTicketsActivity::class.java)
      getIntent().getStringExtra(AbstractCardActivity.CARD_APPLICATION_NUMBER)?.let {
        intent.putExtra(AbstractCardActivity.CARD_APPLICATION_NUMBER, it)
      }
      startActivity(intent)
      this@CardSummaryActivity.finish()
    }
  }
}
