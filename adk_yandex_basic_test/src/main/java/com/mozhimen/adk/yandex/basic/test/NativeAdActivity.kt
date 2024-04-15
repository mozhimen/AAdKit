package com.mozhimen.adk.yandex.basic.test

import android.os.Bundle
import android.util.Log
import com.mozhimen.adk.yandex.basic.commons.INativeAdLoadedListener
import com.mozhimen.adk.yandex.basic.impls.AdKYandexNativeProxy
import com.mozhimen.adk.yandex.basic.test.databinding.ActivityCustomNativeAdBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.viewbinding.BaseActivityVB
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdView
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder

/**
 * @ClassName NativeAdActivity
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/4/2 22:47
 * @Version 1.0
 */
class NativeAdActivity : BaseActivityVB<ActivityCustomNativeAdBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private val _adKYandexNativeAdProxy by lazy { AdKYandexNativeProxy() }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        _adKYandexNativeAdProxy.apply {
            initNativeAdListener(null, object : INativeAdLoadedListener {
                override fun onNativeAdViewLoad(nativeAd: NativeAd): Pair<NativeAdView, NativeAdViewBinder>? {
                    val nativeAdViewBinder = NativeAdViewBinder.Builder(vb.nativeAd.nativeAdContainer)
                        .setAgeView(vb.nativeAd.age)
                        .setBodyView(vb.nativeAd.body)
                        .setCallToActionView(vb.nativeAd.callToAction)
                        .setDomainView(vb.nativeAd.domain)
                        .setFaviconView(vb.nativeAd.favicon)
                        .setFeedbackView(vb.nativeAd.feedback)
                        .setIconView(vb.nativeAd.icon)
                        .setMediaView(vb.nativeAd.media)
                        .setPriceView(vb.nativeAd.price)
                        .setReviewCountView(vb.nativeAd.reviewCount)
                        .setSponsoredView(vb.nativeAd.sponsored)
                        .setTitleView(vb.nativeAd.title)
                        .setWarningView(vb.nativeAd.warning)
                        .build()
                    return vb.nativeAd.nativeAdContainer to nativeAdViewBinder
                }

                override fun onNativeAdViewLoaded(nativeAd: NativeAd, info: String?) {
                    Log.d(TAG, "onNativeAdViewLoaded: info $info")
                }
            }, null)
            initNativeAdParams("demo-native-content-yandex")
            bindLifecycle(this@NativeAdActivity)
        }
    }
}