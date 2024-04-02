package com.mozhimen.adk.yandex.basic.test

import android.os.Bundle
import com.mozhimen.adk.yandex.basic.impls.AdKYandexOpenProxy
import com.mozhimen.adk.yandex.basic.bases.BaseAppOpenAdLoadCallback
import com.mozhimen.adk.yandex.basic.test.databinding.ActivityOpenAdBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.viewbinding.BaseActivityVB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.yandex.mobile.ads.appopenad.AppOpenAd

/**
 * @ClassName OpenAdActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/11
 * @Version 1.0
 */
class OpenAdActivity : BaseActivityVB<ActivityOpenAdBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    private val _adKYandexOpenProxy: AdKYandexOpenProxy by lazy { AdKYandexOpenProxy() }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKYandexOpenProxy.apply {
            initOpenAdListener(object : BaseAppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    _adKYandexOpenProxy.showAppOpenAd(this@OpenAdActivity)
                }
            }, null)
            initOpenAdParams("demo-appopenad-yandex")
            bindLifecycle(this@OpenAdActivity)
        }
    }
}