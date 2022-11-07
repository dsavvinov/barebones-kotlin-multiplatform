package org.jetbrains.kotlin.tasks

import org.jetbrains.kotlin.hardcode.HardcodedDependencies
import org.jetbrains.kotlin.hardcode.HardcodedPaths
import org.jetbrains.kotlin.tools.KotlincNative
import java.io.File

abstract class AssembleNative(
    private val kotlinNativeTargetName: String,
    taskName: String,
    private val debug: Boolean
) : KmpProjectBuildTask(taskName) {
    override fun execute(paths: HardcodedPaths) {
        assembleSourceSet(
            paths.Sources().mainNativeSourceSetForTarget(kotlinNativeTargetName),
            paths.Outputs().AssembledBinaries().mainKlibForNativeTarget(kotlinNativeTargetName),
            paths.libName
        )
    }

    private fun assembleSourceSet(sourceSetRoot: File, outputDir: File, libraryName: String) {
        outputDir.deleteRecursively()

        val dependencies = HardcodedDependencies.dependencies[sourceSetRoot].orEmpty()
        val friendDependencies = HardcodedDependencies.friendDependencies[sourceSetRoot]
        val dependsOn = HardcodedDependencies.dependsOn[sourceSetRoot]

        val arguments = buildList<String> {
            if (debug) {
                add("-g")  // Enable debug mode. This will turn off some optimizations and will generate
                           // additional debug symbols and metainformation. This makes the code larger and slower,
                           // but more debuggable
                add("-ea") // Enables assertions - `kotlin.assert()` will throw exceptions
            } else {
                add("-opt") // Enable optimization mode: aggressive optimizations will be applied, additional
                            // debug metainformation will be reduced to the minimum. Produced code is fast and size
                            // is small, but not very debuggable.
            }

            // Enable multiplatform support
            add("-Xmulti-platform")

            add("-target")
            add(kotlinNativeTargetName)

            // Tell K/N compiler that we need a "library", i.e. redistributable Kotlin-code in form of a .klib,
            // consumable by other Kotlin-clients
            add("-p")
            add("library")

            // Very important property! It has to be unique across all K/N modules linked together, and will
            // affect how Kotlin/Native code will look from ObjC/Swift perspective.
            //
            // If no name specified, K/N will take simple name of the specified "-o" File. This doesn't work for us,
            // as simple name of "-o" both for DirectDependency and TransitiveDependency is the name of K/N target
            // (like, `iosArm64Main`). Therefore, produced klib for TransitiveDependency will have same name as the klib
            // produced for DirectDependency, and K/N Linker will go mad (will complain about cyclic dependency, to be
            // precise).
            //
            // In this example we chose "libraryName" (i.e. "DirectDependency" or "TransitiveDependency"). Note that
            // it's not unique across all produced klibs (both `iosArm64Main.klib` and `iosX64Main.klib` for
            // "DirectDependency" will have same "unique" name for example). This is OK, because they are for different
            // targets and will never be linked together.
            add("-module-name")
            add(libraryName)

            // -Xcommon-sources marks sources as common. Some language constructs (like 'expect'-modifier)
            // are allowed only in sources marked as common
            if (dependsOn != null) add("-Xcommon-sources=${dependsOn.joinToString(separator = ",") { it.canonicalPath }}")

            // Pass friend-paths, if any (tests have them)
            // !! Different argument for K/N
            if (friendDependencies != null) add("-friend-modules=${friendDependencies.canonicalPath}")

            // Dependencies go as `-l`.
            // !! Note that Native's `-l` doesn't accept delimited multi-value argument,
            // so one has to repeat multiple `-l` to pass multiple dependencies
            dependencies.forEach {
                add("-l")
                add(it.canonicalPath)
            }

            // output directory
            // !! Different argument for K/N
            add("-o")
            add(outputDir.canonicalPath)

            // Free args: sources (source roots) to compile. Note that they should be a superset of -Xcommon-sources
            dependsOn.orEmpty().forEach { add(it.canonicalPath) }
            // The next line is commented, because in our examples we don't have sources in target-specific source sets,
            // and the compiler is intolerant to non-existent paths
            // add(sourceSetRoot.canonicalPath)
        }

        KotlincNative.execute(*arguments.toTypedArray())
    }
}

object AssembleNativeDebugIosArm64 : AssembleNative("ios_arm64", "ASSEMBLE_IOS_ARM64_DEBUG", debug = true)
object AssembleNativeDebugIosX64 : AssembleNative("ios_X64", "ASSEMBLE_IOS_X64_DEBUG", debug = true)
object AssembleNativeDebugIosSimulatorArm64 : AssembleNative("ios_simulator_arm64", "ASSEMBLE_IOS_SIMULATOR_ARM64_DEBUG", debug = true)

object AssembleNativeReleaseIosArm64 : AssembleNative("ios_arm64", "ASSEMBLE_IOS_ARM64_RELEASE", debug = false)
object AssembleNativeReleaseIosX64 : AssembleNative("ios_X64", "ASSEMBLE_IOS_X64_RELEASE", debug = false)
object AssembleNativeReleaseIosSimulatorArm64 : AssembleNative("ios_simulator_arm64", "ASSEMBLE_IOS_SIMULATOR_ARM64_RELEASE", debug = false)
