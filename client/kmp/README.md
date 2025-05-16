# Keyple Kotlin Multiplatform Distributed Client Demo App

## Overview

The **Keyple Distributed Client KMP Demo App** is a Kotlin Multiplatform app demonstrating distributed remote
client communications across Android, iOS and desktop platforms. Its purpose is to demonstrate the use of 
Keyple Distributed Client KMP libraries, that provide a distributed architecture layer
for remote terminals, making it easier to develop cross-platform applications connecting to a Keyple server.

It shows how to load contracts into a Calypso card, the whole ticketing process being managed remotely.
Following the contract loading the card can pay presented to a validator running the
[Keyple Demo Validation](https://github.com/calypsonet/keyple-demo-ticketing-validation-app) application and then checked with
the [Keyple Demo Control](https://github.com/calypsonet/keyple-demo-ticketing-control-app) application.

Read the main [README](https://github.com/calypsonet/keyple-demo-ticketing-reloading-remote#readme) to understand the purpose of this application.

## Documentation & Contribution Guide
Full documentation available at [keyple.org](https://keyple.org)

## Supported Platforms
- Android 7.0+ (API 24+)
- iOS 14+
- JVM 17+

## Using the demo app on a mobile device
You need a Calypso card compatible with the keyple demo server. Ensure the card has the appropriate application installed and is suited to the SAM you have connected (Test or Prod keys).

If you host your own keyple demo server, customize its URL on the app using the "Settings" icon.

To setup your compatible cards use the "Settings" -> "Personalize" screen.

Then, you can use "Main screen" -> "Contactless support" to read the card.

The mobile device will start its NFC reader, and start hunting for a compatible NFC card.

When detected, it will connect to the Keyple server, establish a secure session and read the card content.

You can then select a new title to load, simulate a payment flow, and present the NFC card again to write the title onto it
thought another secure session, driven by the remote Keyple server connected to a SAM.

## Build
The code is built with **Gradle** and targets **Android**, **iOS**, and **JVM** platforms.
A recommended workflow is to checkout locally the "keyple-less" dependencies of this demo and publish them to your local maven repository.

See the README in each projects:

https://github.com/eclipse-keyple/keypleless-distributed-client-kmp-lib 
https://github.com/eclipse-keyple/keypleless-reader-nfcmobile-kmp-lib

You can then use Android Studio to build the apps.

### Android App
Android app should build and run out of the box.

### iOS App
The easiest way is generally to start by opening the iosApp/iosApp.xcodeproj file in XCode, and configure 
the app in "Signing and Capabilities" to allow "Automatically manage signing" and ensure your development iPhone is properly detected and ready to use.

Once successfully configured, you should be able to use AndroidStudio to run your iOS app on your iPhone. 

### desktop App
*Prerequisite*: The desktop app needs a PCSC reader connected to the computer. Ensure PCSC and "java" are properly configured on your computer to 
allow java programs to access your USB connected NFC reader. It's generally easy to do on Windows or Mac, not so much on Linux.

In AndroidStudio, create a new "Run/Debug configuration", choose "Gradle", and add a "Run" command:

```
desktopRun -DmainClass=org.calypsonet.keyple.demo.reload.remote.MainKt -PcustomArgs="-filter=ACS"
```

where "-filter=" allows you to filter by name for the right PCSC reader to use (An "ACS" reader in this example).

In "Gradle project", browse to "kmp:composeApp" module.

Save this configuration, and use it to run the app.

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
