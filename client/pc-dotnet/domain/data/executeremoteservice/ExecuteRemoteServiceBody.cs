// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using Newtonsoft.Json;

namespace App.domain.data.executeremoteservice
{
    /// <summary>
    /// The body content of the EXECUTE_REMOTE_SERVICE message.
    /// </summary>
    class ExecuteRemoteServiceBody
    {
        /// <summary>
        /// Core API level.
        /// </summary>
        [JsonProperty("coreApiLevel")]
        public const int CoreApiLevel = ApiInfo.CORE_API_LEVEL;

        /// <summary>
        /// Service ID.
        /// </summary>
        [JsonProperty("serviceId")]
        public required string ServiceId { get; set; }

        /// <summary>
        /// A value indicating whether the reader is contactless.
        /// </summary>
        [JsonProperty("isReaderContactless")]
        public bool IsReaderContactless { get; set; }

        /// <summary>
        /// Input data.
        /// </summary>
        [JsonProperty("inputData")]
        public InputData? InputData { get; set; }
    }
}
