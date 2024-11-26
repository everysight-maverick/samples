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
[Compose](./compose) | Jetpack Compose App example
[AR](./Ar) | LOS (Line fo sight) example

## Using the samples

Open the samples folder with Android Studio:

- Clone\Download this repository to your pc
- **For each sample** add your `sdk.key` to the `assets` folder
- In `settings.gradle` update the following:
  - Create GitHub token with scope `read:packages` ([click here to generate a token](https://github.com/settings/tokens/new))
  - Add your token information in the credentials block
  
  ``` kotlin
  credentials {
    username = "YOUR_PERSONAL_GITHUB_USER_NAME"
    password = "YOUR_PERSONAL_GITHUB_USER_TOKEN"
  }
  ```
  - Additional information can be found [here](https://everysight.github.io/maverick_docs/libraries-api/android/)
    

## More information

More information about Android application setup can be found in our [developers portal](https://everysight.github.io/maverick_docs/libraries-api/overview/)

### Note

Before launching a sample **make sure the previous one is closed** (only one application can be connected to the glasses simultaneously)


