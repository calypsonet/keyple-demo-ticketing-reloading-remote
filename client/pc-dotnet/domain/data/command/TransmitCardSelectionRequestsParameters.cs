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
    /// Represents parameters for transmitting card selection requests.
    /// </summary>
    public class TransmitCardSelectionRequestsParameters
    {
        /// <summary>
        /// Multi-selection processing mode.
        /// </summary>
        [JsonConverter(typeof(MultiSelectionProcessingConverter))]
        [JsonProperty("multiSelectionProcessing")]
        public required MultiSelectionProcessing MultiSelectionProcessing { get; set; }

        /// <summary>
        /// Channel control mode.
        /// </summary>
        [JsonConverter(typeof(ChannelControlConverter))]
        [JsonProperty("channelControl")]
        public required ChannelControl ChannelControl { get; set; }

        /// <summary>
        /// Card selector for the card selection.
        /// </summary>
        [JsonProperty("cardSelectors")]
        public required CardSelector[] CardSelectors { get; set; }

        /// <summary>
        /// Array of card selection requests.
        /// </summary>
        [JsonProperty("cardSelectionRequests")]
        public required CardSelectionRequest[] CardSelectionRequests { get; set; }
    }
}
