package com.mozhimen.adk.google.test

import android.os.Bundle
import com.mozhimen.adk.google.impls.AdKGoogleBannerSimpleProxy
import com.mozhimen.adk.google.test.databinding.ActivityAdsBannerBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.view.applyVisible

/**
 * 横幅广告
 */
class AdsBannerActivity : BaseActivityVDB<ActivityAdsBannerBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    private val _adKGoogleBannerSimpleProxy by lazy { AdKGoogleBannerSimpleProxy(this, "ca-app-pub-3940256099942544/6300978111") }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKGoogleBannerSimpleProxy.apply {
            bindLifecycle(this@AdsBannerActivity)
            setOnLoadListener {
                if (it) {
                    vdb.btnShowBannerAd.applyVisible()
                    vdb.btnHideBannerAd.applyVisible()
                }
            }
        }
        vdb.btnShowBannerAd.setOnClickListener {
            _adKGoogleBannerSimpleProxy.showBanner()
        }
        vdb.btnHideBannerAd.setOnClickListener {
            _adKGoogleBannerSimpleProxy.hideBanner()
        }
    }
}