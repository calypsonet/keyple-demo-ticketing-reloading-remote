// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

namespace App.domain.data.response
{

    /// <summary>
    /// Error codes.
    /// </summary>
    public enum ErrorCode
    {
        /// <summary>
        /// Reader communication error.
        /// </summary>
        READER_COMMUNICATION_ERROR,

        /// <summary>
        /// Card communication error.
        /// </summary>
        CARD_COMMUNICATION_ERROR,

        /// <summary>
        /// Card command error.
        /// </summary>
        CARD_COMMAND_ERROR,
    }
}
