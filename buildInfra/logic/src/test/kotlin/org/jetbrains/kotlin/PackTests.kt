package org.jetbrains.kotlin

import org.junit.jupiter.api.Test

class PackTests {
    class Common {
        @Test
        fun `pack - TransitiveDependency - Common`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_COMMON"))
            main(arrayOf("TRANSITIVE", "PACK_COMMON"))
        }

        @Test
        fun `pack - DirectDependency - Common`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_COMMON"))
            main(arrayOf("TRANSITIVE", "PACK_COMMON"))
            main(arrayOf("DIRECT", "ASSEMBLE_COMMON"))
            main(arrayOf("DIRECT", "PACK_COMMON"))
        }
    }

    class Jvm {
        @Test
        fun `pack - TransitiveDependency - JVM`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_JVM"))
            main(arrayOf("TRANSITIVE", "PACK_JVM"))
        }

        @Test
        fun `pack - DirectDependency - JVM`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_COMMON"))
            main(arrayOf("TRANSITIVE", "PACK_COMMON"))
            main(arrayOf("DIRECT", "ASSEMBLE_COMMON"))
            main(arrayOf("DIRECT", "PACK_COMMON"))
        }
    }

    class Native {
        @Test
        fun `link - TransitiveDependency - Native - Debug - iosArm64`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_IOS_ARM64_DEBUG"))
            main(arrayOf("TRANSITIVE", "LINK_IOS_ARM64_DEBUG"))
        }

        @Test
        fun `link - DirectDependency - Native - Release - iosSimulatorArm64`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_IOS_SIMULATOR_ARM64_RELEASE"))
            main(arrayOf("DIRECT", "ASSEMBLE_IOS_SIMULATOR_ARM64_RELEASE"))
            main(arrayOf("DIRECT", "LINK_IOS_SIMULATOR_ARM64_RELEASE"))
        }
    }
}
