/* ******************************************************************************
 * Copyright (c) 2020 Calypso Networks Association https://calypsonet.org/
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-Clause License which is available at
 * https://opensource.org/licenses/BSD-3-Clause.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 ****************************************************************************** */
package org.calypsonet.keyple.demo.reload.remote.server.card;

import com.google.gson.JsonObject;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.keyple.core.service.SmartCardServiceProvider;
import org.eclipse.keyple.distributed.MessageDto;
import org.eclipse.keyple.distributed.RemotePluginServer;
import org.eclipse.keyple.distributed.SyncNodeServer;
import org.eclipse.keypop.reader.CardReader;
import org.eclipse.keypop.reader.ReaderCommunicationException;

@Path("/card")
public class CardController {

  @Inject CardConfigurator cardConfigurator;

  /**
   * The endpoint access associated with the remote plugin server.
   *
   * @param message The request.
   * @return A list of response messages.
   */
  @POST
  @Path("/remote-plugin")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public List<MessageDto> processMessage(MessageDto message) {

    // Retrieves the node associated to the remote plugin.
    SyncNodeServer node =
        SmartCardServiceProvider.getService()
            .getPlugin(CardConfigurator.REMOTE_PLUGIN_NAME)
            .getExtension(RemotePluginServer.class)
            .getSyncNode();

    // Forwards the message to the node and returns the response to the client.
    return node.onRequest(message);
  }

  /**
   * Return the SAM status.
   *
   * @return {isSamReady:true} if sam is ready.
   */
  @GET
  @Path("/sam-status")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getSamStatus() {
    boolean isSamReady;
    try {
      CardReader samReader = cardConfigurator.getSamReader();
      isSamReady = samReader != null && samReader.isCardPresent(); // ping sam
    } catch (ReaderCommunicationException e) {
      // reader is disconnected
      isSamReady = false;
    }
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("isSamReady", isSamReady);
    return Response.ok(jsonObject.toString()).build();
  }
}
