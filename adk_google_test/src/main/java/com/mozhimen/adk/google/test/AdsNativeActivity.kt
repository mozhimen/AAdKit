package com.mozhimen.adk.google.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MuteThisAdListener
import com.google.android.gms.ads.MuteThisAdReason
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.mozhimen.adk.google.AdKGoogle
import com.mozhimen.adk.google.impls.AdKGoogleNativeSimpleProxy
import com.mozhimen.adk.google.impls.IAdKGoogleNativeListener
import com.mozhimen.adk.google.test.databinding.ActivityAdsNativeBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy

/**
 * 原生广告
 */
class AdsNativeActivity : BaseActivityVB<ActivityAdsNativeBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    private val _adKGoogleNativeSimpleProxy by lazy { AdKGoogleNativeSimpleProxy(this, "ca-app-pub-3940256099942544/2247696110") }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKGoogleNativeSimpleProxy.apply {
            bindLifecycle(this@AdsNativeActivity)
            setIAdKGoogleNativeListener(object : IAdKGoogleNativeListener {
                override fun onAdLoaded(nativeAdView: NativeAdView?) {
                    nativeAdView?.let { vb.flNativeAdContainer.addView(it) }
                }

                override fun onMuteNativeAd() {
                    vb.flNativeAdContainer.removeAllViews()
                }

                override fun onLoadNativeAd(nativeAd: NativeAd?) {
                    vb.btnStopNativeAd.visibility = if (nativeAd?.isCustomMuteThisAdEnabled == true) View.VISIBLE else View.GONE
                }

                override fun onPopulateNativeAdView(nativeAdView: NativeAdView?) {
                    nativeAdView?.let { vb.flNativeAdContainer.removeView(it) }
                }
            })
        }
        vb.btnStopNativeAd.setOnClickListener {
            _adKGoogleNativeSimpleProxy.showChoseMuteNativeAdDialog()
        }
    }
}