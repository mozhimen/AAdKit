/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2022 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.mozhimen.adk.yandex.test

import android.os.Bundle
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.adk.yandex.impls.AdKYandexInterstitialProxy
import com.mozhimen.adk.yandex.test.network.Network
import com.mozhimen.adk.yandex.test.network.NetworkAdapter
import com.mozhimen.adk.yandex.test.databinding.ActivityInterstitialAdBinding
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.uik.databinding.bases.viewbinding.activity.BaseActivityVB
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener

class InterstitialAdActivity : BaseActivityVB<ActivityInterstitialAdBinding>() {

    private val _interstitialAdEventListener = object : InterstitialAdEventListener {
        override fun onAdShown() {
            log("Interstitial ad shown")
        }

        override fun onAdFailedToShow(adError: AdError) {
            log("Interstitial ad show error: $adError")
        }

        override fun onAdDismissed() {
            log("Interstitial ad dismissed")
            disableShowAdButton()
        }

        override fun onAdClicked() {
            log("Interstitial ad clicked")
        }

        override fun onAdImpression(data: ImpressionData?) {
            log("Impression: ${data?.rawData}")
        }
    }

    private val _interstitialAdLoadListener = object : InterstitialAdLoadListener {
        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            log("Interstitial ad loaded")
            enableShowAdButton()
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
            log("Interstitial ad ${adRequestError.adUnitId} failed to load " + "with code ${adRequestError.code}: ${adRequestError.description}")
            disableShowAdButton()
        }
    }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private val _adKYandexInterstitialProxy by lazy_ofNone { AdKYandexInterstitialProxy(this) }

    ////////////////////////////////////////////////////////////////

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        UtilKLogWrapper.d(TAG, "initView: ")
        _adKYandexInterstitialProxy.apply {
            initInterstitialAdListener(_interstitialAdLoadListener, _interstitialAdEventListener)
            initInterstitialAdParams(selectedNetwork.adUnitId)
            bindLifecycle(this@InterstitialAdActivity)
            disableShowAdButton()
        }

        vb.setupUiBidding()
    }

    ////////////////////////////////////////////////////////////////

    private var selectedIndex: Int = 0
    private val selectedNetwork get() = networks[selectedIndex]

    private fun enableShowAdButton() {
        vb.showAdButton.isEnabled = true
    }

    private fun disableShowAdButton() {
        vb.showAdButton.isEnabled = false
    }

    private fun log(message: String) {
        vb.log.text = getString(R.string.log_format, vb.log.text, message)
    }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private fun ActivityInterstitialAdBinding.setupUiBidding() {
        showAdButton.setOnClickListener {
            disableShowAdButton()
            _adKYandexInterstitialProxy.showInterstitialAd()
        }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        networkMenu.setStartIconDrawable(selectedNetwork.iconId)
        networkTextView.setText(selectedNetwork.titleId)
        networkTextView.setAdapter(NetworkAdapter(this@InterstitialAdActivity, networks))
        networkTextView.setOnItemClickListener { _, _, position, _ ->
            selectedIndex = position
            networkMenu.setStartIconDrawable(selectedNetwork.iconId)
            _adKYandexInterstitialProxy.initInterstitialAdParams(selectedNetwork.adUnitId)
            _adKYandexInterstitialProxy.initInterstitialAd()
            _adKYandexInterstitialProxy.loadInterstitialAd()
            disableShowAdButton()
        }
    }

    ////////////////////////////////////////////////////////////////

    companion object {

        private val networks = arrayListOf(
            Network(R.drawable.ic_yandex_icon_24, R.string.yandex_title, "demo-interstitial-yandex"),
            Network(R.drawable.ic_adcolony_icon_24, R.string.adcolony_title, "demo-interstitial-adcolony"),
            Network(R.drawable.ic_admob_icon_24, R.string.admob_title, "demo-interstitial-admob"),
            Network(R.drawable.ic_applovin_icon_24, R.string.applovin_title, "demo-interstitial-applovin"),
            Network(R.drawable.ic_chartboost_icon, R.string.chartboost_title, "demo-interstitial-chartboost"),
            Network(R.drawable.ic_inmobi_icon, R.string.inmobi_title, "demo-interstitial-inmobi"),
            Network(R.drawable.ic_mintegral_logo, R.string.mintegral_title, "demo-interstitial-mintegral"),
            Network(R.drawable.ic_mytarget_icon_24, R.string.my_target_title, "demo-interstitial-mytarget"),
            Network(R.drawable.ic_ironsource_icon_24, R.string.ironsource_title, "demo-interstitial-ironsource"),
            Network(R.drawable.ic_pangle_icon, R.string.pangle_title, "demo-interstitial-pangle"),
            Network(R.drawable.ic_startapp_icon_24, R.string.startapp_title, "demo-interstitial-startapp"),
            Network(R.drawable.ic_tapjoy_icon, R.string.tapjoy_title, "demo-interstitial-tapjoy"),
            Network(R.drawable.ic_unityads_icon_24, R.string.unity_ads_title, "demo-interstitial-unityads"),
            Network(R.drawable.ic_vungle_icon_24, R.string.vungle_title, "demo-interstitial-vungle"),
            Network(R.drawable.ic_adfox_icon, R.string.adfox_title, "R-M-243655-8"),
        )

        private val adFoxRequestParameters =
            mapOf("adf_ownerid" to "270901", "adf_p1" to "cqtgg", "adf_p2" to "fhlx")
    }
}
