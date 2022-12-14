package org.jetbrains.kotlin.hardcode

import org.jetbrains.kotlin.repositoryRoot
import java.io.File

val TransitiveDependency = HardcodedPaths("TransitiveDependency")
val DirectDependency = HardcodedPaths("DirectDependency")

/**
 * Philosophical notice.
 *
 * A shrewd reader might see "HardcodedPaths" name as an unnecessarily humble one,
 * and suggest something more ambitious, like "(Hardcoded)KotlinModule"
 *
 * It is indeed a correct direction for a thought. However, introducing a concept like
 * "Kotlin Module" requires a proper specification, which is surprisingly non-trivial:
 * one has to then talk about compilation units ("Kotlin Module" is not a compilation unit,
 * it's a bigger abstraction), dependency resolution, inner structure of a "Kotlin Module"
 * (what is "commonMain" and "jvmMain"? What is "main", actually?), etc.
 *
 * Therefore, this specific example consciously omits this whole discussion.
 */
class HardcodedPaths(val libName: String) {
    private val sources = File(repositoryRoot, "sources")

    inner class Sources {
        val sharedKmmSources = File(sources, "$libName/src")

        val commonMain = File(sharedKmmSources, "commonMain")
        val commonTest = File(sharedKmmSources, "commonTest")

        val jvmMain = File(sharedKmmSources, "jvmMain")
        val jvmTest = File(sharedKmmSources, "jvmTest")

        val iosMain = File(sharedKmmSources, "iosMain")
        val iosTest = File(sharedKmmSources, "iosTest")

        /**
         * "Leaf" source sets of K/N Targets
         *
         * Their sources are empty in the currently present example.
         * Those source sets are added here to be able to specify dependencies properly in
         * [HardcodedDependencies]
         *
         * It was possible to not specify dependencies for them properly, and handle
         * that directly in [AssembleNative], but that lead to quite intricate
         * and hard-to-notice quirks and hurts readability of the code, so I decided
         * against that
         */
        val iosArm64Main = File(sharedKmmSources, "iosArm64Main")
        val iosArm64Test = File(sharedKmmSources, "iosArm64Test")

        val iosX64Main = File(sharedKmmSources, "iosX64Main")
        val iosX64Test = File(sharedKmmSources, "iosX64Test")

        val iosSimulatorArm64Main = File(sharedKmmSources, "iosSimulatorArm64Main")
        val iosSimulatorArm64Test = File(sharedKmmSources, "iosSimulatorArm64Test")

        fun mainNativeSourceSetForTarget(kotlinNativeTargetName: String) = when (kotlinNativeTargetName) {
            "ios_arm64" -> iosArm64Main
            "ios_X64" -> iosX64Main
            "ios_simulator_arm64" -> iosSimulatorArm64Main
            else -> error("Unknown Kotlin/Native target $kotlinNativeTargetName")
        }
    }

    val outDir = File("$repositoryRoot/buildInfra", "output")

    inner class Outputs {
        inner class AssembledBinaries {
            val assembly = File(outDir, "$libName/assembly")

            val commonMain = File(assembly, "commonMain")
            val commonTest = File(assembly, "commonTest")

            val iosArm64Main = File(assembly, "iosArm64Main.klib")
            val iosX64Main = File(assembly, "iosX64Main.klib")
            val iosSimulatorArm64Main = File(assembly, "iosSimulatorArm64Main.klib")

            val jvmMain = File(assembly, "jvmMain")
            val jvmTest = File(assembly, "jvmTest")

            fun mainKlibForNativeTarget(kotlinNativeTargetName: String) = when (kotlinNativeTargetName) {
                "ios_arm64" -> iosArm64Main
                "ios_X64" -> iosX64Main
                "ios_simulator_arm64" -> iosSimulatorArm64Main
                else -> error("Unknown Kotlin/Native target $kotlinNativeTargetName")
            }
        }

        inner class Packed {
            val packed = File(outDir, "$libName/packed")

            val commonJar = File(packed, "common.jar")

            val jvmJar = File(packed, "jvm.jar")

            /**
             * IMPORTANT NOTICE
             *
             * Paths below are hardcoded, but they can not be chosen entirely arbitrary. They are used on Xcode
             * side, in "Build Settings -> Framework Search Paths" to let the Xcode find the frameworks.
             * Hardcoded values are correct as of Nov 2022, Xcode 14, Kotlin 1.7.20, but might become outdated later
             * (most likely with Xcode updates, and most likely in the number "16.0")
             *
             * Read the respective section about Xcode Frameworks in docs for details.
             */
            val iosArm64DebugFramework = File(packed, "frameworks/Debug/iphonedevice16.0/$libName.framework")
            val iosArm64ReleaseFramework = File(packed, "frameworks/Release/iphonedevice16.0/$libName.framework")
            val iosSimulatorArm64DebugFramework = File(packed, "frameworks/Debug/iphonesimulator16.0/$libName.framework")
            val iosSimulatorArm64ReleaseFramework = File(packed, "frameworks/Release/iphonesimulator16.0/$libName.framework")

            fun frameworkForNativeTarget(kotlinNativeTargetName: String, debug: Boolean) = when (kotlinNativeTargetName) {
                "ios_arm64" -> if (debug) iosArm64DebugFramework else iosArm64ReleaseFramework
                "ios_simulator_arm64" -> if (debug) iosSimulatorArm64DebugFramework else iosSimulatorArm64ReleaseFramework
                else -> error("Unknown Kotlin/Native target $kotlinNativeTargetName")
            }
        }
    }
}
