package org.jetbrains.kotlin.tools

import java.io.File

abstract class AbstractTool(private val executable: File) {
    fun execute(vararg args: String) {
        println("Invoking ${this::class.simpleName!!} with ${args.joinToString(separator = " ")}")
        ProcessBuilder("./${executable.name}", *args).apply {
            directory(executable.parentFile)
            inheritIO()
        }.start().waitFor()
    }

    fun execute(args: List<String>) = execute(*args.toTypedArray())
}
