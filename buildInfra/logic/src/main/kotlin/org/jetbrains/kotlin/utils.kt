package org.jetbrains.kotlin

import java.io.File
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

/**
 * Finds the repository root heuristically, by ascending upwards from the current folder
 * up to the file system root, returning the first folder, for which [isRepoRoot] returned [true]
 */
val repositoryRoot: File by lazy {
    val current = File("").absoluteFile
    val parents = generateSequence(current) { it.parentFile }
    val inspectedFolders = mutableListOf<File>()
    for (parent in parents) {
        if (parent.isRepoRoot()) return@lazy parent else inspectedFolders += parent
    }

    // All parents inspected, repo root isn't found, fail with some readable error
    error(
        """
        Error: repository root isn't found
        Current folder = ${current.canonicalPath}
        
        Inspected folders:
        ${inspectedFolders.joinToString(separator = "\n") { it.canonicalPath }}
        
        None of them contained folders 'buildInfra', 'sources' and file 'README.md',
        as expected from the repository root. Check that you're running `kmmBuilder`
        from subtree of `mpp-without-gradle` repository
        """.trimIndent()
    )
}

/**
 * Checks heuristically if [this]-File is repository root, by
 * looking for expected folder/file structure in said root
 */
private fun File.isRepoRoot(): Boolean {
    val buildInfra = File(this, "buildInfra")
    val sources = File(this, "sources")
    val readme = File(this, "README.md")
    return buildInfra.exists() && sources.exists() && readme.exists()
}
