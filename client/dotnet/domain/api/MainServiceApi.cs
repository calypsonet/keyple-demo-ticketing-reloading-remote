// Copyright (c) 2023 Calypso Networks Association https://calypsonet.org/
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// SPDX-License-Identifier: MIT

namespace App.domain.api
{
    /// <summary>
    /// Defines the operations that the main service API must support. 
    /// These operations primarily involve interacting with a card, where the card transaction
    /// is driven by the server provided to the implementation of this interface.
    /// </summary>
    public interface MainServiceApi
    {

        /// <summary>
        /// Blocks until a card is inserted. 
        /// </summary>
        void WaitForCardInsertion();

        /// <summary>
        /// Selects the card and reads contracts.
        /// The transaction is fully operated by the server.
        /// </summary>
        /// <returns>A string representation of the contracts read from the card.</returns>
        string SelectCardAndReadContracts();

        /// <summary>
        /// Selects the card and increases the counter of its MULTI_TRIP contract if it exists.
        /// The transaction is fully operated by the server.
        /// </summary>
        /// <param name="counterIncrement">The amount by which to increase the contract's counter.</param>
        /// <returns>A string representation of the status of the operation.</returns>
        string SelectCardAndIncreaseContractCounter(int counterIncrement);
    }
}
