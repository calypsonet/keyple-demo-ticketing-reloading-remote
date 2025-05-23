// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using App.domain.utils;
using Newtonsoft.Json;

namespace App.domain.data.response
{
    /// <summary>
    /// Represents an APDU response.
    /// </summary>
    public class ApduResponse
    {
        /// <summary>
        /// APDU data.
        /// </summary>
        [JsonConverter(typeof(HexStringToByteArrayConverter))]
        [JsonProperty("apdu")]
        public required byte[] Apdu { get; set; }

        /// <summary>
        /// Status word.
        /// </summary>
        [JsonConverter(typeof(HexStringToIntConverter))]
        [JsonProperty("statusWord")]
        public int StatusWord { get; set; }
    }
}
