package com.mozhimen.adk.yandex.bases

import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.common.AdRequestError

/**
 * @ClassName BaseAppOpenAdLoadCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/11
 * @Version 1.0
 */
open class BaseAppOpenAdLoadCallback : AppOpenAdLoadListener {
    override fun onAdLoaded(p0: AppOpenAd) {

    }

    override fun onAdFailedToLoad(p0: AdRequestError) {
    }
}