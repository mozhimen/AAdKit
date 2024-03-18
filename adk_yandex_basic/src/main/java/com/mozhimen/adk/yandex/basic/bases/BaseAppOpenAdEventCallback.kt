package com.mozhimen.adk.yandex.basic.bases

import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.ImpressionData

/**
 * @ClassName BaseAppOpenAdEventCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/11
 * @Version 1.0
 */
open class BaseAppOpenAdEventCallback : AppOpenAdEventListener {
    override fun onAdShown() {

    }

    override fun onAdFailedToShow(p0: AdError) {
    }

    override fun onAdDismissed() {
    }

    override fun onAdClicked() {
    }

    override fun onAdImpression(p0: ImpressionData?) {
    }
}