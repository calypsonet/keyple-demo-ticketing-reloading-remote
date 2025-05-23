// Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
//
// This program and the accompanying materials are made available under the
// terms of the BSD 3-Clause License which is available at
// https://opensource.org/licenses/BSD-3-Clause.
//
// SPDX-License-Identifier: BSD-3-Clause

namespace App.domain.api
{
    /// <summary>
    /// An exception that is thrown when the card sent an unexpected status word.
    /// </summary>
    public class UnexpectedStatusWordException : Exception
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="UnexpectedStatusWordException"/> class.
        /// </summary>
        public UnexpectedStatusWordException() { }

        /// <summary>
        /// Initializes a new instance of the <see cref="UnexpectedStatusWordException"/> class with the specified error message.
        /// </summary>
        /// <param name="message">The error message that explains the reason for the exception.</param>
        public UnexpectedStatusWordException(string message) : base(message) { }

        /// <summary>
        /// Initializes a new instance of the <see cref="UnexpectedStatusWordException"/> class with the specified error message and inner exception.
        /// </summary>
        /// <param name="message">The error message that explains the reason for the exception.</param>
        /// <param name="innerException">The exception that is the cause of the current exception, or a null reference if no inner exception is specified.</param>
        public UnexpectedStatusWordException(string message, Exception innerException) : base(message, innerException) { }
    }
}

