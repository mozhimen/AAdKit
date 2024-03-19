package com.mozhimen.adk.yandex.basic

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.mozhimen.adk.yandex.basic.optins.OMetaData_YANDEX_ADS_APPLICATION_ID
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
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
class AdKYandexInterstitialProxy<A>(
    private var _activity: A?
) : BaseWakeBefDestroyLifecycleObserver(), InterstitialAdLoadListener, InterstitialAdEventListener where A : LifecycleOwner, A : Activity {
    private var _interstitialAdLoader: InterstitialAdLoader? = null
    private var _interstitialAd: InterstitialAd? = null
    private var _adUnitId: String = ""
    private var _adFoxRequestParameters: Map<String, String>? = null
    private var _interstitialAdEventListener: InterstitialAdEventListener? = null
    private var _interstitialAdLoadListener: InterstitialAdLoadListener? = null

    //////////////////////////////////////////////////////////////////////

    fun initInterstitialAdListener(interstitialAdEventListener: InterstitialAdEventListener, interstitialAdLoadListener: InterstitialAdLoadListener) {
        _interstitialAdEventListener = interstitialAdEventListener
        _interstitialAdLoadListener = interstitialAdLoadListener
    }

    fun initInterstitialAdParams(adUnitId: String, adFoxRequestParameters: Map<String, String>? = null) {
        _adUnitId = adUnitId
        _adFoxRequestParameters = adFoxRequestParameters
    }

    fun initInterstitialAd() {
        if (_activity != null) {
            Log.d(TAG, "onCreate: InterstitialAdLoader")
            _interstitialAdLoader = InterstitialAdLoader(_activity!!).apply {
                setAdLoadListener(this@AdKYandexInterstitialProxy)
            }
        }
    }

    fun loadInterstitialAd() {
        _interstitialAdLoader?.loadAd(createAdRequestConfiguration(_adUnitId, _adFoxRequestParameters)) ?: run {
            Log.d(TAG, "loadInterstitialAd: null")
        }
    }

    //////////////////////////////////////////////////////////////////////

    fun showInterstitialAd() {
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

    fun destroyInterstitialAdd() {
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
        destroyInterstitialAdd()
        _activity = null
        super.onDestroy(owner)
    }

    //////////////////////////////////////////////////////////////////////

    override fun onAdLoaded(interstitialAd: InterstitialAd) {
        Log.d(TAG, "onAdLoaded: ")
        _interstitialAdLoadListener?.onAdLoaded(interstitialAd)

        _interstitialAd = interstitialAd
    }

    override fun onAdFailedToLoad(adRequestError: AdRequestError) {
        Log.e(TAG, "onAdFailedToLoad: AdRequestError $adRequestError")
        _interstitialAdLoadListener?.onAdFailedToLoad(adRequestError)
    }

    //////////////////////////////////////////////////////////////////////

    override fun onAdShown() {
        Log.d(TAG, "onAdShown: ")
        _interstitialAdEventListener?.onAdShown()
    }

    override fun onAdFailedToShow(p0: AdError) {
        Log.e(TAG, "onAdFailedToShow: AdError $p0")
        _interstitialAdEventListener?.onAdFailedToShow(p0)
    }

    override fun onAdDismissed() {
        Log.d(TAG, "onAdDismissed: ")
        _interstitialAdEventListener?.onAdDismissed()

        destroyInterstitialAdd()// Now you can preload the next interstitial ad.
    }

    override fun onAdClicked() {
        Log.d(TAG, "onAdClicked: ")
        _interstitialAdEventListener?.onAdClicked()
    }

    override fun onAdImpression(p0: ImpressionData?) {
        Log.d(TAG, "onAdImpression: ImpressionData $p0")
        _interstitialAdEventListener?.onAdImpression(p0)
    }

    //////////////////////////////////////////////////////////////////////

    private fun createAdRequestConfiguration(adUnitId: String, adFoxRequestParameters: Map<String, String>? = null): AdRequestConfiguration {
        return if (adFoxRequestParameters != null) {
            AdRequestConfiguration.Builder(adUnitId)
                .setParameters(adFoxRequestParameters)
        } else {
            AdRequestConfiguration.Builder(adUnitId)
        }.build()
    }
}