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
    }

    override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
    }

    override fun onInterstitialAdLoaded() {
    }

    override fun onInterstitialAdLoadFail(adError: AdError) {
    }

    override fun onInterstitialAdClicked(entity: ATAdInfo) {
    }

    override fun onInterstitialAdShow(entity: ATAdInfo) {
    }

    override fun onInterstitialAdClose(entity: ATAdInfo) {
    }

    override fun onInterstitialAdVideoStart(entity: ATAdInfo) {
    }

    override fun onInterstitialAdVideoEnd(entity: ATAdInfo) {
    }

    override fun onInterstitialAdVideoError(adError: AdError) {
    }
}