package org.jetbrains.kotlin

import org.junit.jupiter.api.Test

class AssembleTests {
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
