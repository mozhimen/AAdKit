package com.mozhimen.adk.topon.basic.bases

import android.content.Context
import android.util.Log
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitialAutoEventListener
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName BaseATInterstitialAutoEventCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/28
 * @Version 1.0
 */
open class BaseATInterstitialAutoEventCallback : ATInterstitialAutoEventListener(), IUtilK {
    override fun onInterstitialAdClicked(adInfo: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdClicked:$adInfo")
    }

    override fun onInterstitialAdShow(adInfo: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdShow:$adInfo")
    }

    override fun onInterstitialAdClose(adInfo: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdClose:$adInfo")
    }

    override fun onInterstitialAdVideoStart(adInfo: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdVideoStart:$adInfo")
    }

    override fun onInterstitialAdVideoEnd(adInfo: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdVideoEnd:$adInfo")
    }

    override fun onInterstitialAdVideoError(adError: AdError) {
        Log.i(TAG, "onInterstitialAdVideoError:" + adError.fullErrorInfo)
    }

    override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
        Log.i(TAG, "onDeeplinkCallback: $adInfo| isSuccess:$isSuccess")
    }

    override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
        Log.i(TAG, "onDownloadConfirm: $adInfo")
    }
}