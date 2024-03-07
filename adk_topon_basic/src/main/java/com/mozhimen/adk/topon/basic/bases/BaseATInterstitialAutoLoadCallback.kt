package com.mozhimen.adk.topon.basic.bases

import android.util.Log
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
        Log.i(TAG, "onInterstitialAutoLoaded PlacementId:$placementId: onInterstitialAutoLoaded")
    }

    override fun onInterstitialAutoLoadFail(placementId: String, adError: AdError) {
        Log.i(TAG, "onInterstitialAutoLoadFail PlacementId:$placementId: onInterstitialAutoLoadFail:${adError.fullErrorInfo}")
    }
}