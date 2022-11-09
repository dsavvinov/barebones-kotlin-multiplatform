package org.jetbrains.kotlin.hardcode

import org.jetbrains.kotlin.repositoryRoot
import java.io.File

object HardcodedToolchainsPaths {
    private val tools = File(repositoryRoot, "buildInfra/tools")

    private val kotlinDist = File(tools, "kotlinc")
    private val kotlincDistBin = File(kotlinDist, "bin")
    private val kotlincDistLib = File(kotlinDist, "lib")

    // kotlinc executables
    val kotlinc = File(kotlincDistBin, "kotlinc")
    val kotlincJvm = File(kotlincDistBin, "kotlinc-jvm")
    val kotlincMetadata = File(kotlincDistBin, "kotlinc-metadata")

    // kotlin-native dist: distributed separately for semi-historical reasons
    private val kotlinNativeDist = File(tools, "kotlin-native")
    private val kotlinNativeDistBin = File(kotlinNativeDist, "bin")
    val kotlincNative = File(kotlinNativeDistBin, "kotlinc-native")

    // stdlibs
    val kotlinStdlibJvm = File(kotlincDistLib, "kotlin-stdlib.jar") // NB: JVM stdlib has no suffix for legacy reasons
    val kotlinStdlibJdk7 = File(kotlincDistLib, "kotlin-stdlib-jdk7.jar") // NB: JVM stdlib has separate JDK 7 and JDK 8 jars
    val kotlinStdlibJdk8 = File(kotlincDistLib, "kotlin-stdlib-jdk8.jar")
    val kotlinStdlibCommon = File(kotlincDistLib, "kotlin-stdlib-common.jar")
}
