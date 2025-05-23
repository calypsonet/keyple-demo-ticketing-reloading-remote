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
    /// Represents a card selection response.
    /// </summary>
    public class CardSelectionResponse
    {
        /// <summary>
        /// A value indicating whether a card has been matched.
        /// </summary>
        [JsonProperty("hasMatched")]
        public bool HasMatched { get; set; }

        /// <summary>
        /// The power-on data.
        /// </summary>
        [JsonProperty("powerOnData")]
        public string? PowerOnData { get; set; }

        /// <summary>
        /// Response of the selection application command.
        /// </summary>
        [JsonProperty("selectApplicationResponse")]
        public ApduResponse? SelectApplicationResponse { get; set; }

        /// <summary>
        /// Card response.
        /// </summary>
        [JsonProperty("cardResponse")]
        public CardResponse? CardResponse { get; set; }
    }
}
