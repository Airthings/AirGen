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

package com.airthings.airgen.config

import com.airthings.airgen.App
import com.airthings.airgen.extension.contains
import freemarker.template.Configuration
import kotlin.system.exitProcess

/**
 * The [App]'s FreeMarker configuration to use.
 *
 * @param baseDirectory the base directory from which to load other templates
 * @param templateDirectory logical directory inside [baseDirectory] from which to read FreeMarker files
 * @param outputDirectory the directory to store the resulting output
 * @param ignorePrefix names of files and directories starting with this prefix are not processed
 */
@Suppress("MemberVisibilityCanBePrivate")
data class FreeMarkerConfiguration constructor(
    val baseDirectory: java.io.File,
    val templateDirectory: java.io.File,
    val outputDirectory: java.io.File,
    val ignorePrefix: String = "_",
) : Configuration(VERSION_2_3_31) {
    init {
        if (!baseDirectory.contains(templateDirectory)) {
            System.err.println("Fatal: template directory '$templateDirectory' is invalid.")
            exitProcess(App.EXIT_TEMPLATE_INVALID)
        }

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs()
            if (!outputDirectory.exists()) {
                System.err.println("Fatal: output directory '$outputDirectory' cannot be created.")
                exitProcess(App.EXIT_OUTPUT_INVALID)
            }
        }

        setDirectoryForTemplateLoading(baseDirectory)

        val charset = CHARSET.name()
        defaultEncoding = charset
        outputEncoding = charset
        urlEscapingCharset = charset
        localizedLookup = true
        fallbackOnNullLoopVariable = false
        recognizeStandardFileExtensions = true
        logTemplateExceptions = true
        wrapUncheckedExceptions = true
    }

    companion object {
        /**
         * File extension used in template files.
         */
        const val EXTENSION: String = ".ftl"

        /**
         * Character used for FreeMarker reader and writer.
         */
        val CHARSET: java.nio.charset.Charset = java.nio.charset.StandardCharsets.UTF_8
    }
}
