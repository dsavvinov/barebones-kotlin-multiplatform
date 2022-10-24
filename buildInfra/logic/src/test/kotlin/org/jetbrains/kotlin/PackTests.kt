package org.jetbrains.kotlin

import org.junit.jupiter.api.Test

class PackTests {
    @Test
    fun `pack - TransitiveDependency - Common`() {
        main(arrayOf("TRANSITIVE", "ASSEMBLE_COMMON"))
        main(arrayOf("TRANSITIVE", "PACK_COMMON"))
    }

    @Test
    fun `pack - TransitiveDependency - JVM`() {
        main(arrayOf("TRANSITIVE", "ASSEMBLE_JVM"))
        main(arrayOf("TRANSITIVE", "PACK_JVM"))
    }
}
