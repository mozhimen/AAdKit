package com.mozhimen.adk.yandex.basic.impls

import android.app.Activity
import android.util.Log
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import androidx.lifecycle.LifecycleOwner
import com.mozhimen.adk.basic.commons.IAdKInterstitialProxy
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader

/**
 * @ClassName AdKYandexInterstitial
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/8
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class AdKYandexInterstitialProxy(
    private var _activity: Activity?
) : BaseWakeBefDestroyLifecycleObserver(), InterstitialAdLoadListener, InterstitialAdEventListener, IAdKInterstitialProxy {
    private var _interstitialAdLoader: InterstitialAdLoader? = null
    private var _interstitialAd: InterstitialAd? = null
    private var _adUnitId: String = ""
    private var _adFoxRequestParameters: Map<String, String>? = null

    private var _interstitialAdLoadListener: InterstitialAdLoadListener? = null
    private var _interstitialAdEventListener: InterstitialAdEventListener? = null

    //////////////////////////////////////////////////////////////////////

    fun initInterstitialAdListener(interstitialAdLoadListener: InterstitialAdLoadListener, interstitialAdEventListener: InterstitialAdEventListener) {
        _interstitialAdLoadListener = interstitialAdLoadListener
        _interstitialAdEventListener = interstitialAdEventListener
    }

    fun initInterstitialAdParams(adUnitId: String, adFoxRequestParameters: Map<String, String>? = null) {
        _adUnitId = adUnitId
        _adFoxRequestParameters = adFoxRequestParameters
    }

    override fun initInterstitialAd() {
        if (_activity != null) {
            UtilKLogWrapper.d(TAG, "onCreate: InterstitialAdLoader")
            _interstitialAdLoader = InterstitialAdLoader(_activity!!).apply {
                setAdLoadListener(this@AdKYandexInterstitialProxy)
            }
        }
    }

    override fun loadInterstitialAd() {
        val adRequest = if (_adFoxRequestParameters != null) {
            AdRequestConfiguration.Builder(_adUnitId)
                .setParameters(_adFoxRequestParameters!!)
        } else {
            AdRequestConfiguration.Builder(_adUnitId)
        }.build()
        _interstitialAdLoader?.loadAd(adRequest) ?: run {
            UtilKLogWrapper.d(TAG, "loadInterstitialAd: null")
        }
    }

    //////////////////////////////////////////////////////////////////////

    override fun showInterstitialAd() {
        if (_activity != null && _interstitialAd != null) {
            _interstitialAd!!.apply {
                setAdEventListener(this@AdKYandexInterstitialProxy)
                show(_activity!!)
            }
        }
    }

    fun destroyInterstitialAddLoader() {
        // set listener to null to avoid memory leaks
        _interstitialAdLoader?.setAdLoadListener(null)
        _interstitialAdLoader = null
    }

    override fun destroyInterstitialAd() {
        // don't forget to clean up event listener to null?
        _interstitialAd?.setAdEventListener(null)
        _interstitialAd = null
    }

    //////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        // Interstitial ads loading should occur after initialization of the SDK.
        // Initialize SDK as early as possible, for example in Application.onCreate or at least Activity.onCreate
        // It's recommended to use the same instance of InterstitialAdLoader for every load for
        // achieve better performance
        initInterstitialAd()
        loadInterstitialAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyInterstitialAddLoader()
        destroyInterstitialAd()
        _interstitialAdLoadListener = null
        _interstitialAdEventListener = null
        _activity = null
        super.onDestroy(owner)
    }

    //////////////////////////////////////////////////////////////////////

    override fun onAdLoaded(interstitialAd: InterstitialAd) {
        UtilKLogWrapper.d(TAG, "onAdLoaded: ")
        _interstitialAdLoadListener?.onAdLoaded(interstitialAd)

        _interstitialAd = interstitialAd
    }

    override fun onAdFailedToLoad(adRequestError: AdRequestError) {
        UtilKLogWrapper.e(TAG, "onAdFailedToLoad: AdRequestError $adRequestError")
        _interstitialAdLoadListener?.onAdFailedToLoad(adRequestError)
    }

    //////////////////////////////////////////////////////////////////////

    override fun onAdShown() {
        UtilKLogWrapper.d(TAG, "onAdShown: ")
        _interstitialAdEventListener?.onAdShown()
    }

    override fun onAdFailedToShow(p0: AdError) {
        UtilKLogWrapper.e(TAG, "onAdFailedToShow: AdError $p0")
        _interstitialAdEventListener?.onAdFailedToShow(p0)
    }

    override fun onAdDismissed() {
        UtilKLogWrapper.d(TAG, "onAdDismissed: ")
        _interstitialAdEventListener?.onAdDismissed()

//        destroyInterstitialAd()// Now you can preload the next interstitial ad.
    }

    override fun onAdClicked() {
        UtilKLogWrapper.d(TAG, "onAdClicked: ")
        _interstitialAdEventListener?.onAdClicked()
    }

    override fun onAdImpression(p0: ImpressionData?) {
        UtilKLogWrapper.d(TAG, "onAdImpression: ImpressionData $p0")
        _interstitialAdEventListener?.onAdImpression(p0)
    }
}