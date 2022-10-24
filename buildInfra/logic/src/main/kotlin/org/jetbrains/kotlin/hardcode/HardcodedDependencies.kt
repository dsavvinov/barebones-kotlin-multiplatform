package org.jetbrains.kotlin.hardcode

import org.jetbrains.kotlin.toMutlivalueMap
import java.io.File

/**
 * Hardcoded dependencies of MPP libraries. In simplest non-Gradle prototype,
 * all of them will have to be specified manually in build files
 *
 * Modelled as Map<File, File> or Map<File, List<File>>
 * Keys are paths to source-roots of consumer (like, `~/mpp-without-gradle/`)
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
            DirectDependency.Sources().commonMain to TransitiveDependency.Outputs().Packed().commonJar,
            DirectDependency.Sources().jvmMain to TransitiveDependency.Outputs().Packed().jvmJar,
            DirectDependency.Sources().iosMain to TransitiveDependency.Outputs().Packed().nativeKlib,

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
            // NB: kotlin-native compiler pulls the stdlib form its distribution and doesn't require
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
            DirectDependency.Sources().iosTest to DirectDependency.Outputs().Packed().nativeKlib,

            TransitiveDependency.Sources().commonTest to TransitiveDependency.Outputs().Packed().commonJar,
            TransitiveDependency.Sources().jvmTest to TransitiveDependency.Outputs().Packed().jvmJar,
            TransitiveDependency.Sources().iosTest to TransitiveDependency.Outputs().Packed().nativeKlib,
    )

    /**
     * Hardcoding dependsOn-graph
     *
     * If user creates their own source set, it's up to them to include that source set in a dependsOn-graph
     * properly. However, Kotlin Gradle Plugin is able to create source sets and link them with dependsOn for
     * some simple/popular cases.
     */
    val dependsOn: Map<File, File> = mapOf(
            DirectDependency.Sources().jvmMain to DirectDependency.Sources().commonMain,
            DirectDependency.Sources().jvmTest to DirectDependency.Sources().commonTest,
            DirectDependency.Sources().iosMain to DirectDependency.Sources().commonMain,
            DirectDependency.Sources().iosTest to DirectDependency.Sources().commonTest,

            TransitiveDependency.Sources().jvmMain to TransitiveDependency.Sources().commonMain,
            TransitiveDependency.Sources().jvmTest to TransitiveDependency.Sources().commonTest,
            TransitiveDependency.Sources().iosMain to TransitiveDependency.Sources().commonMain,
            TransitiveDependency.Sources().iosTest to TransitiveDependency.Sources().commonTest,
    )
}
