package com.mozhimen.adk.topon.bases

import com.anythink.core.api.AdError
import com.anythink.nativead.api.ATNativeNetworkListener

/**
 * @ClassName BaseATNativeNetworkCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/15
 * @Version 1.0
 */
open class BaseATNativeNetworkCallback : ATNativeNetworkListener {
    override fun onNativeAdLoaded() {
    }

    override fun onNativeAdLoadFail(p0: AdError?) {
    }
}