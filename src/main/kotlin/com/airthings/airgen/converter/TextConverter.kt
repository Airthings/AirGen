/*
 * Copyright 2023 Airthings ASA. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the “Software”), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

@file:Suppress("unused")

package com.airthings.airgen.converter

/**
 * Helper methods for all other text manipulation.
 */
class TextConverter {
    /**
     * Returns a new String where the the [double] and/or [single] quotes have been replaced by \"
     * and \', respectively.
     *
     * @param string input string to convert
     * @param double whether to replace double quotes or not
     * @param single whether to replace single quotes or not
     */
    fun escapeQuotes(string: String, double: Boolean = true, single: Boolean = false): String {
        var escapedString = string
        if (double) {
            escapedString = escapedString.replace("\"", "\\\"")
        }
        if (single) {
            escapedString = escapedString.replace("'", "\\'")
        }
        return escapedString
    }

    /**
     * Returns a new String where a CR ("\r") and LF ("\n") characters have been replaced by "\\r"
     * and "\\n", respectively.
     *
     * @param string input string to convert
     * @param trimLines whether to trim each individual line or not
     */
    @Suppress("NestedBlockDepth")
    fun oneLiner(string: String, trimLines: Boolean = true): String {
        var normalizedString: String

        if (trimLines) {
            val stringBuilder = StringBuilder()

            normalizedString = string.trim()
            val normalizedStringLength = normalizedString.length
            val crlfChars = charArrayOf(CR_CHAR, LF_CHAR)
            var startIndex = 0

            do {
                val crlfIndex = normalizedString.indexOfAny(crlfChars, startIndex)
                if (0 > crlfIndex) {
                    break
                }

                // Add the found string, trimmed.
                stringBuilder.append(normalizedString.substring(startIndex, crlfIndex).trimEnd())

                // Add the CRLF characters until a non-CRLF character is found.
                var nonCrlfIndex = crlfIndex
                while (normalizedStringLength > ++nonCrlfIndex) {
                    val char = normalizedString[nonCrlfIndex]
                    if (CR_CHAR != char && LF_CHAR != char) {
                        break
                    }
                }

                // Add the CRLF characters.
                stringBuilder.append(normalizedString.substring(crlfIndex, nonCrlfIndex))

                // Find the next character that isn't a space.
                startIndex = nonCrlfIndex
                while (normalizedStringLength > startIndex) {
                    val char = normalizedString[startIndex]
                    if (' ' == char) startIndex++ else break
                }
            } while (normalizedStringLength > startIndex)

            // Last string to add.
            if (normalizedStringLength > startIndex) {
                stringBuilder.append(normalizedString.substring(startIndex))
            }

            normalizedString = stringBuilder.toString()
        } else {
            normalizedString = string
        }

        return normalizedString
            .replace(CR_STRING, "\\r")
            .replace(LF_STRING, "\\n")
    }

    companion object {
        private const val CR_STRING = "\r"
        private const val CR_CHAR = '\r'
        private const val LF_STRING = "\n"
        private const val LF_CHAR = '\n'
    }
}
