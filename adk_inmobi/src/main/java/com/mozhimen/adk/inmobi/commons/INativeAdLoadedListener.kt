package com.mozhimen.adk.inmobi.commons

import android.view.View
import com.inmobi.ads.InMobiNative

/**
 * @ClassName INativeAdLoadedListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/6/6
 * @Version 1.0
 */
interface INativeAdLoadedListener {
    fun onNativeAdViewLoad(nativeAd: InMobiNative?): View?
    fun onNativeAdViewLoaded(nativeAd: InMobiNative?) {}
}