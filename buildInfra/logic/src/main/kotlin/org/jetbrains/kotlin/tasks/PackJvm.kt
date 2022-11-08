package org.jetbrains.kotlin.tasks

import org.jetbrains.kotlin.hardcode.HardcodedPaths
import org.jetbrains.kotlin.packJar

object PackJvm : KmmProjectBuildTask("PACK_JVM") {
    override fun execute(paths: HardcodedPaths) {
        packJar(
            destination = paths.Outputs().Packed().jvmJar,
            sources = listOf(paths.Outputs().AssembledBinaries().jvmMain)
        )
    }
}
