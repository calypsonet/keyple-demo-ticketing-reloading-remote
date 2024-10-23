plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript {
    dependencies {
        classpath(libs.spotless.plugin.gradle)
        classpath("org.eclipse.keyple:keyple-gradle:0.2.+") { isChanging = true }
        classpath(libs.jaxb.api)
        classpath(libs.jaxb.impl)
    }
}