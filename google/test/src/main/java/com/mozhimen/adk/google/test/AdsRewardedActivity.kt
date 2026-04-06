package com.mozhimen.adk.google.test

import android.os.Bundle
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.mozhimen.adk.google.impls.AdKGoogleRewardedProxy
import com.mozhimen.adk.google.test.databinding.ActivityAdsRewardedBinding
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.view.applyInVisible
import com.mozhimen.kotlin.utilk.android.view.applyVisible
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB

class AdsRewardedActivity : BaseActivityVDB<ActivityAdsRewardedBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private val _adKGoogleRewardedProxy by lazy_ofNone { AdKGoogleRewardedProxy(this/*, "ca-app-pub-3940256099942544/5224354917"*/) }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKGoogleRewardedProxy.apply {
            initRewardedAdParams("ca-app-pub-3940256099942544/5224354917")
            initRewardedAdListener(
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(p0: RewardedAd) {
                        vdb.btnShowRewardedAd.applyVisible()
                    }
                },
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        vdb.btnShowRewardedAd.applyInVisible()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        vdb.btnShowRewardedAd.applyInVisible()
                    }
                }, null
            )
            bindLifecycle(this@AdsRewardedActivity)
        }
        vdb.btnShowRewardedAd.setOnClickListener {
            _adKGoogleRewardedProxy.showRewardedAd()
        }
    }
}