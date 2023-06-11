# Android Samples

## Overview

This folder contains the Android samples for the Epsilon Software Developer Kit

It is recommended to learn, download and run the samples


## SDK Libraries

The SDK contains the following libraries

### - NativeEvsKit.jar

The SDK Core, contains all the class and logic required for developing your application

Common for IOS and Android, implemented in Kotlin Native

The Native API documentation can be found [here](../README.md#api-documentation)

### - EvsKit.aar

Contains the SDK (thin) bootstrap for Android, as described [here](#sdk-bootstrap)

The bootstrap initializes the Native library with Android specific functionalities like resources management and bluetooth communication

## SDK Bootstrap

The SDK bootstrap class is a singleton class named `Evs`

The `Evs.instance()` returns the `IEvsApp` which is the entry point for all of the SDK functionalities


### Initializing `Evs`

Before any call to any SDK functionality you must call the `init` and the `start` functions
```kotlin
//init the SDK with an Android context and start its operation
//start can be called in a later stage according to your application flow
Evs.init(context).start()
```

### Setting a Logger

You may set a logger to the SDK to receive log information

You may use the default SDK logger, which writes to the logcat or you may wrote a logger of your own

Start the default logger
```kotlin
//enables logcat logging
Evs.startDefaultLogger()
```
Advanced logging capabilities will be described separately

### Using the app instance

```kotlin
//get the IEvsApp instance
Evs.instance()
```
Please refer to the samples and the api documentation for more information

### Stopping `Evs`

Upon application termination, you should stop the SDK functionality
```kotlin
//stopping the SDK functionality
Evs.instance().stop()
```
## The Samples

For beginners, it is recommended to learn the samples by the following order

Sample | Description
-------|------------
[HelloWorld](./helloworld) | Your first 'hello world' application
[GlassesControl](./GlassesControl) | How to control you glasses
[UIElements](./UIElements) | Adding various ui elements on your screen
[CustomControls](./CustomControls) | Creating custom ui elements
[ImagesHandling](./ImagesHandling) | Using images
[Animations](./Animations) | Performing animations
[OtaHandling](./OtaHandling) | Glasses firmware updates
[Inertial Sensors](./InertialSensors) | Using Inertial Sensors 

## Using the samples

All the samples are built and run using Android Studio. One Gradle project which contains all the samples

- Clone\Download this repository to your pc
- Add the api key certificate to the `assets` folder **of each sample**
- Add **both of the SDK libraries** you got from Everysight to the [libs](./libs) folder

## Notes

- Only one application from the same phone should be connected to the glasses simultaneously, otherwise the glasses display behavior is undefined. Before launching a sample **make sure the previous one is closed**
- Animations won't work properly on old phones with low Bluetooth bandwidth. Bluetooth 5 is recommanded for good animation performance
- Animations will not run during assets uploading to glasses (like images)


