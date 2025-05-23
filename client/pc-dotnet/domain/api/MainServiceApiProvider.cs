// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using App.domain.spi;

namespace App.domain.api
{
    /// <summary>
    /// Provider class for creating an instance of the MainServiceApi.
    /// </summary>
    internal class MainServiceApiProvider
    {
        /// <summary>
        /// Creates and returns an instance of the MainServiceApi.
        /// </summary>
        /// <param name="readerSpi">The ReaderSpi implementation to use.</param>
        /// <param name="readerName">The name of the reader to use.</param>
        /// <param name="serverSpi">The ServerSpi implementation to use.</param>
        /// <returns>An instance of the MainServiceApi.</returns>
        public static MainServiceApi getService(ReaderSpi readerSpi, string readerName, ServerSpi serverSpi)
        {
            return new MainServiceApiAdapter(readerSpi, readerName, serverSpi);
        }
    }
}
