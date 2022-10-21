package org.jetbrains.kotlin.tools

import org.jetbrains.kotlin.assertExists
import org.jetbrains.kotlin.hardcode.HardcodedToolchainsPaths

object Kotlinc : AbstractTool(HardcodedToolchainsPaths.kotlinc.assertExists())

object KotlincJvm : AbstractTool(HardcodedToolchainsPaths.kotlincJvm.assertExists())

object KotlincNative : AbstractTool(HardcodedToolchainsPaths.kotlincNative.assertExists())

object KotlincMetadata : AbstractTool(HardcodedToolchainsPaths.kotlincMetadata.assertExists())
