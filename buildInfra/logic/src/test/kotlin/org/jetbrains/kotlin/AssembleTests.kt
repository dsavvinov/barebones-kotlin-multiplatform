package org.jetbrains.kotlin

import org.junit.jupiter.api.Test

class AssembleTests {

    class Common {
        @Test
        fun `assemble - TransitiveDependency - Common`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_COMMON"))
        }

        @Test
        fun `assemble - DirectDependency - Common`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_COMMON"))
            main(arrayOf("TRANSITIVE", "PACK_COMMON"))
            main(arrayOf("DIRECT", "ASSEMBLE_COMMON"))
        }
    }

    class Jvm {
        @Test
        fun `assemble - TransitiveDependency - JVM`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_JVM"))
        }

        @Test
        fun `assemble - DirectDependency - JVM`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_JVM"))
            main(arrayOf("TRANSITIVE", "PACK_JVM"))
            main(arrayOf("DIRECT", "ASSEMBLE_JVM"))
        }
    }

    class Native {
        @Test
        fun `assemble - TransitiveDependency - Native (iosArm64)`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_IOSARM64"))
        }

        @Test
        fun `assemble - DirectDependency - Native (iosArm64)`() {
            main(arrayOf("TRANSITIVE", "ASSEMBLE_IOSARM64"))
            main(arrayOf("DIRECT", "ASSEMBLE_IOSARM64"))
        }
    }
}
