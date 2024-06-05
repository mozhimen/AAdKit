package com.mozhimen.adk.yandex.basic.bases

import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.basick.utilk.commons.IUtilK
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener

/**
 * @ClassName BaseInterstitialAdLoadCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/8
 * @Version 1.0
 */
open class BaseInterstitialAdLoadCallback : InterstitialAdLoadListener {
    override fun onAdLoaded(p0: InterstitialAd) {
    }

    override fun onAdFailedToLoad(p0: AdRequestError) {
    }
}