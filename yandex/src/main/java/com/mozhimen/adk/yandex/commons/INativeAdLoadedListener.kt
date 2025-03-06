package com.mozhimen.adk.yandex.commons

import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdView
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder

/**
 * @ClassName INativeAdLoadedListener
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/4/2 23:27
 * @Version 1.0
 */
interface INativeAdLoadedListener {
    fun onNativeAdViewLoad(
        nativeAd: NativeAd,
    ): Pair<NativeAdView, NativeAdViewBinder>?

    fun onNativeAdViewLoaded(nativeAd: NativeAd, info: String?) {}
}