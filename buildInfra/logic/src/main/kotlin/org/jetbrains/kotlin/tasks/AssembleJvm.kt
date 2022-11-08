package org.jetbrains.kotlin.tasks

import org.jetbrains.kotlin.hardcode.HardcodedDependencies
import org.jetbrains.kotlin.hardcode.HardcodedPaths
import org.jetbrains.kotlin.tools.KotlincJvm
import java.io.File

object AssembleJvm : KmmProjectBuildTask("ASSEMBLE_JVM") {
    override fun execute(paths: HardcodedPaths) {
        assembleSourceSet(paths.Sources().jvmMain, paths.Outputs().AssembledBinaries().jvmMain)
    }

    private fun assembleSourceSet(sourceSetRoot: File, outputDir: File) {
        outputDir.deleteRecursively()

        val dependencies = HardcodedDependencies.dependencies[sourceSetRoot].orEmpty()
        // NB: note that the separator is : or ;
        // Other multi-value arguments, like -Xcommon-sources use ',' separator
        val dependenciesString = dependencies.joinToString(File.pathSeparator) { it.canonicalPath }

        val friendDependencies = HardcodedDependencies.friendDependencies[sourceSetRoot]
        val dependsOn = HardcodedDependencies.dependsOn[sourceSetRoot]

        val arguments = buildList<String> {
            // -Xcommon-sources marks sources as common. Some language constructs (like 'expect'-modifier)
            // are allowed only in sources marked as common
            if (dependsOn != null) add("-Xcommon-sources=${dependsOn.joinToString(separator = ",") { it.canonicalPath }}")

            // Enable multiplatform support
            add("-Xmulti-platform")

            // Pass friend-paths, if any (test have them)
            if (friendDependencies != null) add("-Xfriend-paths=${friendDependencies.canonicalPath}")

            // Dependencies go as "classpath"
            add("-classpath")
            add(dependenciesString)

            // output directory
            add("-d")
            add(outputDir.canonicalPath)

            // Free args: sources (source roots) to compile. Note that they should be a superset of -Xcommon-sources
            //
            // !! DIFFERENCE FROM COMMON !!
            //
            // Currently, platform compilers take *sources* of common module as argument, not produced .kotlin_metadata!
            add(sourceSetRoot.canonicalPath)
            dependsOn.orEmpty().forEach { add(it.canonicalPath) }
        }

        KotlincJvm.execute(*arguments.toTypedArray())
    }
}
