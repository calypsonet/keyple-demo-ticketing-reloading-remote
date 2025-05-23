// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using Newtonsoft.Json;

namespace App.domain.data.response
{
    /// <summary>
    /// Represents a card response.
    /// </summary>
    public class CardResponse
    {
        /// <summary>
        /// Z value indicating whether the logical channel is open.
        /// </summary>
        [JsonProperty("isLogicalChannelOpen")]
        public bool IsLogicalChannelOpen { get; set; }

        /// <summary>
        /// List of APDU responses.
        /// </summary>
        [JsonProperty("apduResponses")]
        public required List<ApduResponse> ApduResponses { get; set; }
    }
}
