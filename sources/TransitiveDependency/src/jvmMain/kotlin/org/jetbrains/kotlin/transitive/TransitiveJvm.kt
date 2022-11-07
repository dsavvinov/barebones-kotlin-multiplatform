package org.jetbrains.kotlin.transitive

actual typealias TransitiveExpect = TypealiasExpansion

class TypealiasExpansion {
    val value: String = "Transitive, JVM"

    fun a() { }
    fun b() { }
}
