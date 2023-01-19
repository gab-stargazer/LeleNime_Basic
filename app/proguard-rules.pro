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

-printconfiguration build/outputs/fullProguardConfig.pro
-dontusemixedcaseclassnames
-verbose
-ignorewarnings
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keepattributes *
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable

# Common Android related
-dontwarn android.app.**
-dontwarn android.support.**
-dontwarn android.view.**
-dontwarn android.widget.**
-dontwarn **CompatHoneycomb
-dontwarn **CompatHoneycombMR2
-dontwarn **CompatCreatorHoneycombMR2

# Common Kotlin related
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit

# Kotlin Coroutine related
-keep class kotlin.coroutines.Continuation

# Specific App related
-keep class com.lelestacia.lelenimexml.MainActivity

# Parcelize related
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepnames @kotlinx.android.parcel.Parcelize public class * { *; }
-keepnames @kotlinx.parcelize.Parcelize public class * { *; }

# Room related
-keep @com.google.android.filament.proguard.UsedBy* class * {
  <init>();
}
-keepclassmembers class * {
  @com.google.android.filament.proguard.UsedBy* *;
}
-keep class com.google.android.filament.proguard.UsedBy*
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity public class *
-dontwarn androidx.room.paging.**

# SQL Cipher related
-dontwarn com.google.common.primitives.**
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class net.sqlcipher.** {
    *;
}

# GSON related
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-dontnote com.google.gson.annotations.SerializedName
-dontnote com.google.gson.annotations.Expose
-keep,allowobfuscation interface com.google.gson.annotations.*
-keep,allowshrinking,allowoptimization class com.google.gson.stream.** { *; }
-keepclassmembers,allowobfuscation class * {
 @com.google.gson.annotations.SerializedName <fields>;
}

# Retrofit related
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# OkHttp related
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform

-keep class * implements androidx.viewbinding.ViewBinding {
    public static *** bind(android.view.View);
    public static *** inflate(android.view.LayoutInflater);
}

-keepnames class * implements android.os.Parcelable {
  public static final ** CREATOR;
}

-keepnames @kotlinx.parcelize.Parcelize public class * { *; }

-printconfiguration build/outputs/fullProguardConfig.pro