// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

namespace App.domain.spi
{
    /// <summary>
    /// The ServerSpi interface represents the server service provider interface (SPI).
    /// It provides a method for transmitting requests to the server.
    /// </summary>
    public interface ServerSpi
    {

        /// <summary>
        /// Transmits a JSON-formatted request string to the server.
        /// </summary>
        /// <param name="jsonRequest">The JSON-formatted string that contains the request to be sent to the server.</param>
        /// <returns>
        /// A JSON string representing the server's response.
        /// </returns>
        /// <exception cref="ServerIOException">If the communication with the server has failed.</exception>
        string transmitRequest(string jsonRequest);
    }
}
