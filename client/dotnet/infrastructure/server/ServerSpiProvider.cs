// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using App.domain.spi;

namespace App.infrastructure.server
{
    /// <summary>
    /// Provides a singleton instance of ServerSpi implemented by ServerSpiAdapter.
    /// </summary>
    public class ServerSpiProvider
    {
        private static ServerSpiAdapter? s_instance;
        private static readonly object s_lock = new object();

        private ServerSpiProvider() { }

        /// <summary>
        /// Gets the singleton instance of ServerSpiAdapter.
        /// </summary>
        public static ServerSpi getInstance(string baseUrl, int port, string endpoint)
        {
            if (s_instance == null)
            {
                lock (s_lock)
                {
                    if (s_instance == null)
                    {
                        s_instance = new ServerSpiAdapter(baseUrl, port, endpoint);
                    }
                }
            }
            return s_instance;
        }
    }
}
