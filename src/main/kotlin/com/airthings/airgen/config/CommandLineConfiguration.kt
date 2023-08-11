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
import com.beust.jcommander.Parameter

/**
 * Arguments available on the command-line when running [App].
 *
 * @param help whether to display usage information
 * @param verbose whether to be verbose about what is being done
 * @param model path to the file containing the model (.json, .yaml, or .yml)
 * @param base base directory from where the template directory is loaded
 * @param template logical path to the template directory (inside [base])
 * @param output path to the output directory to store all resulting files
 */
@Suppress("LongParameterList", "UseDataClass")
class CommandLineConfiguration constructor(
    @Parameter(
        names = ["--help", "-h"],
        description = "Display (this) usage information",
        help = true
    )
    var help: Boolean = true,

    @Parameter(
        names = ["--verbose", "-v"],
        description = "Be verbose about what is being done"
    )
    var verbose: Boolean = false,

    @Parameter(
        names = ["--model", "-m"],
        description = "Path to the file containing the model (.json, .yaml, or .yml)"
    )
    var model: String?,

    @Parameter(
        names = ["--base", "-b"],
        description = "Base directory from where the template directory is loaded"
    )
    var base: String?,

    @Parameter(
        names = ["--template", "-t"],
        description = "Logical path to the template directory (inside --base)"
    )
    var template: String?,

    @Parameter(
        names = ["--output", "-o"],
        description = "Path to the output directory to store all resulting files"
    )
    var output: String?,
)
