package org.jetbrains.kotlin.tasks

import org.jetbrains.kotlin.hardcode.HardcodedPaths

sealed class KmmProjectBuildTask(val taskName: String) {
    abstract fun execute(paths: HardcodedPaths)

    companion object {
        val allTasks = listOf(
            // Assemble: launching compiler, getting output as is
            AssembleCommon,
            AssembleJvm,

            AssembleNativeDebugIosArm64,
            AssembleNativeDebugIosX64,
            AssembleNativeDebugIosSimulatorArm64,

            AssembleNativeReleaseIosArm64,
            AssembleNativeReleaseIosX64,
            AssembleNativeReleaseIosSimulatorArm64,
            
            // Packing/linking: transforming compiler output into format which is consumable
            PackCommon,
            PackJvm,

            LinkNativeIosArm64Debug,
            LinkNativeIosX64Debug,
            LinkNativeIosSimulatorArm64Debug,

            LinkNativeIosArm64Release,
            LinkNativeIosX64Release,
            LinkNativeIosSimulatorArm64Release,
        )
    }
}
