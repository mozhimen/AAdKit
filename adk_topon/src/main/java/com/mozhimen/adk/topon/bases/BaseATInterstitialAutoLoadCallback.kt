package com.mozhimen.adk.topon.bases

import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitialAutoLoadListener
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName BaseATInterstitialAutoLoadCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/28
 * @Version 1.0
 */
open class BaseATInterstitialAutoLoadCallback : ATInterstitialAutoLoadListener, IUtilK {
    override fun onInterstitialAutoLoaded(placementId: String) {
    }

    override fun onInterstitialAutoLoadFail(placementId: String, adError: AdError) {
    }
}