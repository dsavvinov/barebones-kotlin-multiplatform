package org.jetbrains.kotlin.tasks

import org.jetbrains.kotlin.hardcode.DirectDependency
import org.jetbrains.kotlin.hardcode.HardcodedDependencies
import org.jetbrains.kotlin.hardcode.HardcodedPaths
import org.jetbrains.kotlin.hardcode.TransitiveDependency
import org.jetbrains.kotlin.tools.KotlincNative
import java.io.File

/**
 * Links iOS Framework from Kotlin/Native klibs, suitable for consuming it in the iOS-only projects
 */
abstract class LinkNative(
    private val kotlinNativeTargetName: String,
    taskName: String,
    private val debug: Boolean
) : KmpProjectBuildTask(taskName) {
    override fun execute(paths: HardcodedPaths) {
        if (paths === TransitiveDependency) {
            println(
                """
                Warning!
                
                Calling Link-task on TransitiveDependency might be not what you wanted. In short, K/N Framework linking
                produces "fat" binary, which includes API of selected module **along with all its dependencies**. 
                
                This is not an error because the produced framework won't be incorrect, but it has no use in this 
                specific example. In this example, to get a framework for plugging into iOS App, you should call
                Link-task on DirectDependency (it will produce the framework with TransitiveDependency' API included)
            """.trimIndent()
            )
        }

        val source = paths.Sources().mainNativeSourceSetForTarget(kotlinNativeTargetName)
        val sourceKlib = paths.Outputs().AssembledBinaries().mainKlibForNativeTarget(kotlinNativeTargetName)

        linkFramework(
            sourceKlib = sourceKlib,
            dependenciesKlibs = HardcodedDependencies.dependencies[source].orEmpty(),
            destination = paths.Outputs().Packed().frameworkForNativeTarget(kotlinNativeTargetName, debug)
        )
    }

    private fun linkFramework(sourceKlib: File, dependenciesKlibs: List<File>, destination: File) {
        destination.deleteRecursively()

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

            add("-Xmulti-platform")

            add("-target")
            add(kotlinNativeTargetName)

            // Tell K/N compiler that we need an Apple Framework
            add("-p")
            add("framework")

            add("-no-endorsed-libs")

            // One klib (the one resulted from this module) is specified via -Xinclude
            add("-Xinclude=${sourceKlib.canonicalPath}")

            // Dependencies klibs specified via repeating `-l`
            dependenciesKlibs.forEach {
                add("-l")
                add(it.canonicalPath)
            }

            // Destination for framework, format: <path>/<framework-name>.framework
            // Will create <path>/<framework-name>.framework with the framework contents,
            // and <path>/<framework-name>.framework.dSYM with debug symbols
            add("-o")
            add(destination.canonicalPath)
        }

        KotlincNative.execute(arguments)
    }
}

object LinkNativeIosArm64Debug : LinkNative("ios_arm64", "LINK_IOS_ARM64_DEBUG", debug = true)
object LinkNativeIosX64Debug : LinkNative("ios_X64", "LINK_IOS_X64_DEBUG", debug = true)
object LinkNativeIosSimulatorArm64Debug : LinkNative("ios_simulator_arm64", "LINK_IOS_SIMULATOR_ARM64_DEBUG", debug = true)

object LinkNativeIosArm64Release : LinkNative("ios_arm64", "LINK_IOS_ARM64_RELEASE", debug = false)
object LinkNativeIosX64Release : LinkNative("ios_X64", "LINK_IOS_X64_RELEASE", debug = false)
object LinkNativeIosSimulatorArm64Release : LinkNative("ios_simulator_arm64", "LINK_IOS_SIMULATOR_ARM64_RELEASE", debug = false)
