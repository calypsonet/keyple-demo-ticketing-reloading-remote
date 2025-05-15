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
    /// Selection mode.
    /// </summary>
    public enum MultiSelectionProcessing
    {
        /// <summary>
        /// Select only the first match.
        /// </summary>
        FIRST_MATCH,

        /// <summary>
        /// Process all matches.
        /// </summary>
        PROCESS_ALL,
    }
}
