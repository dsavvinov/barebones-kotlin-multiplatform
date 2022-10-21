package org.jetbrains.kotlin.tasks

import org.jetbrains.kotlin.hardcode.HardcodedPaths
import org.jetbrains.kotlin.jarOutputStream
import java.util.jar.JarEntry
import kotlin.io.path.relativeTo

object PackCommon : KmpBuildTask("PACK_COMMON") {
    override fun execute(paths: HardcodedPaths) {
        val jarDestination = paths.Outputs().Packed().commonJar
        jarDestination.delete()
        jarDestination.parentFile.mkdirs()

        jarDestination.jarOutputStream().use { os ->
            val root = paths.Outputs().AssembledBinaries().commonMain
            val rootPath = root.toPath()
            root.walk().filter { it.isFile }.forEach { file ->
                os.putNextEntry(JarEntry(file.toPath().relativeTo(rootPath).toString()))
                os.write(file.readBytes())
            }
            os.finish()
        }
    }
}
