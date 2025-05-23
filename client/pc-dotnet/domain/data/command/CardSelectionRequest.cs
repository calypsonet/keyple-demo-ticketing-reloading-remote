// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using App.domain.utils;
using Newtonsoft.Json;

namespace App.domain.data.command
{
    /// <summary>
    /// Represents a card selection request.
    /// </summary>
    public class CardSelectionRequest
    {
        /// <summary>
        /// Card request associated with the card selection.
        /// </summary>
        [JsonProperty("cardRequest")]
        public CardRequest? CardRequest { get; set; }

        /// <summary>
        /// Successful status words of the selection application command.
        /// </summary>
        [JsonConverter(typeof(HexStringToSetToIntHashSetConverter))]
        [JsonProperty("successfulSelectionStatusWords")]
        public HashSet<int>? SuccessfulSelectionStatusWords { get; set; }
    }
}
