// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

namespace App.domain.spi
{
    /// <summary>
    /// An exception that is thrown when the reader is not found.
    /// </summary>
    public class ReaderNotFoundException : Exception
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="ReaderNotFoundException"/> class.
        /// </summary>
        public ReaderNotFoundException() { }

        /// <summary>
        /// Initializes a new instance of the <see cref="ReaderNotFoundException"/> class with the specified error message.
        /// </summary>
        /// <param name="message">The error message that explains the reason for the exception.</param>
        public ReaderNotFoundException(string message) : base(message) { }

        /// <summary>
        /// Initializes a new instance of the <see cref="ReaderNotFoundException"/> class with the specified error message and inner exception.
        /// </summary>
        /// <param name="message">The error message that explains the reason for the exception.</param>
        /// <param name="innerException">The exception that is the cause of the current exception, or a null reference if no inner exception is specified.</param>
        public ReaderNotFoundException(string message, Exception innerException) : base(message, innerException) { }
    }
}
