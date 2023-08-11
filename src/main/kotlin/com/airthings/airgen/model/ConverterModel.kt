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

package com.airthings.airgen.model

import com.airthings.airgen.converter.HtmlConverter
import com.airthings.airgen.converter.NumberConverter
import com.airthings.airgen.converter.TextConverter

/**
 * Contains an ever-growing list of converters and utilities.
 *
 * This class is instantiated once and thus must be immutable. Do not retain any session data between multiple
 * inclusions or runs.
 *
 * You can retrieve the singleton instance using [ConverterModel.instance].
 */
class ConverterModel private constructor() {
    /**
     * Helper methods for HTML manipulation.
     */
    val html: HtmlConverter by lazy {
        HtmlConverter()
    }

    /**
     * Helper methods for all other text manipulation.
     */
    val text: TextConverter by lazy {
        TextConverter()
    }

    /**
     * Helper methods for all other text manipulation.
     */
    val number: NumberConverter by lazy {
        NumberConverter()
    }

    companion object {
        /**
         * Returns the singleton instance of [ConverterModel].
         */
        val instance: ConverterModel by lazy {
            ConverterModel()
        }
    }
}
