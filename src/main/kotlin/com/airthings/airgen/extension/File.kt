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

package com.airthings.airgen.extension

/**
 * Returns the logical path of [file] in relation to this [File][java.io.File].
 *
 * Example: If this File's canonical path is "/var/log/airgun" and this function is called with a [file]
 * whose canonical path is "/var/log/airgun/2020/jan/001.log", then the return result would be "2020/jan/001.log".
 *
 * If the function is called with a file path that doesn't live inside this File's canonical path, then the
 * canonical path of [file] is returned as-is.
 *
 * @param file the file instance to use
 * @param ignoreCase if true then the case is ignored when checking for paths
 */
fun java.io.File.logicalPath(file: java.io.File, ignoreCase: Boolean = false): java.io.File =
    java.io.File(logicalPath(file.canonicalPath, ignoreCase))

/**
 * Returns the logical path of [path] in relation to this [File][java.io.File].
 *
 * Example: If this File's canonical path is "/var/log/airgun" and this function is called with a [path]
 * whose canonical path is "/var/log/airgun/2020/jan/001.log", then the return result would be "2020/jan/001.log".
 *
 * If the function is called with a file path that doesn't live inside this File's canonical path, then the
 * canonical path of [path] is returned as-is.
 *
 * @param path the file instance to use
 * @param ignoreCase if true then the case is ignored when checking for paths
 */
fun java.io.File.logicalPath(path: String, ignoreCase: Boolean = false): String {
    val thisPath = canonicalPath.trim().trimEnd(java.io.File.separatorChar)
    val filePath = path.trim().trimEnd(java.io.File.separatorChar)

    return if (filePath.startsWith(thisPath, ignoreCase)) {
        filePath.substring(thisPath.length).trimStart(java.io.File.separatorChar)
    } else {
        filePath
    }
}

/**
 * Strips the [extension] from this [File][java.io.File], if found, and returns it as a
 * [File][java.io.File] instance.
 *
 * @param extension the extension to strip, with or without an initial dot
 * @param ignoreCase if true then the case is ignored when checking for the extension
 */
fun java.io.File.stripExtension(extension: String, ignoreCase: Boolean = false): java.io.File {
    val normalizedExtension = if (extension.startsWith(".")) extension else ".$extension"
    val normalizedPath = canonicalPath
    val strippedPath = if (normalizedPath.endsWith(normalizedExtension, ignoreCase)) {
        normalizedPath.substring(0, normalizedPath.length - normalizedExtension.length)
    } else {
        normalizedPath
    }

    return java.io.File(strippedPath)
}

/**
 * Returns true if provided [file] resides within this [File][java.io.File], false otherwise.
 *
 * @param file the file instance to use
 * @param ignoreCase if true then the case is ignored when checking for paths
 */
fun java.io.File.contains(file: java.io.File, ignoreCase: Boolean = false): Boolean =
    contains(file.canonicalPath, ignoreCase)

/**
 * Returns true if provided [path] resides within this [File][java.io.File], false otherwise.
 *
 * @param path the file instance to use
 * @param ignoreCase if true then the case is ignored when checking for paths
 */
fun java.io.File.contains(path: String, ignoreCase: Boolean = false): Boolean {
    val thisPath = canonicalPath.trim().trimEnd(java.io.File.separatorChar)
    val filePath = path.trim().trimEnd(java.io.File.separatorChar)

    return filePath.startsWith(thisPath, ignoreCase)
}
