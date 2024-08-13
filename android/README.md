# Android Samples

## Overview

This folder contains the Android samples for the Maverick SDK.

It is recommended to explore, download and run the samples.

## The Samples

It is recommended to learn the samples by the following order:

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

Open the samples folder with Android Studio:

- Clone\Download this repository to your pc
- **For each sample** add the api key certificate to the `assets` folder
- Add **both of the SDK libraries** to the [libs](./libs) folder

## proguard-rules.pro

When enabling minify, please add the following rules to the proguard-rules.pro file:
```
-keepattributes InnerClasses

-keep class io.jsonwebtoken.** { *; }
-keepnames class io.jsonwebtoken.* { *; }
-keepnames interface io.jsonwebtoken.* { *; }

-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type
```

### Note

Before launching a sample **make sure the previous one is closed** (only one application can be connected to the glasses simultaneously)


