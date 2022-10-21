package org.jetbrains.kotlin

import org.jetbrains.kotlin.hardcode.DirectDependency
import org.jetbrains.kotlin.hardcode.TransitiveDependency
import org.jetbrains.kotlin.tasks.KmpBuildTask

fun main(args: Array<String>) {
    fun error(message: String) {
        println("Error: $message\n\n")
        printUsage()
    }

    val projectName = args.firstOrNull() ?: return error("No project specified")
    val taskName = args.getOrNull(1) ?: return error("No task specified")
    if (args.size > 2) {
        return error("Extra unexpected arguments specified\n" +
                "   '${args.drop(2).joinToString(separator = " ")}'")
    }


    val projectPaths = when (projectName) {
        "DIRECT" -> DirectDependency
        "TRANSITIVE" -> TransitiveDependency
        else -> return error("Unknown project $projectName")
    }

    val task = KmpBuildTask.allTasks.firstOrNull { it.taskName == taskName }
        ?: return error("Unknown task $taskName")

    task.execute(projectPaths)
}

fun printUsage() {
    println("""
        Usage:
            kmpBuilder <PROJECT> <TASK>
        
        Available projects:
            DIRECT - tasks related to 'sources/direct'
            TRANSITIVE - tasks related to 'sources/transitive'
        
        Available tasks:
            ASSEMBLE_COMMON - invokes K/Common compiler, produces .kotlin_metadata in <repo-root>/out/assembly/common(Main|Test)
            ASSEMBLE_JVM    - invokes K/JVM compiler, produces .classfiles in <repo-root>/out/assembly/jvm(Main|Test)
            ASSEMBLE_IOS_DEVICE - invokes K/Native compiler, produces TODO in <repo-root>/out/assembly/iosDeviceArm64(Main|Test)
            ASSEMBLE_IOS_INTEL_SIM - invokes K/Native compiler, produces TODO in <repo-root>/out/assembly/iosSimulatorX64(Main|Test)
            ASSEMBLE_IOS_ARM_SIM - invokes K/Native compiler, produces TODO in <repo-root>/out/assembly/iosSimulatorArm64(Main|Test)
            
            PACK_COMMON - packs .jar file from <repo-root>/out/jvm into <repo-root>/out/packed/common.jar
            PACK_JVM - packs .jar file from <repo-root>/out/jvm into <repo-root>/out/packed/jvm.jar
            PACK_NATIVE - packs XCFramework from <repo-root>/out/ios(.*) into ../buid/packed/XCFramework
            
            BUILD - runs all tasks above sequentially, 
    """.trimIndent())
}
