package com.mozhimen.adk.google.test

import android.os.Bundle
import android.view.ViewGroup
import com.google.android.gms.ads.AdListener
import com.mozhimen.adk.google.impls.AdKGoogleBannerProxy
import com.mozhimen.adk.google.test.databinding.ActivityAdsBannerBinding
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.util.px2dp
import com.mozhimen.kotlin.utilk.android.view.addAndRemoveOnGlobalLayoutListener
import com.mozhimen.kotlin.utilk.android.view.applyVisible
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.kotlin.utilk.wrapper.UtilKScreen
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB

/**
 * 横幅广告
 */
class AdsBannerActivity : BaseActivityVDB<ActivityAdsBannerBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private val _adKGoogleBannerProxy by lazy_ofNone { AdKGoogleBannerProxy() }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKGoogleBannerProxy.apply {
            initBannerAdParams("ca-app-pub-3940256099942544/9214589741")
            initBannerAdSize((UtilKScreen.getWidth_ofDisplayMetrics_ofSys().px2dp() - 46f).toInt())
            initBannerAdListener(object : AdListener() {
                override fun onAdLoaded() {
                    vdb.btnShowBannerAd.applyVisible()
                    vdb.btnHideBannerAd.applyVisible()
                }
            })
            bindLifecycle(this@AdsBannerActivity)
        }
        vdb.root.addAndRemoveOnGlobalLayoutListener {
            _adKGoogleBannerProxy.addBannerViewToContainer(vdb.root as ViewGroup)
        }
        vdb.btnShowBannerAd.setOnClickListener {
            _adKGoogleBannerProxy.showBannerAd()
        }
        vdb.btnHideBannerAd.setOnClickListener {
            _adKGoogleBannerProxy.hideBannerAd()
        }
    }
}