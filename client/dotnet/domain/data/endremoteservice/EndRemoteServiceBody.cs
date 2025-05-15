// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using Newtonsoft.Json;

/// <summary>
/// The body content of the END_REMOTE_SERVICE message.
/// </summary>
public class EndRemoteServiceBody
{
    /// <summary>
    /// Core API level.
    /// </summary>
    [JsonProperty("coreApiLevel")]
    public required int CoreApiLevel { get; set; }

    /// <summary>
    /// Gets or sets the OutputData.
    /// </summary>
    [JsonProperty("outputData")]
    public required OutputData OutputData { get; set; }
}
