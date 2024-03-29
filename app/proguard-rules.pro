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
#ReactiveNetwork
-dontwarn com.github.pwittchen.reactivenetwork.library.ReactiveNetwork
-dontwarn io.reactivex.functions.Function
-dontwarn io.reactivex.functions.Action
-dontwarn io.reactivex.functions.Consumer
-dontwarn javax.servlet.ServletContextListener
-dontwarn io.reactivex.CompletableOnSubscribe
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type