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
    /// Card selector used for card selection.
    /// </summary>
    public class CardSelector
    {
        /// <summary>
        /// Card protocol.
        /// </summary>
        [JsonProperty("logicalProtocolName")]
        public string? LogicalProtocolName { get; set; }

        /// <summary>
        /// Power On Data regular expression filter.
        /// </summary>
        [JsonProperty("powerOnDataRegex")]
        public string? PowerOnDataRegex { get; set; }

        /// <summary>
        /// Application Identifier (AID) of the card.
        /// </summary>
        [JsonConverter(typeof(HexStringToByteArrayConverter))]
        [JsonProperty("aid")]
        public byte[]? Aid { get; set; }

        /// <summary>
        /// File occurrence.
        /// </summary>
        [JsonConverter(typeof(FileOccurrenceConverter))]
        [JsonProperty("fileOccurrence")]
        public FileOccurrence FileOccurrence { get; set; }

        /// <summary>
        /// File control information.
        /// </summary>
        [JsonConverter(typeof(FileControlInformationConverter))]
        [JsonProperty("fileControlInformation")]
        public FileControlInformation FileControlInformation { get; set; }
    }
}
