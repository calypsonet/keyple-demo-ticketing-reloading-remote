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
    /// Represents the parameters for transmitting a card request.
    /// </summary>
    public class TransmitCardRequestParameters
    {
        /// <summary>
        /// Card request.
        /// </summary>
        [JsonProperty("cardRequest")]
        public required CardRequest CardRequest { get; set; }

        /// <summary>
        /// Channel control mode.
        /// </summary>
        [JsonConverter(typeof(ChannelControlConverter))]
        [JsonProperty("channelControl")]
        public required ChannelControl ChannelControl { get; set; }
    }
}
