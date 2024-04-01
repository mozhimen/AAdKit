/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.mozhimen.adk.yandex.basic.test

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.mozhimen.adk.yandex.basic.AdKYandexInlineBannerProxy
import com.mozhimen.adk.yandex.basic.test.databinding.ActivityInlineBannerAdBinding
import com.mozhimen.adk.yandex.basic.test.network.Network
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.wrapper.UtilKScreen
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

class InlineBannerAdActivity : AppCompatActivity(R.layout.activity_inline_banner_ad) {

    companion object {
        private val networks = arrayListOf(
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-banner-yandex"),
            Network(R.drawable.ic_adcolony_icon_24, R.string.adcolony_title, "demo-banner-adcolony"),
            Network(R.drawable.ic_admob_icon_24, R.string.admob_title, "demo-banner-admob"),
            Network(R.drawable.ic_applovin_icon_24, R.string.applovin_title, "demo-banner-applovin"),
            Network(R.drawable.ic_chartboost_icon, R.string.chartboost_title, "demo-banner-chartboost"),
            Network(R.drawable.ic_inmobi_icon, R.string.inmobi_title, "demo-banner-inmobi"),
            Network(R.drawable.ic_ironsource_icon_24, R.string.ironsource_title, "demo-banner-ironsource"),
            Network(R.drawable.ic_mintegral_logo, R.string.mintegral_title, "demo-banner-mintegral"),
            Network(R.drawable.ic_mytarget_icon_24, R.string.my_target_title, "demo-banner-mytarget-mrec"),
            Network(R.drawable.ic_startapp_icon_24, R.string.startapp_title, "demo-banner-startapp"),
            Network(R.drawable.ic_unityads_icon_24, R.string.unity_ads_title, "demo-banner-unityads"),
            Network(R.drawable.ic_vungle_icon_24, R.string.vungle_title, "demo-banner-vungle-mrec"),
            Network(R.drawable.ic_adfox_icon, R.string.adfox_title, "R-M-243655-8"),
        )
    }

    ///////////////////////////////////////////////////////////////////////

    private val adInfoFragment get() = requireNotNull(_adInfoFragment)
    private var _adInfoFragment: AdInfoFragment? = null

    private lateinit var binding: ActivityInlineBannerAdBinding

    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    private val _adKYandexInlineBannerProxy by lazy { AdKYandexInlineBannerProxy() }

    private val _bannerAdEventListener = object : BannerAdEventListener {
        override fun onAdLoaded() {
            adInfoFragment.hideLoading()
            adInfoFragment.log("Banner ad loaded")
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            adInfoFragment.hideLoading()
            adInfoFragment.log(
                "Banner ad failed to load with code ${error.code}: ${error.description}"
            )
        }

        override fun onAdClicked() {
            adInfoFragment.log("Banner ad clicked")
        }

        override fun onLeftApplication() {
            adInfoFragment.log("Left application")
        }

        override fun onReturnedToApplication() {
            adInfoFragment.log("Returned to application")
        }

        override fun onImpression(data: ImpressionData?) {
            adInfoFragment.log("Impression: ${data?.rawData}")
        }
    }

    ///////////////////////////////////////////////////////////////////////

    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInlineBannerAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _adInfoFragment = AdInfoFragment.newInstance(networks)
        adInfoFragment.onLoadClickListener = {
//            _adKYandexInlineBannerProxy.loadBanner()
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.ad_info, adInfoFragment)
        }

        _adKYandexInlineBannerProxy.apply {
            initBannerAdListener(_bannerAdEventListener)
            initBannerAdParams(adInfoFragment.selectedNetwork.adUnitId)
            initBannerAdSize(UtilKScreen.getWidth_ofDisplayMetrics_ofSys(), 90f.dp2px.toInt())
            bindLifecycle(this@InlineBannerAdActivity)
        }

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.coordinatorLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.coordinatorLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                _adKYandexInlineBannerProxy.apply {
                    addBannerViewToContainer(binding.bannerContainer)
                }
            }
        })
    }

    override fun onDestroy() {
        _adInfoFragment = null
        super.onDestroy()
    }

    ///////////////////////////////////////////////////////////////////////

    private fun getRequestParameters(): Map<String, String> {
        return if (adInfoFragment.selectedNetwork.titleId == R.string.adfox_title) {
            return mapOf("adf_ownerid" to "270901", "adf_p1" to "cqtgh", "adf_p2" to "fkbd")
        } else emptyMap()
    }
}
