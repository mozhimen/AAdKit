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
open class BaseATAdSourceStatusCallback : ATAdSourceStatusListener, IUtilK {
    override fun onAdSourceAttempt(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceAttempt: 广告源开始加载回调 $adInfo")
    }

    override fun onAdSourceBiddingAttempt(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceBiddingAttempt: 竞价广告源开始竞价回调 $adInfo")
    }

    override fun onAdSourceLoadFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError) {
        Log.e(TAG, "onAdSourceLoadFail 广告源加载失败回调 Info: $adInfo")
        Log.e(TAG, "onAdSourceLoadFail 广告源加载失败回调 error: ${adError.getFullErrorInfo()}")
    }

    override fun onAdSourceBiddingFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError?) {
        Log.e(TAG, "onAdSourceBiddingFail 竞价广告源竞价失败回调 Info: $adInfo")
        if (adError != null) {
            Log.e(TAG, "onAdSourceBiddingFail error: ${adError.getFullErrorInfo()}")
        }
    }

    override fun onAdSourceLoadFilled(adInfo: ATAdInfo) {
        Log.e(TAG, "onAdSourceLoadFilled: 广告源广告填充回调 $adInfo")
    }

    override fun onAdSourceBiddingFilled(adInfo: ATAdInfo) {
        Log.e(TAG, "onAdSourceBiddingFilled: 竞价广告源竞价成功回调 $adInfo")
    }
}