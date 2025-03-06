package com.mozhimen.adk.yandex.impls

import android.app.Activity
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import androidx.lifecycle.LifecycleOwner
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

/**
 * @ClassName AdKYandexOpenProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/11
 * @Version 1.0
 */
@OApiCall_BindViewLifecycle
@OApiInit_ByLazy
@OApiCall_BindLifecycle
class AdKYandexOpenProxy : BaseWakeBefDestroyLifecycleObserver(), IAdKOpenProxy, AppOpenAdLoadListener, AppOpenAdEventListener {
    private var _appOpenAdLoader: AppOpenAdLoader? = null
    private var _appOpenAd: AppOpenAd? = null
    private var _appOpenAdLoadListener: AppOpenAdLoadListener? = null
    private var _appOpenAdEventListener: AppOpenAdEventListener? = null
    private var _adUnitId = ""
    //    private var _loadingInProgress = AtomicBoolean(false)

    ///////////////////////////////////////////////////////////////////////////////

    fun initOpenAdListener(appOpenAdLoadListener: AppOpenAdLoadListener?, appOpenAdEventListener: AppOpenAdEventListener?) {
        _appOpenAdLoadListener = appOpenAdLoadListener
        _appOpenAdEventListener = appOpenAdEventListener
    }

    fun initOpenAdParams(adUnitId: String) {
        _adUnitId = adUnitId
    }

    override fun initOpenAd() {
        _appOpenAdLoader = AppOpenAdLoader(_context).apply {
            setAdLoadListener(this@AdKYandexOpenProxy)
        }
    }

    override fun loadOpenAd() {
        // load new Ad if there is no loaded Ad and new ad isn't loading
//        if (_loadingInProgress.compareAndSet(false, true)) {
        _appOpenAdLoader?.loadAd(AdRequestConfiguration.Builder(_adUnitId).build()) ?: run {
            UtilKLogWrapper.d(TAG, "loadOpenAd: null")
        }
//        }
    }

    override fun showOpenAd(activity: Activity?) {
        // show AppOpenAd when Application comes foreground if there is opened specific Activity
        if (activity != null && _appOpenAd != null) {
//            showAdIfAvailable(_activity)
            _appOpenAd!!.apply {
                setAdEventListener(this@AdKYandexOpenProxy)
                show(activity)
            }
        }
    }

    fun destroyOpenAdLoader() {
        _appOpenAdLoader?.setAdLoadListener(null)
        _appOpenAdLoader = null
    }

    override fun destroyOpenAd() {
        _appOpenAd?.setAdEventListener(null)
        _appOpenAd = null
    }

///////////////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initOpenAd()
        loadOpenAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyOpenAdLoader()
        destroyOpenAd()
        _appOpenAdLoadListener = null
        _appOpenAdEventListener = null
        super.onDestroy(owner)
    }

///////////////////////////////////////////////////////////////////////////////

    override fun onAdLoaded(appOpenAd: AppOpenAd) {
        UtilKLogWrapper.d(TAG, "onAdLoaded: ")
//        _loadingInProgress.set(false)
        // save appOpenAd for future use
        _appOpenAd = appOpenAd

        _appOpenAdLoadListener?.onAdLoaded(appOpenAd)

    }

    override fun onAdFailedToLoad(adRequestError: AdRequestError) {
        UtilKLogWrapper.d(TAG, "onAdFailedToLoad: AdRequestError $adRequestError")
//        _loadingInProgress.set(false)
        // use your own reload logic
        // NOTE: avoid continuous reloading when load errors occur
        _appOpenAdLoadListener?.onAdFailedToLoad(adRequestError)
    }

///////////////////////////////////////////////////////////////////////////////

    override fun onAdShown() {
        UtilKLogWrapper.d(TAG, "onAdShown: ")
        // Called when an app open ad has been shown
        _appOpenAdEventListener?.onAdShown()
    }

    override fun onAdFailedToShow(p0: AdError) {
        UtilKLogWrapper.d(TAG, "onAdFailedToShow: AdError $p0")
        // Called when an app open ad failed to show
        _appOpenAdEventListener?.onAdFailedToShow(p0)
    }

    override fun onAdDismissed() {
        // Called when an app open ad has been dismissed
        UtilKLogWrapper.d(TAG, "onAdDismissed: ")
        destroyOpenAd()
        _appOpenAdEventListener?.onAdDismissed()
    }

    override fun onAdClicked() {
        UtilKLogWrapper.d(TAG, "onAdClicked: ")
        // Called when user clicked on the ad
        _appOpenAdEventListener?.onAdClicked()
    }

    override fun onAdImpression(p0: ImpressionData?) {
        UtilKLogWrapper.d(TAG, "onAdImpression: ")
        // Called when an impression was observed
        _appOpenAdEventListener?.onAdImpression(p0)
    }
}