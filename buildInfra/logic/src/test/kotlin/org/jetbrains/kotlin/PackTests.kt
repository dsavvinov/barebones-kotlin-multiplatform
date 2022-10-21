package org.jetbrains.kotlin

import org.junit.jupiter.api.Test

class PackTests {
    @Test
    fun `pack - TranstiveDependency - Common`() {
        main(arrayOf("TRANSITIVE", "ASSEMBLE_COMMON"))
        main(arrayOf("TRANSITIVE", "PACK_COMMON"))
    }
}
