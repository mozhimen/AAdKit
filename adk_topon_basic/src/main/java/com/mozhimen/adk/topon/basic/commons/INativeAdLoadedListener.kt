package com.mozhimen.adk.topon.basic.commons

import android.view.View
import com.anythink.nativead.api.ATNativePrepareInfo
import com.anythink.nativead.api.ATNativeView
import com.anythink.nativead.api.NativeAd

/**
 * @ClassName INativeAdLoadedListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/12
 * @Version 1.0
 */
interface INativeAdLoadedListener {
    fun onNativeAdViewLoad(nativeAd: NativeAd?, atNativePrepareInfo: ATNativePrepareInfo): Pair<ATNativeView?, View?>?
    fun onNativeAdViewLoaded(nativeAd: NativeAd?, videoFuns: List<String>?) {}
}