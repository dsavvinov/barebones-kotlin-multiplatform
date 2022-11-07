package org.jetbrains.kotlin.tasks

import org.jetbrains.kotlin.hardcode.HardcodedDependencies
import org.jetbrains.kotlin.hardcode.HardcodedPaths
import org.jetbrains.kotlin.tools.KotlincMetadata
import java.io.File

object AssembleCommon : KmpProjectBuildTask("ASSEMBLE_COMMON") {
    override fun execute(paths: HardcodedPaths) {
        assembleSourceSet(paths.Sources().commonMain, paths.Outputs().AssembledBinaries().commonMain)
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
            add("-Xcommon-sources=${sourceSetRoot.canonicalPath}")

            // Enable multiplatform support
            add("-Xmulti-platform")

            // Pass friend-paths, if any (test have them)
            if (friendDependencies != null) add("-Xfriend-paths=${friendDependencies.canonicalPath}")

            // Pass dependsOn-path a.ka. refines-paths
            if (dependsOn != null) add("-Xcommon-sources=${dependsOn.joinToString(separator = ",") { it.canonicalPath }}")
            // TODO(dsavvinov): this is wrong, fix

            // Dependencies go as "classpath"
            add("-classpath")
            add(dependenciesString)

            // output directory
            add("-d")
            add(outputDir.canonicalPath)

            // Free args: sources (source roots) to compile. Note that they should be a superset of -Xcommon-sources
            add(sourceSetRoot.canonicalPath)
        }

        KotlincMetadata.execute(*arguments.toTypedArray())
    }
}
