package com.mozhimen.adk.google.test

import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.mozhimen.adk.google.impls.AdKGoogleNativeSimpleProxy
import com.mozhimen.adk.google.impls.IAdKGoogleNativeListener
import com.mozhimen.adk.google.test.databinding.ActivityAdsNativeBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy

/**
 * 原生广告
 */
class AdsNativeActivity : BaseActivityVDB<ActivityAdsNativeBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    private val _adKGoogleNativeSimpleProxy by lazy { AdKGoogleNativeSimpleProxy(this, "ca-app-pub-3940256099942544/2247696110") }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKGoogleNativeSimpleProxy.apply {
            bindLifecycle(this@AdsNativeActivity)
            setIAdKGoogleNativeListener(object : IAdKGoogleNativeListener {
                override fun onAdLoaded(nativeAdView: NativeAdView?) {
                    nativeAdView?.let { vdb.flNativeAdContainer.addView(it) }
                }

                override fun onMuteNativeAd() {
                    vdb.flNativeAdContainer.removeAllViews()
                }

                override fun onLoadNativeAd(nativeAd: NativeAd?) {
                    vdb.btnStopNativeAd.visibility = if (nativeAd?.isCustomMuteThisAdEnabled == true) View.VISIBLE else View.GONE
                }

                override fun onPopulateNativeAdView(nativeAdView: NativeAdView?) {
                    nativeAdView?.let { vdb.flNativeAdContainer.removeView(it) }
                }
            })
        }
        vdb.btnStopNativeAd.setOnClickListener {
            _adKGoogleNativeSimpleProxy.showChoseMuteNativeAdDialog()
        }
    }
}