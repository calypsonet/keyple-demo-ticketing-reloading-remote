/* ******************************************************************************
 * Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.server.card;

import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.keyple.core.service.ObservablePlugin;
import org.eclipse.keyple.core.service.Plugin;
import org.eclipse.keyple.core.service.PluginEvent;
import org.eclipse.keyple.core.service.SmartCardServiceProvider;
import org.eclipse.keyple.core.service.spi.PluginObservationExceptionHandlerSpi;
import org.eclipse.keyple.core.service.spi.PluginObserverSpi;
import org.eclipse.keypop.reader.CardReader;
import org.eclipse.keypop.reader.CardReaderEvent;
import org.eclipse.keypop.reader.ObservableCardReader;
import org.eclipse.keypop.reader.spi.CardReaderObservationExceptionHandlerSpi;
import org.eclipse.keypop.reader.spi.CardReaderObserverSpi;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CardSamObserver
    implements CardReaderObserverSpi,
        CardReaderObservationExceptionHandlerSpi,
        PluginObserverSpi,
        PluginObservationExceptionHandlerSpi {
  private static final Logger logger = LoggerFactory.getLogger(CardSamObserver.class);
  private ObservablePlugin plugin;
  private ObservableCardReader reader;
  private boolean isSamAvailable = true; // The SAM must be inserted by default

  @ConfigProperty(name = "sam.pcsc.reader.filter")
  String samReaderFilter;

  @Inject CardConfigurator cardConfigurator;

  @Override
  public void onReaderObservationError(String contextInfo, String readerName, Throwable e) {
    logger.error(
        "An error occurred while observing the SAM reader {}:{}", contextInfo, readerName, e);
  }

  @Override
  public void onReaderEvent(CardReaderEvent readerEvent) {
    logger.info("Reader event received: {}", readerEvent.getType());
    isSamAvailable =
        readerEvent.getType() == CardReaderEvent.Type.CARD_INSERTED
            || readerEvent.getType() == CardReaderEvent.Type.CARD_MATCHED;
    logger.debug("SAM availability set to {}", isSamAvailable);
  }

  public boolean isSamAvailable() {
    logger.debug("Checking SAM availability: {}", isSamAvailable);
    return isSamAvailable;
  }

  @Override
  public void onPluginObservationError(String pluginName, Throwable e) {
    logger.error("An error occurred while observing the SAM plugin {}", pluginName, e);
  }

  @Override
  public void onPluginEvent(PluginEvent pluginEvent) {
    logger.info("Plugin event received {} {}", pluginEvent.getType(), pluginEvent.getPluginName());
    restartMonitoring();
  }

  void startMonitoring() {
    logger.info("Start monitoring SAM reader");
    isSamAvailable = false;

    initSamPluginAndReader();

    plugin.setPluginObservationExceptionHandler(this);
    plugin.addObserver(this);
    reader.setReaderObservationExceptionHandler(this);
    reader.addObserver(this);

    logger.info("Starting card detection on reader: {}", reader.getName());
    reader.startCardDetection(ObservableCardReader.DetectionMode.REPEATING);
  }

  private void restartMonitoring() {
    logger.info("Restart monitoring SAM reader");
    isSamAvailable = false;

    logger.debug("Removing existing reader observer.");
    reader.removeObserver(this);
    reader.stopCardDetection();

    initSamPluginAndReader();

    if (reader == null) {
      logger.warn("SAM plugin or reader not found. Monitoring not started.");
      return;
    }

    reader.setReaderObservationExceptionHandler(this);
    reader.addObserver(this);

    logger.info("Starting card detection on reader: {}", reader.getName());
    reader.startCardDetection(ObservableCardReader.DetectionMode.REPEATING);
  }

  private void initSamPluginAndReader() {
    logger.info("Initializing SAM plugin and reader using filter: {}", samReaderFilter);
    Pattern p = Pattern.compile(samReaderFilter);
    for (Plugin plugin : SmartCardServiceProvider.getService().getPlugins()) {
      logger.debug("Checking plugin: {}", plugin.getName());
      for (CardReader reader : plugin.getReaders()) {
        logger.debug("Checking reader: {}", reader.getName());
        if (p.matcher(reader.getName()).matches()) {
          logger.info("Matching reader found: {}", reader.getName());
          this.plugin = (ObservablePlugin) plugin;
          this.reader = (ObservableCardReader) reader;
          return;
        }
      }
    }

    logger.warn("No matching SAM reader found.");
  }
}
