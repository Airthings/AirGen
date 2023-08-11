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
 * Helper methods for HTML manipulation.
 */
class HtmlConverter {
    /**
     * Returns a new String where the the [double] and/or [single] quotes have been replaced by "&amp;#34;"
     * and "&amp;#39;", respectively.
     *
     * @param string input string to convert
     * @param double whether to replace double quotes or not
     * @param single whether to replace single quotes or not
     */
    fun escapeQuotes(string: String, double: Boolean = true, single: Boolean = false): String {
        var escapedString = string
        if (double) {
            escapedString = escapedString.replace("\"", "&#34;")
        }
        if (single) {
            escapedString = escapedString.replace("'", "&#39;")
        }
        return escapedString
    }

    /**
     * Returns a new String where the HTML "&amp;", "&lt;", and "&gt;" characters have been converted to
     * "&amp;amp;", "&amp;lt;", and "&amp;gt;", respectively.
     *
     * @param string input string to convert
     */
    fun escapeHtml(string: String): String =
        string
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
}
