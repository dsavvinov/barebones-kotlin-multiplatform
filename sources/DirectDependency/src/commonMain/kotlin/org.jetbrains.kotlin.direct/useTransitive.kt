package org.jetbrains.kotlin.direct

import org.jetbrains.kotlin.transitive.TransitiveExpect

fun consumeTransitiveExpectInCommon(tc: TransitiveExpect) {
    println(tc.value)
}
