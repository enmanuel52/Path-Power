// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.nl.littlerobots.version.catalog.update)
    kotlin("plugin.serialization") version "1.9.22" apply false
    id("com.android.test") version "8.2.1" apply false
    id("androidx.baselineprofile") version "1.2.2" apply false
}