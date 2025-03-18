// Copyright (c) 2023 Calypso Networks Association https://calypsonet.org/
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// SPDX-License-Identifier: MIT

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
