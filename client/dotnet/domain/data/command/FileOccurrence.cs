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
    /// File occurrence selection strategy.
    /// </summary>
    public enum FileOccurrence
    {
        /// <summary>
        /// Select the first occurrence.
        /// </summary>
        FIRST,

        /// <summary>
        /// Select the last occurrence.
        /// </summary>
        LAST,

        /// <summary>
        /// Select the next occurrence.
        /// </summary>
        NEXT,

        /// <summary>
        /// Select the previous occurrence.
        /// </summary>
        PREVIOUS,
    }
}
