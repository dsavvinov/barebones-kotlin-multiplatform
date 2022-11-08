package org.jetbrains.kotlin

import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import kotlin.io.path.relativeTo

fun File.assertExists(): File {
    require(exists()) { "File $absolutePath not found" }
    return this
}

fun <K, V> Iterable<Pair<K, V>>.toMutlivalueMap(): Map<K, List<V>> {
    return this.groupBy { it.first }.mapValues { it.value.map { it.second } }
}

fun File.jarOutputStream(): JarOutputStream = JarOutputStream(outputStream())

/**
 * Creates a *new* JAR-file at [destination] with [sources]
 * being placed at the top-level. If [sources] is a directory,
 * all its content packed as well.
 */
fun packJar(destination: File, sources: List<File>) {
    destination.delete()
    destination.parentFile.mkdirs()

    destination.jarOutputStream().use { os ->
        for (root in sources) {
            val rootPath = root.toPath()
            root.walk().filter { it.isFile }.forEach { file ->
                os.putNextEntry(JarEntry(file.toPath().relativeTo(rootPath).toString()))
                os.write(file.readBytes())
            }
            os.finish()
        }
    }
}
