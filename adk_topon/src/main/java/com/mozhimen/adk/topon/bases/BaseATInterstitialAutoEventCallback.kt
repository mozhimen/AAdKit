package com.mozhimen.adk.topon.bases

import android.content.Context
import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
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
    }

    override fun onInterstitialAdShow(adInfo: ATAdInfo) {
    }

    override fun onInterstitialAdClose(adInfo: ATAdInfo) {
    }

    override fun onInterstitialAdVideoStart(adInfo: ATAdInfo) {
    }

    override fun onInterstitialAdVideoEnd(adInfo: ATAdInfo) {
    }

    override fun onInterstitialAdVideoError(adError: AdError) {
    }

    override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
    }

    override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
    }
}