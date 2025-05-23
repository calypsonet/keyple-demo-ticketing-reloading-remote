// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace App.domain.data.response
{
    /// <summary>
    /// Represents an error.
    /// </summary>
    public class Error
    {
        /// <summary>
        /// Error code.
        /// </summary>
        [JsonConverter(typeof(StringEnumConverter))]
        [JsonProperty("code")]
        public required ErrorCode Code { get; set; }

        /// <summary>
        /// Error message.
        /// </summary>
        [JsonProperty("message")]
        public required string Message { get; set; }
    }
}
