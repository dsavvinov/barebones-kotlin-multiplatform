package com.example.androidapp.android

import org.jetbrains.kotlin.direct.consumeTransitiveExpectInCommon
import org.jetbrains.kotlin.direct.consumeTransitiveExpectInJvm
import org.jetbrains.kotlin.transitive.TransitiveExpect
import org.jetbrains.kotlin.transitive.TypealiasExpansion

fun use() {
    // Common API from TransitiveDependency is visible
    val tc = TransitiveExpect()
    println(tc.value)
    // JVM-specific API from TransitiveDependency is visible
    tc.a()

    // Common API from DirectDependency is visible
    consumeTransitiveExpectInCommon(tc)
    // JVM API from DirectDependency is visible
    consumeTransitiveExpectInJvm(tc)
    // JVM-specific typealias are expanded properly
    // (a.k.a. on JVM we know that "TransitiveExpect" and "TypealiasExpansion"
    // are same types
    consumeTransitiveExpectInJvm(TypealiasExpansion())
}