# adk_yandex
#///////////////////////////////////////////////////
# amazon
-keep class com.amazon.device.ads.** { *; }
-keep class com.iabtcf.** { *; }

#///////////////////////////////////////////////////
# bytedance
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}

-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep class com.bytedance.frameworks.** { *; }

-keep class ms.bd.c.Pgl.**{*;}
-keep class com.bytedance.mobsec.metasec.ml.**{*;}

-keep class com.ss.android.**{*;}

-keep class com.bytedance.embedapplog.** {*;}
-keep class com.bytedance.embed_dr.** {*;}

-keep class com.bykv.vk.** {*;}

#///////////////////////////////////////////////////
# chartboost
-keep class com.chartboost.** { *; }

#///////////////////////////////////////////////////
# facebook
-keep class androidx.recyclerview.widget.RecyclerView { *; }
-keep class androidx.appcompat.app.AppCompatActivity { *; }
#-keep class com.facebook.ads.BuildConfig { *; }

#///////////////////////////////////////////////////
# hyprmx
-keep class com.hyprmx.** { *; }

#///////////////////////////////////////////////////
# inmobi
-keepattributes SourceFile,LineNumberTable
-keep class com.inmobi.** { *; }
-dontwarn com.inmobi.**
-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}

#skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**

#skip Moat classes
-keep class com.moat.** {*;}
-dontwarn com.moat.**

#skip AVID classes
-keep class com.integralads.avid.library.** {*;}

#skip AndroidX libraries
-keep class androidx.browser.customtabs.CustomTabsService { *; }
-keep class androidx.recyclerview.widget.RecyclerView { *; }

#///////////////////////////////////////////////////
# ironsource
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep class com.ironsource.adapters.** { *;
}
-dontwarn com.ironsource.mediationsdk.**
-dontwarn com.ironsource.adapters.**
-dontwarn com.moat.**
-keep class com.moat.** { public protected private *; }

-keep class com.ironsource.mediationsdk.** { *; }

#///////////////////////////////////////////////////
# five_corp
-keep class com.five_corp.ad.** { *; }

#///////////////////////////////////////////////////
# maio
# For removing warnings due to lack of Multi-Window support
-dontwarn android.app.Activity
# For Mediation Debugger support
-keepnames class jp.maio.sdk.android.*

#///////////////////////////////////////////////////
# mytarget
-keep class com.my.target.** {*;}

#///////////////////////////////////////////////////
# smaato
#-keep public class com.smaato.soma.internal.connector.OrmmaBridge {
#public *;
#}
#-keepattributes *Annotation*

-keep public class com.smaato.sdk.** { *; }
-keep public interface com.smaato.sdk.** { *; }

#///////////////////////////////////////////////////
# unityads
# For Mediation Debugger support
-keepnames class com.smaato.sdk.core.*

# Keep filenames and line numbers for stack traces
-keepattributes SourceFile,LineNumberTable

# Keep JavascriptInterface for WebView bridge
-keepattributes JavascriptInterface

# Sometimes keepattributes is not enough to keep annotations
-keep class android.webkit.JavascriptInterface {*;}

# Keep all classes in Unity Ads package
-keep class com.unity3d.ads.** {*;}

# Keep all classes in Unity Services package
-keep class com.unity3d.services.** {*;}

-dontwarn com.google.ar.core.**

#///////////////////////////////////////////////////
# verve
-keepattributes Signature
-keep class net.pubnative.** { *; }

#///////////////////////////////////////////////////
# vungle
-keep class com.vungle.warren.** { *; }
-dontwarn com.vungle.warren.error.VungleError$ErrorCode

# Moat SDK
-keep class com.moat.** { *; }
-dontwarn com.moat.**

# Okio
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Retrofit
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Google Android Advertising ID
-keep class com.google.android.gms.internal.** { *; }
-dontwarn com.google.android.gms.ads.identifier.**

#///////////////////////////////////////////////////
# yandex
-keepclassmembers class com.yandex.mobile.ads.* {
    public void onImpression(com.yandex.mobile.ads.common.ImpressionData);
}

#///////////////////////////////////////////////////
# ysonetwork
-keep class com.ysocorp.ysonetwork.** { *; }

