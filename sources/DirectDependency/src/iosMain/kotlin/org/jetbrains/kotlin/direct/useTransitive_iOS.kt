package org.jetbrains.kotlin.direct

import org.jetbrains.kotlin.transitive.TransitiveExpect

fun consumeTransitiveExpectInIOS(tc: TransitiveExpect) {
    // Common API is callable, and returns whatever value is specified in respective (iOS, here) 'actual'-implementation
    require(tc.value == "Transitive, iOS")

    // iOS-specific API is visible, because we're looking at expect-class from iOS perspective
    tc.extraIosApi()
}
