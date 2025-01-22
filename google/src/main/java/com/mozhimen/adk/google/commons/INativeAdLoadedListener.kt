package com.mozhimen.adk.google.commons

import com.google.android.gms.ads.MediaContent
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAd.Image
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * @ClassName INativeAdLoadedListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/2
 * @Version 1.0
 */
interface INativeAdLoadedListener {
    fun onNativeAdViewLoad(
        nativeAd: NativeAd,
        icon: Image?,
        headline: String?,
        advertiser: String?,
        starRating: Double?,
        body: String?,
        mediaContent: MediaContent?,
        callToAction: String?,
        price: String?,
        store: String?
    ): NativeAdView?

    fun onNativeAdViewLoaded(nativeAd: NativeAd) {}

    fun onNativeAdViewMuted() {}
}