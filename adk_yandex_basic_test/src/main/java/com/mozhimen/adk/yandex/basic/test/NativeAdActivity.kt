package com.mozhimen.adk.yandex.basic.test

import android.os.Bundle
import com.mozhimen.adk.yandex.basic.commons.INativeAdLoadedListener
import com.mozhimen.adk.yandex.basic.impls.AdKYandexNativeProxy
import com.mozhimen.adk.yandex.basic.test.databinding.ActivityCustomNativeAdBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.viewbinding.BaseActivityVB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy

/**
 * @ClassName NativeAdActivity
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/4/2 22:47
 * @Version 1.0
 */
class NativeAdActivity : BaseActivityVB<ActivityCustomNativeAdBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private val _adKYandexNativeAdProxy by lazy { AdKYandexNativeProxy(this) }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKYandexNativeAdProxy.apply {
            initNativeAdListener(null, object : INativeAdLoadedListener {

            }, null)
            initBannerAdParams("demo-native-content-yandex")
            bindLifecycle(this@NativeAdActivity)
        }

    }
}