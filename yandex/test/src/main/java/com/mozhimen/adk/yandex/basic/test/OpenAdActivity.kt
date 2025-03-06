package com.mozhimen.adk.yandex.basic.test

import android.os.Bundle
import com.mozhimen.adk.yandex.impls.AdKYandexOpenProxy
import com.mozhimen.adk.yandex.bases.BaseAppOpenAdLoadCallback
import com.mozhimen.adk.yandex.basic.test.databinding.ActivityOpenAdBinding
import com.mozhimen.uik.databinding.bases.activity.viewbinding.BaseActivityVB
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
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
    private val _adKYandexOpenProxy: AdKYandexOpenProxy by lazy_ofNone { AdKYandexOpenProxy() }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKYandexOpenProxy.apply {
            initOpenAdListener(object : BaseAppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    _adKYandexOpenProxy.showOpenAd(this@OpenAdActivity)
                }
            }, null)
            initOpenAdParams("demo-appopenad-yandex")
            bindLifecycle(this@OpenAdActivity)
        }
    }
}