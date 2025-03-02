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

using System.Text;
using App.domain.spi;
using Serilog;
using Serilog.Events;

namespace App.infrastructure.server
{
    /// <summary>
    /// The ServerSpiAdapter class implements ServerSpi and provides functionality
    /// to transmit a JSON request to a specified server.
    /// </summary>
    internal class ServerSpiAdapter : ServerSpi
    {
        private readonly ILogger _logger;
        private readonly string _baseUrl;
        private readonly string _endpoint;

        /// <summary>
        /// Initializes a new instance of the ServerSpiAdapter class.
        /// </summary>
        /// <param name="baseUrl">The base URL of the server to connect to.</param>
        /// <param name="port">The port to connect on.</param>
        /// <param name="endpoint">The endpoint to send requests to.</param>
        public ServerSpiAdapter(string baseUrl, int port, string endpoint)
        {
            _logger = Log.ForContext<ServerSpiAdapter>();
            _baseUrl = $"{baseUrl}:{port}";
            _endpoint = endpoint;
        }

        /// <summary>
        /// Transmit a JSON request to the server and return the server's response.
        /// </summary>
        /// <param name="jsonRequest">The JSON request to transmit.</param>
        /// <returns>A JSON string containing the server's response.</returns>
        public string transmitRequest(string jsonRequest)
        {
            if (_logger.IsEnabled(LogEventLevel.Debug))
            {
                _logger.Debug($"Tx Json = {jsonRequest}");
            }
            string? result = null;
            try
            {
                // Initialize a new HttpClient with the base URL
                using (HttpClient httpClient = new HttpClient { BaseAddress = new Uri(_baseUrl) })
                {
                    // Create the content of the POST request
                    StringContent content = new StringContent(jsonRequest, Encoding.UTF8, "application/json");

                    // Send the POST request and get the response
                    HttpResponseMessage response = httpClient.PostAsync(_endpoint, content).Result;

                    if (response.IsSuccessStatusCode)
                    {
                        // If the request was successful, read the content of the response
                        result = response.Content.ReadAsStringAsync().Result;

                        if (_logger.IsEnabled(LogEventLevel.Debug))
                        {
                            _logger.Debug($"Rx Json = {result}");
                        }
                    }
                    else
                    {
                        // If the request was not successful, throw an exception with the status code
                        throw new ServerIOException($"Server status code: {response.StatusCode}");
                    }
                }
            }
            catch (Exception ex)
            {
                // If an exception occurred, throw a new exception with the message of the original exception
                throw new ServerIOException($"Exception when calling the API: {ex.Message}", ex);
            }

            // Return the result of the request
            return result;
        }
    }
}
