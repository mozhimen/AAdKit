package com.mozhimen.adk.topon.commons

import android.view.View
import com.anythink.nativead.api.ATNativeMaterial
import com.anythink.nativead.api.ATNativePrepareInfo
import com.anythink.nativead.api.ATNativeAdView
import com.anythink.nativead.api.NativeAd

/**
 * @ClassName INativeAdLoadedListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/12
 * @Version 1.0
 */
interface INativeAdLoadedListener {
    fun onNativeAdViewLoad(nativeAd: NativeAd?, adMaterial: ATNativeMaterial?, atNativePrepareInfo: ATNativePrepareInfo): Pair<ATNativeAdView?, View?>?
    fun onNativeAdViewLoaded(nativeAd: NativeAd?, videoFuns: List<String>?) {}
}