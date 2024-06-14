package com.mozhimen.adk.google.test

import android.os.Bundle
import android.view.ViewGroup
import com.google.android.gms.ads.AdListener
import com.mozhimen.adk.google.impls.AdKGoogleBannerProxy
import com.mozhimen.adk.google.test.databinding.ActivityAdsBannerBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.util.px2dp
import com.mozhimen.basick.utilk.android.view.addAndRemoveOnGlobalLayoutListener
import com.mozhimen.basick.utilk.android.view.applyVisible
import com.mozhimen.basick.utilk.wrapper.UtilKScreen

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
            initBannerAdSize((UtilKScreen.getWidth_ofDisplayMetrics_ofSys().px2dp - 46f).toInt())
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