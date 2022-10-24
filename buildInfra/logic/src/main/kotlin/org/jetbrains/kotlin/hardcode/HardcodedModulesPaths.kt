package org.jetbrains.kotlin.hardcode

import java.io.File

/**
 * Paths are hardcoded for the sake of simplifying this example
 */

val TransitiveDependency = HardcodedPaths("TransitiveDependency")
val DirectDependency = HardcodedPaths("DirectDependency")

class HardcodedPaths(val libName: String) {
    private val repoRoot = File("../../")
    private val sources = File(repoRoot, "sources")

    inner class Sources {
        val sharedKmmSources = File(sources, "$libName/src")

        val commonMain = File(sharedKmmSources, "commonMain")
        val commonTest = File(sharedKmmSources, "commonTest")

        val jvmMain = File(sharedKmmSources, "jvmMain")
        val jvmTest = File(sharedKmmSources, "jvmTest")

        val iosMain = File(sharedKmmSources, "iosMain")
        val iosTest = File(sharedKmmSources, "iosTest")

        // "Leaf" source sets of K/N Targets
        //
        // Their sources are empty in the currently present example.
        // Source sets added to be able to specify dependencies properly in
        // [HardcodedDependencies]
        //
        // It was possible to not specify dependencies for them properly, and handle
        // that directly in [AssembleNative], but that lead to quite intricate
        // and hard-to-notice quirks and hurts readability of the code, so I decided
        // against that
        val iosArm64Main = File(sharedKmmSources, "iosArm64Main")
        val iosArm64Test = File(sharedKmmSources, "iosArm64Test")

        val iosX64Main = File(sharedKmmSources, "iosX64Main")
        val iosX64Test = File(sharedKmmSources, "iosX64Test")

        val iosSimulatorArm64Main = File(sharedKmmSources, "iosSimulatorArm64Main")
        val iosSimulatorArm64Test = File(sharedKmmSources, "iosSimulatorArm64Test")
    }

    val outDir = File("$repoRoot/buildInfra", "output")

    inner class Outputs {
        /**
         * Assembly is the direct output of Kotlin Compiler
         */
        inner class AssembledBinaries {
            val assembly = File(outDir, "$libName/assembly")

            val commonMain = File(assembly, "commonMain")
            val commonTest = File(assembly, "commonTest")

            val iosDeviceArm64Main = File(assembly, "iosDeviceArm64Main")
            val iosDeviceArm64Test = File(assembly, "iosDeviceArm64Test")
            val iosSimX64Main = File(assembly, "iosSimX64Main")
            val iosSimX64Test = File(assembly, "iosSimX64Test")
            val iosSimArm64Main = File(assembly, "iosSimArm64Main")
            val iosSimArm64Test = File(assembly, "iosSimArm64Test")

            val jvmMain = File(assembly, "jvmMain")
            val jvmTest = File(assembly, "jvmTest")
        }

        inner class Packed {
            val packed = File(outDir, "$libName/packed")

            val commonJar = File(packed, "common.jar")

            val jvmJar = File(packed, "jvm.jar")

            /**
             * We distribute two artifacts for native:
             * - klib contains information about declarations and their bodies in Kotlin-specific binary format
             *   Because the format is Kotlin-specific, only Kotlin can consume those libraries
             *
             * - NativeFramework is a usual iOS framework, linked from respective KLib, containing Obj-C code and
             *   Swift Headers. It is consumable by native iOS clients.
             */
            val nativeKlib = File(packed, "native.klib")
            val nativeFramework = File(packed, "nativeFramework")
        }
    }
}
