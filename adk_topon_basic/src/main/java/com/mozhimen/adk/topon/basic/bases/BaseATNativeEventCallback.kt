package com.mozhimen.adk.topon.basic.bases

import com.anythink.core.api.ATAdInfo
import com.anythink.nativead.api.ATNativeAdView
import com.anythink.nativead.api.ATNativeEventListener

/**
 * @ClassName BaseATNativeEventCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/15
 * @Version 1.0
 */
class BaseATNativeEventCallback : ATNativeEventListener {
    override fun onAdImpressed(p0: ATNativeAdView?, p1: ATAdInfo?) {
    }

    override fun onAdClicked(p0: ATNativeAdView?, p1: ATAdInfo?) {
    }

    override fun onAdVideoStart(p0: ATNativeAdView?) {
    }

    override fun onAdVideoEnd(p0: ATNativeAdView?) {
    }

    override fun onAdVideoProgress(p0: ATNativeAdView?, p1: Int) {
    }
}