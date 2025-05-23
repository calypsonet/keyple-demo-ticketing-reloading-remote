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
    /// Represents the body of a response for the "IS_CONTACTLESS" service.
    /// </summary>
    public class IsContactlessRespBody
    {
        /// <summary>
        /// Core API level.
        /// </summary>
        [JsonProperty("coreApiLevel")]
        public const int CoreApiLevel = ApiInfo.CORE_API_LEVEL;

        /// <summary>
        /// Service name.
        /// </summary>
        [JsonProperty("service")]
        public const string Service = "IS_CONTACTLESS";

        /// <summary>
        /// Contactless mode flag.
        /// </summary>
        [JsonProperty("result", NullValueHandling = NullValueHandling.Ignore)]
        public bool? Result { get; set; }

        /// <summary>
        /// Error information, if any.
        /// </summary>
        [JsonProperty("error", NullValueHandling = NullValueHandling.Ignore)]
        public Error? Error { get; set; }
    }
}
