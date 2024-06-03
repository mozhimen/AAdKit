package com.mozhimen.adk.topon.bases

import android.content.Context
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAdExtraInfo
import com.anythink.splashad.api.ATSplashExListener

/**
 * @ClassName BaseATSplashExCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/11
 * @Version 1.0
 */
open class BaseATSplashExCallback : ATSplashExListener {
    override fun onAdLoaded(p0: Boolean) {
    }

    override fun onAdLoadTimeout() {
    }

    override fun onNoAdError(p0: AdError?) {
    }

    override fun onAdShow(p0: ATAdInfo?) {
    }

    override fun onAdClick(p0: ATAdInfo?) {
    }

    override fun onAdDismiss(p0: ATAdInfo?, p1: ATSplashAdExtraInfo?) {
    }

    override fun onDeeplinkCallback(p0: ATAdInfo?, p1: Boolean) {
    }

    override fun onDownloadConfirm(p0: Context?, p1: ATAdInfo?, p2: ATNetworkConfirmInfo?) {
    }
}