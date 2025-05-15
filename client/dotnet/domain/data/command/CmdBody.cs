// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using Newtonsoft.Json;

namespace App.domain.data.command
{
    /// <summary>
    /// Represents the body of a command. It is dedicated to the message identification step.
    /// </summary>
    public class CmdBody
    {
        /// <summary>
        /// Core API level.
        /// </summary>
        [JsonProperty("coreApiLevel")]
        public required int CoreApiLevel { get; set; }

        /// <summary>
        /// Service name.
        /// </summary>
        [JsonProperty("service")]
        public required string Service { get; set; }
    }
}
