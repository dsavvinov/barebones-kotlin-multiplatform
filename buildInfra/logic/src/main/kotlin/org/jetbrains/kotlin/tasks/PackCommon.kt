package org.jetbrains.kotlin.tasks

import org.jetbrains.kotlin.hardcode.HardcodedPaths
import org.jetbrains.kotlin.packJar

object PackCommon : KmpProjectBuildTask("PACK_COMMON") {
    override fun execute(paths: HardcodedPaths) {
        packJar(
            destination = paths.Outputs().Packed().commonJar,
            sources = listOf(paths.Outputs().AssembledBinaries().commonMain)
        )
    }
}
