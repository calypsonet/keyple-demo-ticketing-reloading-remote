// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

namespace App.domain.data.command
{
    /// <summary>
    /// File information output format.
    /// </summary>
    public enum FileControlInformation
    {
        /// <summary>
        /// File Control Information (FCI) format.
        /// </summary>
        FCI,

        /// <summary>
        /// File Control Parameters (FCP) format.
        /// </summary>
        FCP,

        /// <summary>
        /// File Management Data (FMD) format.
        /// </summary>
        FMD,

        /// <summary>
        /// No response format.
        /// </summary>
        NO_RESPONSE,
    }
}
