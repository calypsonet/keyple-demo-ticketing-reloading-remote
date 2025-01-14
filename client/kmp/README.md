# Keyple Kotlin Multiplatform Distributed Client Demo App

## Overview

The **Keyple Distributed Client KMP Demo App** is a Kotlin Multiplatform app demonstrating distributed remote
client communications across Android, iOS and desktop platforms. Its purpose is to demonstrate the use of 
Keyple Distributed Client KMP libraries, that provide a distributed architecture layer
for remote terminals, making it easier to develop cross-platform applications connecting to a Keyple server.

## Documentation & Contribution Guide
Full documentation available at [keyple.org](https://keyple.org)

## Supported Platforms
- Android 7.0+ (API 24+)
- iOS
- JVM 17+

## Build
The code is built with **Gradle** and targets **Android**, **iOS**, and **JVM** platforms.

## API Documentation

This is a Kotlin Multiplatform project targeting Android, iOS an desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
