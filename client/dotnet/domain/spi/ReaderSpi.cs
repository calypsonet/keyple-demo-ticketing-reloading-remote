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

namespace App.domain.spi
{
    /// <summary>
    /// The ReaderSpi interface represents the reader service provider interface (SPI).
    /// It provides methods to manage the card reader.
    /// </summary>
    public interface ReaderSpi
    {

        /// <summary>
        /// Retrieves a list of available smart card readers.
        /// </summary>
        /// <returns>A list of reader names.</returns>
        public List<string> GetReaders();

        /// <summary>
        /// Selects the reader to work with.
        /// </summary>
        /// <param name="readerName">The name of the reader.</param>
        void SelectReader(string readerName);

        /// <summary>
        /// Waits for a card to be inserted in the reader.
        /// </summary>
        /// <returns>True if a card is detected, otherwise false.</returns>
        bool WaitForCardPresent();

        /// <summary>
        /// Attempts to open the physical channel (to establish communication with the card).
        /// </summary>
        /// <exception cref="ReaderNotFoundException">If the communication with the reader has failed.</exception>
        /// <exception cref="CardIOException">If the communication with the card has failed.</exception>
        void OpenPhysicalChannel();

        /// <summary>
        /// Attempts to close the current physical channel.
        ///
        /// The physical channel may have been implicitly closed previously by a card withdrawal.
        /// </summary>
        /// <exception cref="ReaderNotFoundException">If the communication with the reader has failed.</exception>
        void ClosePhysicalChannel();

        /// <summary>
        /// Gets the power-on data.
        /// 
        /// The power-on data is defined as the data retrieved by the reader when the card is inserted.
        /// 
        /// In the case of a contact reader, this is the Answer To Reset data (ATR) defined by ISO7816.
        /// 
        /// In the case of a contactless reader, the reader decides what this data is. Contactless
        /// readers provide a virtual ATR (partially standardized by the PC/SC standard), but other devices
        /// can have their own definition, including for example elements from the anti-collision stage of
        /// the ISO14443 protocol (ATQA, ATQB, ATS, SAK, etc).
        /// 
        /// These data being variable from one reader to another, they are defined here in string format
        /// which can be either a hexadecimal string or any other relevant information.
        /// </summary>
        /// <returns>A not empty string.</returns>
        string GetPowerOnData();


        /// <summary>
        /// Transmits an Application Protocol Data Unit (APDU) command to the smart card and receives the response.
        /// </summary>
        /// <param name="commandApdu">The command APDU to be transmitted.</param>
        /// <returns>The response APDU received from the smart card.</returns>
        /// <exception cref="ReaderNotFoundException">If the communication with the reader has failed.</exception>
        /// <exception cref="CardIOException">If the communication with the card has failed.</exception>
        byte[] TransmitApdu(byte[] commandApdu);
    }
}
