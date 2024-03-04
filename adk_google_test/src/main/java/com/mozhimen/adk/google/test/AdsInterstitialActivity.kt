package com.mozhimen.adk.google.test

import android.os.Bundle
import com.mozhimen.adk.google.impls.AdKGoogleInterstitialSimpleProxy
import com.mozhimen.adk.google.impls.IAdKGoogleInterstitialListener
import com.mozhimen.adk.google.test.databinding.ActivityAdsInterstitialBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.view.applyInVisible
import com.mozhimen.basick.utilk.android.view.applyVisible
import java.lang.ref.WeakReference

/**
 * 插页广告
 */
class AdsInterstitialActivity : BaseActivityVDB<ActivityAdsInterstitialBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    private val _adKGoogleInterstitialSimpleProxy by lazy { AdKGoogleInterstitialSimpleProxy(WeakReference(this), "ca-app-pub-3940256099942544/1033173712") }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKGoogleInterstitialSimpleProxy.apply {
            bindLifecycle(this@AdsInterstitialActivity)
            setAdkGoogleInterstitialListener(object : IAdKGoogleInterstitialListener {
                override fun onAdLoaded() {
                    vdb.btnShowInterstitialAd.applyVisible()
                }

                override fun onAdDismissedFullScreenContent() {
                    vdb.btnShowInterstitialAd.applyInVisible()
                }

                override fun onAdFailedToShowFullScreenContent() {
                    vdb.btnShowInterstitialAd.applyInVisible()
                }
            })
        }
        vdb.btnShowInterstitialAd.setOnClickListener {
            _adKGoogleInterstitialSimpleProxy.showInterstitialAd()
        }
    }
}