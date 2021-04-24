# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepnames class com.google.gson.** {*;}
-keepnames enum com.google.gson.** {*;}
-keepnames interface com.google.gson.** {*;}
-keep class com.google.gson.** { *; }
-keepnames class org.** {*;}
-keepnames enum org.** {*;}
-keepnames interface org.** {*;}
-keep class org.** { *; }

-keepnames class com.thelumierguy.crashwatcher.data.** {*;}
-keepnames enum com.thelumierguy.crashwatcher.data.** {*;}
-keepnames interface com.thelumierguy.crashwatcher.data.** {*;}
-keep class com.thelumierguy.crashwatcher.data.** { *; }
-keep class com.thelumierguy.crashwatcher.CrashWatcher { *; }

-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}