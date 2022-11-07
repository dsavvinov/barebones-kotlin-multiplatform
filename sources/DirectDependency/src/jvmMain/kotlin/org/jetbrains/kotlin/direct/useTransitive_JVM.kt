package org.jetbrains.kotlin.direct

import org.jetbrains.kotlin.transitive.TransitiveExpect
import org.jetbrains.kotlin.transitive.TypealiasExpansion

fun consumeTransitiveExpectInJvm(tc: TransitiveExpect) {
    // Common API is callable, and returns whatever value is specified in respective (JVM, here) 'actual'-implementation
    require(tc.value == "Transitive, JVM")

    // JVM-specific API is visible, because we're looking at expect-class from JVM perspective
    tc.a()
    tc.b()

    // We used 'actual typealias' feature, so here we know that 'TransitiveExpect' is the same type as
    // 'TypealiasExpansion'
    val typealiasExpansion: TypealiasExpansion = tc  // OK, assignable
    val tc2: TransitiveExpect = TypealiasExpansion() // OK, assignable
}
