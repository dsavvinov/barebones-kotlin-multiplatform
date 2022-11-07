package org.jetbrains.kotlin.tasks

import org.jetbrains.kotlin.hardcode.HardcodedPaths

sealed class KmpProjectBuildTask(val taskName: String) {
    abstract fun execute(paths: HardcodedPaths)

    companion object {
        val allTasks = listOf(
            AssembleCommon,
            AssembleJvm,
            PackCommon,
            PackJvm,
        )
    }
}
