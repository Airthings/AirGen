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
 * Helper methods for number formatting.
 */
class NumberConverter {
    /**
     * Formats and returns [number] as a hexadecimal String.
     *
     * @param number the [Number] to format
     * @param withPrefix whether to add a "0x" prefix to the returned String
     * @param minimalDigits minimal number of digits to zero-fill the returned formatted number
     */
    fun toHex(number: Number, withPrefix: Boolean = false, minimalDigits: Int? = null): String {
        var formattedNumber = number.toLong().toString(HEXADECIMAL_RADIX)
        if (null != minimalDigits) {
            formattedNumber = formattedNumber.zeroFillInternal(minimalDigits)
        }
        return if (withPrefix) "0x$formattedNumber" else formattedNumber
    }

    /**
     * Formats and returns [number] as a hexadecimal String.
     *
     * @param number the [Number] to format
     * @param withPrefix whether to add a "0x" prefix to the returned String
     * @param minimalDigits minimal number of digits to zero-fill the returned formatted number
     */
    fun toHex(number: String, withPrefix: Boolean = false, minimalDigits: Int? = null): String {
        var formattedNumber = number.toLong().toString(HEXADECIMAL_RADIX)
        if (null != minimalDigits) {
            formattedNumber = formattedNumber.zeroFillInternal(minimalDigits)
        }
        return if (withPrefix) "0x$formattedNumber" else formattedNumber
    }

    /**
     * Returns a new number with a number of digits equal or greater than [minimalDigits].
     *
     * If [number] is shorter than [minimalDigits], then the returned number will have zero digits added to its
     * left-side (meaning, mathematically, they are insignificant).
     *
     * @param number the [Number] to zero-fill
     * @param minimalDigits minimal number of digits to zero-fill the returned new number
     */
    fun zeroFill(number: Number, minimalDigits: Int): String {
        val normalizedNumber = number.toLong().toString()
        return normalizedNumber.zeroFillInternal(minimalDigits)
    }

    /**
     * Returns a new number with a number of digits equal or greater than [minimalDigits].
     *
     * If [number] is shorter than [minimalDigits], then the returned number will have zero digits added to its
     * left-side (meaning, mathematically, they are insignificant).
     *
     * @param number the [Number] to zero-fill
     * @param minimalDigits minimal number of digits to zero-fill the returned new number
     */
    fun zeroFill(number: String, minimalDigits: Int): String {
        val normalizedNumber = number.toLong().toString()
        return normalizedNumber.zeroFillInternal(minimalDigits)
    }

    private fun String.zeroFillInternal(minimalDigits: Int): String = if (length >= minimalDigits) {
        this
    } else {
        "${"0".repeat(minimalDigits - length)}$this"
    }

    companion object {
        /**
         * The radix for hexadecimal numbers.
         */
        const val HEXADECIMAL_RADIX: Int = 16
    }
}
