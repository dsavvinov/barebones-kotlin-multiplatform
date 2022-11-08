package org.jetbrains.kotlin

import org.jetbrains.kotlin.hardcode.DirectDependency
import org.jetbrains.kotlin.hardcode.TransitiveDependency
import org.jetbrains.kotlin.tasks.KmpProjectBuildTask

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

    val task = KmpProjectBuildTask.allTasks.firstOrNull { it.taskName == taskName }
        ?: return error("Unknown task $taskName")

    task.execute(projectPaths)
}

fun printUsage() {
    println("""
        Usage:
            kmpBuilder <PROJECT> <TASK>
        
        Available projects:
            DIRECT - tasks related to 'sources/DirectDependency'
            TRANSITIVE - tasks related to 'sources/TransitiveDependency'
        
        Available tasks:
            ASSEMBLE_COMMON - invokes K/Common compiler, produces .kotlin_metadata 
            ASSEMBLE_JVM    - invokes K/JVM compiler, produces .classfiles 
            ASSEMBLE_<K/N PLATFORM>_<BUILD_TYPE> - invokes K/Native compiler, produces K/N klib
            
            PACK_COMMON - packs .jar file with .kotlin_metadata
            PACK_JVM - packs .jar file with .classfiles     
            LINK_<K/N PLATFORM>_<BUILD_TYPE> - invokes a K/N compiler in linker-mode, produces iOS Framework
         
        Available <K/N PLATFORM> values:
            IOS_ARM64           - iOS 64-bit device (any non-archaic iPhones)
            IOS_X64             - iPhone simulator running on Intel X64 Macs
            IOS_SIMULATOR_ARM64 - iPhone simulator running on M1 Macs
        
        Available <BUILD_TYPE> values:
            DEBUG   - debug build, less optimizations, more same and debuggable produced code
            RELEASE - release build, more aggressive optimizations     
    """.trimIndent())
}
