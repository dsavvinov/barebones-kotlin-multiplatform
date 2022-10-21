package org.jetbrains.kotlin

import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarOutputStream

fun File.assertExists(): File {
    require(exists()) { "File $absolutePath not found" }
    return this
}

fun <K, V> Iterable<Pair<K, V>>.toMutlivalueMap(): Map<K, List<V>> {
    return this.groupBy { it.first }.mapValues { it.value.map { it.second } }
}

fun File.jarOutputStream(): JarOutputStream = JarOutputStream(outputStream())
