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

import com.airthings.airgen.App
import com.airthings.airgen.config.FreeMarkerConfiguration
import com.airthings.airgen.extension.logicalPath
import com.airthings.airgen.extension.stripExtension
import kotlin.system.exitProcess

/**
 * Top-level model used to process FreeMarker templates and files.
 *
 * @param config the FreeMarker configuration used
 * @param file logical path (inside configured [template directory][FreeMarkerConfiguration.templateDirectory])
 * to the template file that's currently being processed, optional
 * @param air contains an ever-growing list of utilities and helpers
 * @param input any structured model to be fed to the FreeMarker template
 */
@Suppress("MemberVisibilityCanBePrivate")
class TemplateModel constructor(
    private val config: FreeMarkerConfiguration,
    val file: String?,
    val air: ConverterModel,
    val input: Any,
) {
    /**
     * Processes the provided template path and its subdirectories and files.
     *
     * The provided [template] must reside within the configured
     * [base directory][FreeMarkerConfiguration.baseDirectory], otherwise an error is thrown.
     *
     * If [input] is missing, then the input configured with this [TemplateModel] is used.
     *
     * @param template the template directory to process (logical path to
     * [base directory][FreeMarkerConfiguration.baseDirectory])
     * @param input optional input to use, defaults to the input defined in this [TemplateModel]
     */
    @Throws(java.io.IOException::class)
    fun process(template: String? = null, input: Any? = null) {
        val templatePath = template?.trim()
        processDirectoryImpl(
            directory = if (templatePath.isNullOrEmpty()) {
                config.baseDirectory
            } else {
                java.io.File(config.baseDirectory, templatePath)
            },
            input = input ?: this.input
        )
    }

    /**
     * Processes [file] and stores the output in [outputPath].
     *
     * The provided [file] must reside within the configured
     * [template directory][FreeMarkerConfiguration.templateDirectory], otherwise an error is thrown.
     *
     * The provided [outputPath] must reside within the provided
     * [output directory][FreeMarkerConfiguration.outputDirectory], otherwise an error is thrown.
     *
     * If [input] is missing, then the input configured with this [TemplateModel] is used.
     *
     * @param file the input file to process
     * @param outputPath the output file path to store results in
     * @param input optional input to use, defaults to the input defined in this [TemplateModel]
     */
    @Throws(java.io.IOException::class)
    fun process(file: String, outputPath: String, input: Any? = null) {
        processFile(
            templateDirectory = config.templateDirectory,
            file = file,
            outputFile = java.io.File(config.outputDirectory, outputPath),
            input = input
        )
    }

    /**
     * Processes [file] and stores the output in [outputPath].
     *
     * The provided [file] must reside within the provided [template directory][template], otherwise
     * an error is thrown.
     *
     * The provided [outputPath] must reside within the provided
     * [output directory][FreeMarkerConfiguration.outputDirectory], otherwise an error is thrown.
     *
     * If [input] is missing, then the input configured with this [TemplateModel] is used.
     *
     * @param template the template directory to process (logical path residing under
     * [base directory][FreeMarkerConfiguration.baseDirectory])
     * @param file the input file to process (residing in [template])
     * @param outputPath the output file path to store results in
     * @param input optional input to use, defaults to the input defined in this [TemplateModel]
     */
    @Throws(java.io.IOException::class)
    fun process(template: String, file: String, outputPath: String, input: Any? = null) {
        val templatePath = template.trim()
        processFile(
            templateDirectory = if (templatePath.isEmpty()) {
                config.baseDirectory
            } else {
                java.io.File(config.baseDirectory, templatePath)
            },
            file = file,
            outputFile = java.io.File(config.outputDirectory, outputPath),
            input = input
        )
    }

    @Throws(java.io.IOException::class)
    private fun ensureDirectoryExists(directory: java.io.File) {
        if (directory.isFile) {
            throw java.io.IOException(
                "Fatal: unable to create directory, " +
                    "a file already exists with the same name: $directory"
            )
        }

        if (!directory.isDirectory) {
            directory.mkdirs()
            if (!directory.isDirectory) {
                throw java.io.IOException("Fatal: unable to create directory: $directory")
            }
        }
    }

    @Throws(java.io.IOException::class)
    private fun processFile(
        templateDirectory: java.io.File,
        file: String,
        outputFile: java.io.File,
        input: Any? = null,
    ) {
        val inputFile = java.io.File(templateDirectory, file)
        if (!inputFile.isFile || !inputFile.canRead()) {
            System.err.println("File '$inputFile' must be readable.")
            exitProcess(App.EXIT_INPUT_INVALID)
        }

        outputFile.parentFile?.apply(::ensureDirectoryExists)

        outputFile
            .stripExtension(FreeMarkerConfiguration.EXTENSION)
            .writer()
            .use {
                val model = TemplateModel(
                    config = FreeMarkerConfiguration(
                        baseDirectory = config.baseDirectory,
                        templateDirectory = templateDirectory.canonicalFile,
                        outputDirectory = config.outputDirectory,
                        ignorePrefix = config.ignorePrefix
                    ),
                    file = file,
                    air = air,
                    input = input ?: this.input
                )

                config
                    .getTemplate(config.baseDirectory.logicalPath(inputFile).path)
                    .process(model, it)
            }
    }

    @Throws(java.io.IOException::class)
    private fun processDirectoryImpl(directory: java.io.File, input: Any) {
        directory.listFiles()
            ?.filterNot {
                val name = it.name
                "." == name || ".." == name || name.startsWith(config.ignorePrefix)
            }
            ?.forEach { file ->
                when {
                    file.isDirectory ->
                        processDirectoryImpl(file, input)

                    file.isFile -> {
                        val logicalFile = config.templateDirectory.logicalPath(file)
                        val outputFile = java.io.File(config.outputDirectory, logicalFile.path)

                        if (file.name.endsWith(FreeMarkerConfiguration.EXTENSION)) {
                            processFile(
                                templateDirectory = config.templateDirectory,
                                file = logicalFile.path,
                                outputFile = outputFile,
                                input = input
                            )
                        } else {
                            file.copyTo(outputFile)
                        }
                    }

                    else -> System.err.println("Ignoring unknown file: ${file.canonicalPath}")
                }
            }
    }
}
