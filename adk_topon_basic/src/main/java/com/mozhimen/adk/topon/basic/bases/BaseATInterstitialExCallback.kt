package com.mozhimen.adk.topon.basic.bases

import android.content.Context
import android.util.Log
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitialExListener
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName BaseATInterstitialExCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/28
 * @Version 1.0
 */
open class BaseATInterstitialExCallback : ATInterstitialExListener, IUtilK {
    override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
        Log.i(TAG, "onDeeplinkCallback:$adInfo--status:$isSuccess")
    }

    override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
        Log.i(TAG, "onDownloadConfirm: adInfo=$adInfo")
    }

    override fun onInterstitialAdLoaded() {
        Log.i(TAG, "onInterstitialAdLoaded")
    }

    override fun onInterstitialAdLoadFail(adError: AdError) {
        Log.i(TAG, "onInterstitialAdLoadFail:${adError.fullErrorInfo}")
    }

    override fun onInterstitialAdClicked(entity: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdClicked:$entity")
    }

    override fun onInterstitialAdShow(entity: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdShow:$entity")
    }

    override fun onInterstitialAdClose(entity: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdClose:$entity")
    }

    override fun onInterstitialAdVideoStart(entity: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdVideoStart:$entity")
    }

    override fun onInterstitialAdVideoEnd(entity: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdVideoEnd:$entity")
    }

    override fun onInterstitialAdVideoError(adError: AdError) {
        Log.i(TAG, "onInterstitialAdVideoError:${adError.fullErrorInfo}")
    }
}