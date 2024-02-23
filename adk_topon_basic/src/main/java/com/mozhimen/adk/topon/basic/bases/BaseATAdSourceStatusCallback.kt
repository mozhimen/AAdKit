package com.mozhimen.adk.topon.basic.bases

import android.util.Log
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATAdSourceStatusListener
import com.mozhimen.basick.utilk.bases.IUtilK

/**
 * @ClassName ATAdSourceStatusListenerImpl
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/23
 * @Version 1.0
 */
class BaseATAdSourceStatusCallback : ATAdSourceStatusListener, IUtilK {
    override fun onAdSourceBiddingAttempt(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceBiddingAttempt: $adInfo")
    }

    override fun onAdSourceBiddingFilled(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceBiddingFilled: $adInfo")
    }

    override fun onAdSourceBiddingFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError?) {
        Log.i(TAG, "onAdSourceBiddingFail Info: $adInfo")
        if (adError != null) {
            Log.i(TAG, "onAdSourceBiddingFail error: " + adError.getFullErrorInfo())
        }
    }

    override fun onAdSourceAttempt(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceAttempt: $adInfo")
    }

    override fun onAdSourceLoadFilled(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceLoadFilled: $adInfo")
    }

    override fun onAdSourceLoadFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError) {
        Log.i(TAG, "onAdSourceLoadFail Info: $adInfo")
        Log.i(TAG, "onAdSourceLoadFail error: " + adError.getFullErrorInfo())
    }
}