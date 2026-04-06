package com.mozhimen.adk.yandex.test

import android.os.Bundle
import com.mozhimen.adk.yandex.impls.AdKYandexOpenProxy
import com.mozhimen.adk.yandex.bases.BaseAppOpenAdLoadCallback
import com.mozhimen.adk.yandex.test.databinding.ActivityOpenAdBinding
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.uik.databinding.bases.viewbinding.activity.BaseActivityVB
import com.yandex.mobile.ads.appopenad.AppOpenAd

/**
 * @ClassName OpenAdActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/11
 * @Version 1.0
 */
class OpenAdActivity : BaseActivityVB<ActivityOpenAdBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private val _adKYandexOpenProxy: AdKYandexOpenProxy by lazy_ofNone { AdKYandexOpenProxy() }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
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