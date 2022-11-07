package org.jetbrains.kotlin.transitive

actual class TransitiveExpect {
    actual val value: String = "Transitive, iOS"

    fun extraIosApi() { }
}
