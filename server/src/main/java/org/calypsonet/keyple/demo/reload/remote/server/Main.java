/* ******************************************************************************
 * Copyright (c) 2020 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT License which is available at
 * https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.server;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import java.awt.*;
import java.net.URI;
import javax.inject.Inject;
import org.calypsonet.keyple.demo.reload.remote.server.card.CardConfigurator;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Main class of quarkus */
@QuarkusMain
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String... args) {
    Quarkus.run(AppServer.class, args);
  }

  /** Main class of the Demo Application. */
  public static class AppServer implements QuarkusApplication {

    @ConfigProperty(name = "quarkus.http.port")
    Integer assignedPort;

    @Inject CardConfigurator cardConfigurator;

    @Override
    public int run(String... args) throws Exception {
      // Start the SAM & Calypso Card configuration
      cardConfigurator.init();
      // Open the dashboard on the default browser
      URI webappUri = new URI("http://localhost:" + assignedPort + "/");
      Desktop.getDesktop().browse(webappUri);
      logger.info("Keyple Demo Reload Server started at port {}", assignedPort);
      Quarkus.waitForExit();
      return 0;
    }
  }
}
