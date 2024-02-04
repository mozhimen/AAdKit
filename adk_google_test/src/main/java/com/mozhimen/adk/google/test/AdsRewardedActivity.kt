package com.mozhimen.adk.google.test

import android.os.Bundle
import com.mozhimen.adk.google.impls.AdKGoogleRewardedSimpleProxy
import com.mozhimen.adk.google.impls.IAdKGoogleRewardedListener
import com.mozhimen.adk.google.test.databinding.ActivityAdsRewardedBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.view.applyInVisible
import com.mozhimen.basick.utilk.android.view.applyVisible
import java.lang.ref.WeakReference

class AdsRewardedActivity : BaseActivityVB<ActivityAdsRewardedBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    private val _adKGoogleRewardedSimpleProxy by lazy { AdKGoogleRewardedSimpleProxy(WeakReference(this), "ca-app-pub-3940256099942544/5224354917") }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKGoogleRewardedSimpleProxy.apply {
            bindLifecycle(this@AdsRewardedActivity)
            setAdKGoogleRewardedListener(object : IAdKGoogleRewardedListener {
                override fun onAdLoaded() {
                    vb.btnShowRewardedAd.applyVisible()
                }

                override fun onAdDismissedFullScreenContent() {
                    vb.btnShowRewardedAd.applyInVisible()
                }

                override fun onAdFailedToShowFullScreenContent() {
                    vb.btnShowRewardedAd.applyInVisible()
                }

            })
        }
        vb.btnShowRewardedAd.setOnClickListener {
            _adKGoogleRewardedSimpleProxy.showRewardedVideo()
        }
    }
}