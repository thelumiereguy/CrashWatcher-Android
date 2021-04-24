# CrashWatcher

### A Custom Uncaught Exception Handler, which can be used to pinpoint crashe, which includes :
- Stacktrace of the crash
- The screens (Activities and Fragments) which lead up to the crash
- The intents and bundle data sent to those screens
- Share the above data to other apps or just copy the text.

</br> 

[![Release](https://img.shields.io/badge/release-1.5-blue?style=for-the-badge)](https://jitpack.io/#thelumiereguy/CrashWatcher-Android) [![API](https://img.shields.io/badge/API-19%2B-orange?style=for-the-badge)](https://android-arsenal.com/api?level=19) [![Twitter](https://img.shields.io/badge/twitter-thelumiereguy-blue?style=for-the-badge)](https://twitter.com/thelumiereguy)

# Table of Contents  

- [Example](#example)  
- [Installation](#installation)  
- [TODO](#todo)  

# Example  

[![Iscreen](https://user-images.githubusercontent.com/46375353/92323951-6fb3d200-f05a-11ea-82f3-5b2891bcc6a1.png)]()

# Installation  

```gradle  
repositories {  
 maven { url 'https://jitpack.io' }  
}  
  
dependencies {  
 implementation 'com.github.thelumiereguy:CrashWatcher-Android:1.6'
 
 //OR 
 //if you only want to enable this in Debug Builds
 debugImplementation 'com.github.thelumiereguy:CrashWatcher-Android:1.6'
}  
```

# TODO  
* Add more customizability to the tracking
