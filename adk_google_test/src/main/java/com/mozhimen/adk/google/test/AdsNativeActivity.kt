package com.mozhimen.adk.google.test

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.gms.ads.MediaContent
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.mozhimen.adk.google.commons.INativeAdLoadedListener
import com.mozhimen.adk.google.impls.AdKGoogleNativeProxy
import com.mozhimen.adk.google.test.databinding.ActivityAdsNativeBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.view.applyVisibleIf

/**
 * 原生广告
 */
class AdsNativeActivity : BaseActivityVDB<ActivityAdsNativeBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private val _adKGoogleNativeProxy by lazy { AdKGoogleNativeProxy(this) }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKGoogleNativeProxy.apply {
            initBannerAdParams("ca-app-pub-3940256099942544/2247696110")
            initNativeAdListener(null, object : INativeAdLoadedListener {
                override fun onNativeAdViewLoaded(nativeAd: NativeAd) {
                    vdb.btnStopNativeAd.visibility = if (nativeAd.isCustomMuteThisAdEnabled) View.VISIBLE else View.GONE
                    _adKGoogleNativeProxy.addNativeViewToContainer(vdb.flNativeAdContainer)
                }

                override fun onNativeAdViewLoad(
                    nativeAd: NativeAd,
                    icon: NativeAd.Image?,
                    headline: String?,
                    advertiser: String?,
                    starRating: Double?,
                    body: String?,
                    mediaContent: MediaContent?,
                    callToAction: String?,
                    price: String?,
                    store: String?
                ): NativeAdView? =
                    (LayoutInflater.from(this@AdsNativeActivity).inflate(R.layout.layout_admob_native_ad, null) as? NativeAdView)?.apply {
                        iconView = findViewById<AppCompatImageView>(R.id.iv_ad_app_icon).apply {
                            icon?.let { setImageDrawable(it.drawable) }
                            applyVisibleIf(icon != null)
                        }
                        headlineView = findViewById<AppCompatTextView>(R.id.tv_ad_headline).apply {
                            text = headline
                            applyVisibleIf(headline != null)
                        }
                        advertiserView = findViewById<AppCompatTextView>(R.id.tv_advertiser).apply {
                            text = advertiser
                            applyVisibleIf(advertiser != null)
                        }
                        starRatingView = findViewById<AppCompatRatingBar>(R.id.rb_ad_stars).apply {
                            starRating?.let { rating = it.toFloat() }
                            applyVisibleIf(starRating != null)
                        }
                        bodyView = findViewById<AppCompatTextView>(R.id.tv_ad_body).apply {
                            text = body
                            applyVisibleIf(body != null)
                        }
                        mediaView = findViewById<MediaView>(R.id.mv_ad_media).apply {
                            mediaContent?.let {
                                this.mediaContent = it
                                it.videoController.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                                    override fun onVideoStart() {
                                        // 视频开始
                                        Log.i(TAG, "onVideoStart")
                                    }

                                    override fun onVideoEnd() {
                                        // 视频结束，结束后可以刷新广告
                                        Log.i(TAG, "onVideoEnd")
                                    }

                                    override fun onVideoPlay() {
                                        // 视频播放
                                        Log.i(TAG, "onVideoPlay")
                                    }

                                    override fun onVideoPause() {
                                        // 视频暂停
                                        Log.i(TAG, "onVideoPause")
                                    }

                                    override fun onVideoMute(mute: Boolean) {
                                        // 视频是否静音
                                        // mute true 静音 false 非静音
                                        Log.i(TAG, "onVideoMute mute:$mute")
                                    }
                                }
                            }
                        }
                        callToActionView = findViewById<AppCompatButton>(R.id.btn_ad_call_to_action).apply {
                            text = callToAction
                            applyVisibleIf(callToAction != null)
                        }
                        priceView = findViewById<AppCompatTextView>(R.id.tv_ad_price).apply {
                            text = price
                            applyVisibleIf(price != null)
                        }
                        storeView = findViewById<AppCompatTextView>(R.id.tv_ad_store).apply {
                            text = store
                            applyVisibleIf(store != null)
                        }
                        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                            gravity = Gravity.BOTTOM
                        }
                    }

                override fun onNativeAdViewMuted() {
                    vdb.flNativeAdContainer.removeAllViews()
                }
            }, null)
            bindLifecycle(this@AdsNativeActivity)
        }
        vdb.btnStopNativeAd.setOnClickListener {
            _adKGoogleNativeProxy.showChoseMuteNativeAdDialog("关闭此原生广告的原因是？")
        }
    }
}