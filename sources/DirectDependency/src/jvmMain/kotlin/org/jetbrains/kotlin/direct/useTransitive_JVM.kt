package org.jetbrains.kotlin.direct

import org.jetbrains.kotlin.transitive.TransitiveJvm

fun useTransitiveJvm() {
    println(TransitiveJvm().value)
}
