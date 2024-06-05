package com.mozhimen.adk.yandex.basic.cons

/**
 * @ClassName CBuild
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/8
 * @Version 1.0
 */
object CMetaData {
    const val YANDEX_ADS_APPLICATION_ID = """
<manifest>
  <application>
    <meta-data
        android:name="com.google.android.gms.version"
        android:value="@integer/google_play_services_version" />
      
    <!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="ca-app-pub-4449457472880521~8476795127" />
        
    <receiver
        android:name="com.vungle.warren.NetworkProviderReceiver"
        android:exported="true" />
  </application>
</manifest>
    """
}