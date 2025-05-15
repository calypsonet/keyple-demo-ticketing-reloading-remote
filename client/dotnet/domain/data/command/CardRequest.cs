// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using Newtonsoft.Json;

namespace App.domain.data.command
{
    /// <summary>
    /// Represents a card request containing a list of APDU requests.
    /// </summary>
    public class CardRequest
    {
        /// <summary>
        /// List of APDU requests.
        /// </summary>
        [JsonProperty("apduRequests")]
        public required List<ApduRequest> ApduRequests { get; set; }

        /// <summary>
        /// A value indicating whether status codes verification is enabled.
        /// </summary>
        [JsonProperty("stopOnUnsuccessfulStatusWord")]
        public bool StopOnUnsuccessfulStatusWord { get; set; }
    }
}
