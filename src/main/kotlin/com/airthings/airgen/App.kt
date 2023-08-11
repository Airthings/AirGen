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

package com.airthings.airgen

import com.airthings.airgen.config.CommandLineConfiguration
import com.airthings.airgen.config.FreeMarkerConfiguration
import com.airthings.airgen.model.ConverterModel
import com.airthings.airgen.model.TemplateModel
import com.beust.jcommander.JCommander
import com.google.gson.Gson
import kotlin.system.exitProcess
import org.yaml.snakeyaml.Yaml

/**
 * Main class of AirGen.
 */
object App {
    /**
     * Main entry point of AirGen.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        AppUsageRunner(args).run()
    }

    /**
     * Exit code indicating that the app ran as intended.
     */
    const val EXIT_NORMAL: Int = 0

    /**
     * Exit code indicating that the usage page was shown.
     */
    const val EXIT_USAGE: Int = 99

    /**
     * Exit code indicating an invalid model file is passed as argument.
     */
    const val EXIT_MODEL_INVALID: Int = 1

    /**
     * Exit code indicating an invalid base directory is passed as argument.
     */
    const val EXIT_BASE_INVALID: Int = 2

    /**
     * Exit code indicating an invalid logical template directory is passed as argument.
     */
    const val EXIT_TEMPLATE_INVALID: Int = 3

    /**
     * Exit code indicating an invalid output directory is passed as argument.
     */
    const val EXIT_OUTPUT_INVALID: Int = 4

    /**
     * Exit code indicating an invalid input data is detected.
     */
    const val EXIT_INPUT_INVALID: Int = 5
}

private class AppUsageRunner constructor(args: Array<String>) : Runnable {
    private val commandLineConfig = CommandLineConfiguration(
        help = false,
        model = null,
        base = null,
        template = null,
        output = null
    )
    private val parser = JCommander.newBuilder()
        .addObject(commandLineConfig)
        .build()
        .also {
            it.programName = "java --jar path/to/airgen.jar"

            @Suppress("SpreadOperator")
            it.parse(*args)
        }

    override fun run() {
        val modelPath = commandLineConfig.model
        val basePath = commandLineConfig.base
        val templatePath = commandLineConfig.template
        val outputPath = commandLineConfig.output

        if (
            modelPath.isNullOrBlank() ||
            basePath.isNullOrBlank() ||
            outputPath.isNullOrBlank()
        ) {
            parser.usage()
            exitProcess(if (commandLineConfig.help) App.EXIT_NORMAL else App.EXIT_USAGE)
        }

        val modelFile = java.io.File(modelPath)
        if (!modelFile.isFile) {
            System.err.println("Fatal: model file '$modelPath' is invalid.")
            exitProcess(App.EXIT_MODEL_INVALID)
        }

        // Retrieve the initial input model.
        val input = modelFile.bufferedReader().use {
            if (modelFile.name.endsWith(".json", ignoreCase = true)) {
                Gson().fromJson(it, Any::class.java)
            } else {
                Yaml().load(it)
            }
        }

        val baseDirectory = java.io.File(basePath)
        if (!baseDirectory.isDirectory) {
            System.err.println("Fatal: base directory '$basePath' is invalid.")
            exitProcess(App.EXIT_BASE_INVALID)
        }

        val templateDirectory = if (templatePath.isNullOrBlank()) {
            baseDirectory
        } else {
            java.io.File(baseDirectory, templatePath)
        }

        val outputDirectory = java.io.File(outputPath)

        val freeMarkerConfig = FreeMarkerConfiguration(
            baseDirectory.canonicalFile,
            templateDirectory.canonicalFile,
            outputDirectory.canonicalFile
        )

        TemplateModel(
            freeMarkerConfig,
            null,
            ConverterModel.instance,
            input
        ).process(templatePath)
    }
}
