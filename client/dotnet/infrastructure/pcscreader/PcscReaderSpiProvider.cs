// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using App.domain.spi;

namespace App.infrastructure.pcscreader
{
    /// <summary>
    /// Provides a singleton instance of PcscReaderSpi implemented by PcscReaderSpiAdapter.
    /// </summary>
    public class PcscReaderSpiProvider
    {
        private static PcscReaderSpiAdapter? s_instance;
        private static readonly object s_lock = new object();

        private PcscReaderSpiProvider() { }

        /// <summary>
        /// Gets the singleton instance of PcscReaderSpiAdapter.
        /// </summary>
        public static ReaderSpi getInstance()
        {
            if (s_instance == null)
            {
                lock (s_lock)
                {
                    if (s_instance == null)
                    {
                        s_instance = new PcscReaderSpiAdapter();
                    }
                }
            }
            return s_instance;
        }
    }
}
