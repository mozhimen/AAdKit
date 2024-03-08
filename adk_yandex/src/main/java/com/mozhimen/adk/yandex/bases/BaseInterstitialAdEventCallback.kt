package com.mozhimen.adk.yandex.bases

import android.util.Log
import com.mozhimen.basick.utilk.commons.IUtilK
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener

/**
 * @ClassName BaseInterstitialAdEventCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/8
 * @Version 1.0
 */
open class BaseInterstitialAdEventCallback : InterstitialAdEventListener, IUtilK {
    override fun onAdShown() {
        Log.d(TAG, "onAdShown: ")
    }

    override fun onAdFailedToShow(p0: AdError) {
        Log.d(TAG, "onAdFailedToShow: AdError $p0")
    }

    override fun onAdDismissed() {
        Log.d(TAG, "onAdDismissed: ")
    }

    override fun onAdClicked() {
        Log.d(TAG, "onAdClicked: ")
    }

    override fun onAdImpression(p0: ImpressionData?) {
        Log.d(TAG, "onAdImpression: ImpressionData $p0")
    }
}