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

  @ConfigProperty(name = "sam.pcsc.reader.filter")
  String samReaderFilter;

  @Inject CardConfigurator cardConfigurator;

  private ObservablePlugin plugin;
  private ObservableCardReader reader;
  private boolean isSamAvailable = true; // The SAM must be inserted by default

  @Override
  public void onPluginObservationError(String pluginName, Throwable e) {
    logger.error(
        "An error occurred while observing the SAM plugin {}: {}", pluginName, e.getMessage(), e);
  }

  @Override
  public void onReaderObservationError(String contextInfo, String readerName, Throwable e) {
    logger.error(
        "An error occurred while observing the SAM reader {}/{}: {}",
        contextInfo,
        readerName,
        e.getMessage(),
        e);
  }

  @Override
  public void onPluginEvent(PluginEvent pluginEvent) {
    logger.info("Plugin event received {} {}", pluginEvent.getType(), pluginEvent.getPluginName());
    restartReaderMonitoring();
  }

  @Override
  public void onReaderEvent(CardReaderEvent readerEvent) {
    logger.info("Reader event received: {}", readerEvent.getType());
    isSamAvailable =
        readerEvent.getType() == CardReaderEvent.Type.CARD_INSERTED
            || readerEvent.getType() == CardReaderEvent.Type.CARD_MATCHED;
    logger.debug("SAM availability set to {}", isSamAvailable);
  }

  boolean isSamAvailable() {
    logger.trace("Checking SAM availability: {}", isSamAvailable);
    return isSamAvailable;
  }

  void startMonitoring() {
    logger.info("Start SAM plugin and reader monitoring");
    isSamAvailable = false;
    reader = searchReader();
    if (reader == null) {
      throw new IllegalStateException("SAM reader not found");
    }
    logger.info(
        "Starting SAM plugin monitoring (only once at server startup): {}", plugin.getName());
    plugin.setPluginObservationExceptionHandler(this);
    plugin.addObserver(this);
    startReaderMonitoring();
  }

  private void restartReaderMonitoring() {
    logger.info("Restart SAM reader monitoring");
    isSamAvailable = false;
    if (reader != null) {
      logger.info("Stopping current SAM reader monitoring");
      reader.removeObserver(this);
      reader.stopCardDetection();
    }
    reader = searchReader(plugin);
    if (reader == null) {
      logger.warn("SAM reader not found. SAM reader monitoring not restarted");
      return;
    }
    startReaderMonitoring();
  }

  private void startReaderMonitoring() {
    logger.info("Starting SAM reader monitoring: {}", reader.getName());
    reader.setReaderObservationExceptionHandler(this);
    reader.addObserver(this);
    reader.startCardDetection(ObservableCardReader.DetectionMode.REPEATING);
  }

  private ObservableCardReader searchReader() {
    logger.info("Search SAM plugin and reader using filter: {}", samReaderFilter);
    for (Plugin plugin : SmartCardServiceProvider.getService().getPlugins()) {
      reader = searchReader(plugin);
      if (reader != null) {
        this.plugin = (ObservablePlugin) plugin;
        return reader;
      }
    }
    return null;
  }

  private ObservableCardReader searchReader(Plugin plugin) {
    logger.info(
        "Search SAM reader for plugin {} using filter: {}", plugin.getName(), samReaderFilter);
    Pattern p = Pattern.compile(samReaderFilter);
    for (CardReader reader : plugin.getReaders()) {
      logger.debug("Checking reader: {}", reader.getName());
      if (p.matcher(reader.getName()).matches()) {
        logger.info("Matching reader found: {}", reader.getName());
        return (ObservableCardReader) reader;
      }
    }
    logger.warn("No matching SAM reader found");
    return null;
  }
}
