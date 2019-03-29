-keep @androidx.annotation.Keep class *
-keepclassmembers @androidx.annotation.Keep class * {
    <fields>;
}

#-keepclassmembers class * implements android.os.Parcelable {
#    static ** CREATOR;
#}

-dontwarn java.lang.invoke.*

# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keepattributes EnclosingMethod
-keepattributes Signature
-keepattributes Exceptions

# need for valid menu/search_menu.xml/@id:action_search/app:actionViewClass processing
-keep class androidx.appcompat.widget.SearchView { *; }

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn java.rmi.**
-dontwarn org.jaxen.**
-dontwarn ru.fantlab.android.ui.modules.**
-dontnote com.google.android.gms.**

-keepnames class * { @com.evernote.android.state.State *;}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}