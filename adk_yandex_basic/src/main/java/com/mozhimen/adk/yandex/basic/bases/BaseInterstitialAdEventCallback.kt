package com.mozhimen.adk.yandex.basic.bases

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
open class BaseInterstitialAdEventCallback : InterstitialAdEventListener {
    override fun onAdShown() {
    }

    override fun onAdFailedToShow(p0: AdError) {
    }

    override fun onAdDismissed() {
    }

    override fun onAdClicked() {
    }

    override fun onAdImpression(p0: ImpressionData?) {
    }
}