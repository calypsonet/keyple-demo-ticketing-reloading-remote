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
    /// Represents an APDU request.
    /// </summary>
    public class ApduRequest
    {
        /// <summary>
        /// APDU data.
        /// </summary>
        [JsonConverter(typeof(HexStringToByteArrayConverter))]
        [JsonProperty("apdu")]
        public required byte[] Apdu { get; set; }

        /// <summary>
        /// Successful status words for the APDU.
        /// </summary>
        [JsonConverter(typeof(HexStringToSetToIntHashSetConverter))]
        [JsonProperty("successfulStatusWords")]
        public required HashSet<int> SuccessfulStatusWords { get; set; }

        /// <summary>
        /// Extra information about the APDU.
        /// </summary>
        [JsonProperty("info")]
        public string? Info { get; set; }
    }
}
