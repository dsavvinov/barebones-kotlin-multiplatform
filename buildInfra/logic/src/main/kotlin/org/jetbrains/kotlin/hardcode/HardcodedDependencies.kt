package org.jetbrains.kotlin.hardcode

import org.jetbrains.kotlin.toMutlivalueMap
import java.io.File

/**
 * Hardcoded dependencies of MPP libraries. In the simplest non-Gradle prototype,
 * all of them will have to be specified manually in build files
 *
 * Modelled as Map<File, File> or Map<File, List<File>>
 * Keys are paths to source-roots of consumer (like, `~/mpp-without-gradle/sources/DirectDependency/commonMain`)
 * Values are paths to the dependency(-ies) root(s)
 */
object HardcodedDependencies {

    val dependencies: Map<File, List<File>> = listOf(
        /**
         * DirectDependency depends on TransitiveDependency -> respective platform sources of DirectDependency
         * see respective platform output of TransitiveDependency
         *
         * The dependency itself is usually declared by users, but in Gradle they just specify the dependency
         * on TransitiveDependency as the whole, and Kotlin Gradle Plugin together with Gradle infers the
         * granular dependencies.
         */
        DirectDependency.Sources().commonMain            to TransitiveDependency.Outputs().Packed().commonJar,
        DirectDependency.Sources().jvmMain               to TransitiveDependency.Outputs().Packed().jvmJar,
        DirectDependency.Sources().iosArm64Main          to TransitiveDependency.Outputs().AssembledBinaries().iosArm64Main,
        DirectDependency.Sources().iosX64Main            to TransitiveDependency.Outputs().AssembledBinaries().iosX64Main,
        DirectDependency.Sources().iosSimulatorArm64Main to TransitiveDependency.Outputs().AssembledBinaries().iosSimulatorArm64Main,

        /**
         * All Kotlin sources need Kotlin Stdlib to be functional.
         *
         * Users usually don't specify this, Kotlin Gradle Plugin adds it by default
         */
        DirectDependency.Sources().commonMain to HardcodedToolchainsPaths.kotlinStdlibCommon,
        TransitiveDependency.Sources().commonMain to HardcodedToolchainsPaths.kotlinStdlibCommon,
        // NB: JVM stdlib consists of several parts
        DirectDependency.Sources().jvmMain to HardcodedToolchainsPaths.kotlinStdlibJvm,
        DirectDependency.Sources().jvmMain to HardcodedToolchainsPaths.kotlinStdlibJdk7,
        DirectDependency.Sources().jvmMain to HardcodedToolchainsPaths.kotlinStdlibJdk8,
        TransitiveDependency.Sources().jvmMain to HardcodedToolchainsPaths.kotlinStdlibJvm,
        TransitiveDependency.Sources().jvmMain to HardcodedToolchainsPaths.kotlinStdlibJdk7,
        TransitiveDependency.Sources().jvmMain to HardcodedToolchainsPaths.kotlinStdlibJdk8,

        // NB: kotlin-native compiler pulls the stdlib from its distribution and doesn't require
        // to pass the path to the stdlib explicitly, so we don't declare that dependency here
    ).toMutlivalueMap()

    /**
     * Hardcoding *Test -> *Main dependencies
     *
     * Users don't do it manually (barring advanced cases with custom test-like sources, like "benchmarks",
     * "functionalTests", etc.). Kotlin Gradle Plugin sets this up under the hood
     */
    val friendDependencies: Map<File, File> = mapOf(
        DirectDependency.Sources().commonTest to DirectDependency.Outputs().Packed().commonJar,
        DirectDependency.Sources().jvmTest to DirectDependency.Outputs().Packed().jvmJar,

        TransitiveDependency.Sources().commonTest to TransitiveDependency.Outputs().Packed().commonJar,
        TransitiveDependency.Sources().jvmTest to TransitiveDependency.Outputs().Packed().jvmJar,
    )

    /**
     * Hardcoding dependsOn-graph
     *
     * If user creates their own source set, it's up to them to include that source set in a dependsOn-graph
     * properly. This is fairly advanced, and usually, Kotlin Gradle Plugin is able to create source sets and link
     * them with dependsOn for popular cases.
     *
     * Also note, that if users ever specify dependsOn by hand, they only declare direct dependsOn edges, but not
     * transitive ones, e.g. iosArm64Main -> iosMain, but not iosArm64Main -> commonMain. Compiler needs the full
     * clojure, which is usually built by build tools. Here we specify the full clojure.
     */
    val dependsOn: Map<File, List<File>> =
        DirectDependency.Sources().hardcodedDependsOnGraph() + TransitiveDependency.Sources().hardcodedDependsOnGraph()

    private fun HardcodedPaths.Sources.hardcodedDependsOnGraph(): Map<File, List<File>> = mapOf(
        jvmMain               to listOf(commonMain),
        iosMain               to listOf(commonMain),
        iosX64Main            to listOf(iosMain, commonMain),
        iosArm64Main          to listOf(iosMain, commonMain),
        iosSimulatorArm64Main to listOf(iosMain, commonMain),

        jvmTest               to listOf(commonTest),
        iosTest               to listOf(commonTest),
        iosX64Test            to listOf(iosTest, commonTest),
        iosArm64Test          to listOf(iosTest, commonTest),
        iosSimulatorArm64Test to listOf(iosTest, commonTest),
    )
}
