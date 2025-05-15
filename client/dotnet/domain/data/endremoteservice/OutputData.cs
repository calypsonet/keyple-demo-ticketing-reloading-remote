// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

using Newtonsoft.Json;
/// <summary>
/// Output data used for the server operation result.
/// </summary>
public class OutputData
{
    /// <summary>
    /// Gets or sets the contracts.
    /// </summary>
    [JsonProperty("items")]
    public required List<string> Items { get; set; }

    /// <summary>
    /// Gets or sets the status code.
    /// </summary>
    [JsonProperty("statusCode")]
    public int StatusCode { get; set; }

    /// <summary>
    /// Gets or sets the message.
    /// </summary>
    [JsonProperty("message")]
    public required string Message { get; set; }
}
