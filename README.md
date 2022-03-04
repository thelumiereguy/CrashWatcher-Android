# CrashWatcher

### A Custom Uncaught Exception Handler, which can be used to pinpoint crashes, which includes :

- Stacktrace of the crash
- The screens (Activities and Fragments) which lead up to the crash
- The intents and bundle data sent to those screens
- Share the above data to other apps or just copy the text.

</br> 

[![Release](https://img.shields.io/github/v/release/thelumiereguy/CrashWatcher-Android?style=for-the-badge)](https://jitpack.io/#thelumiereguy/CrashWatcher-Android) [![API](https://img.shields.io/badge/API-19%2B-orange?style=for-the-badge)](https://android-arsenal.com/api?level=19) [![Twitter](https://img.shields.io/badge/twitter-thelumiereguy-blue?style=for-the-badge)](https://twitter.com/thelumiereguy)

# Table of Contents

- [Example](#example)
- [Installation](#installation)
- [TODO](#todo)

# Example

[![Iscreen](https://user-images.githubusercontent.com/46375353/92323951-6fb3d200-f05a-11ea-82f3-5b2891bcc6a1.png)]()

# Installation

### You can either put this inside your Application class, or you can use something like AndroidX's [Jetpack Startup Library](https://developer.android.com/topic/libraries/app-startup) to better optimize your app launch times. (You can check the sample here)

```
CrashWatcher.initCrashWatcher(context)  
```

### Gradle setup

```  
repositories {  
 maven { url 'https://jitpack.io' }  
}  
  
dependencies {  
 implementation 'com.github.thelumiereguy:CrashWatcher-Android:2.0.2'
 
 //OR 
 //if you only want to enable this in Debug Builds
 debugImplementation 'com.github.thelumiereguy:CrashWatcher-Android:2.0.2'
}  
```

# TODO

* Add more customizability to the tracking
