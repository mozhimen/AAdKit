package com.mozhimen.adk.topon.basic.bases

import android.util.Log
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATAdSourceStatusListener
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName ATAdSourceStatusListenerImpl
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/23
 * @Version 1.0
 */
open class BaseATAdSourceStatusCallback : ATAdSourceStatusListener, IUtilK {
    override fun onAdSourceAttempt(adInfo: ATAdInfo) {
    }

    override fun onAdSourceBiddingAttempt(adInfo: ATAdInfo) {
    }

    override fun onAdSourceLoadFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError) {
    }

    override fun onAdSourceBiddingFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError?) {
    }

    override fun onAdSourceLoadFilled(adInfo: ATAdInfo) {
    }

    override fun onAdSourceBiddingFilled(adInfo: ATAdInfo) {
    }
}