package com.mozhimen.adk.google.test

import android.os.Bundle
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mozhimen.adk.google.impls.AdKGoogleInterstitialProxy
import com.mozhimen.adk.google.test.databinding.ActivityAdsInterstitialBinding
import com.mozhimen.basick.bases.databinding.BaseActivityVDB
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.view.applyInVisible
import com.mozhimen.kotlin.utilk.android.view.applyVisible

/**
 * 插页广告
 */
class AdsInterstitialActivity : BaseActivityVDB<ActivityAdsInterstitialBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private val _adKGoogleInterstitialProxy by lazy_ofNone { AdKGoogleInterstitialProxy(this) }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKGoogleInterstitialProxy.apply {
            initInterstitialAdParams("ca-app-pub-3940256099942544/1033173712")
            initInterstitialAdListener(
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(p0: InterstitialAd) {
                        vdb.btnShowInterstitialAd.applyVisible()
                    }
                },
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        vdb.btnShowInterstitialAd.applyInVisible()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        vdb.btnShowInterstitialAd.applyInVisible()
                    }
                })
            bindLifecycle(this@AdsInterstitialActivity)
        }
        vdb.btnShowInterstitialAd.setOnClickListener {
            _adKGoogleInterstitialProxy.showInterstitialAd()
        }
    }
}