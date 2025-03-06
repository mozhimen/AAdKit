package com.mozhimen.adk.yandex.bases

import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

/**
 * @ClassName BaseBannerAdEventListener
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/3/9 0:24
 * @Version 1.0
 */
open class BaseBannerAdEventCallback : BannerAdEventListener {
    override fun onAdLoaded() {
    }

    override fun onAdFailedToLoad(p0: AdRequestError) {
    }

    override fun onAdClicked() {
    }

    override fun onLeftApplication() {
    }

    override fun onReturnedToApplication() {
    }

    override fun onImpression(p0: ImpressionData?) {
    }
}