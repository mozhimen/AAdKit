package com.mozhimen.adk.google.impls

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy

/**
 * @ClassName AdKYandexOpenProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/11
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
class AdKGoogleOpenProxy : BaseWakeBefDestroyLifecycleObserver() {
    private var _appOpenAd: AppOpenAd? = null
    private var _appOpenAdLoadCallback: AppOpenAdLoadCallback? = null
    private var _appOpenAdEventListener: FullScreenContentCallback? = null

    //    private var _loadingInProgress = AtomicBoolean(false)
    private var _adUnitId = ""

    ///////////////////////////////////////////////////////////////////////////////

    fun initOpenAdListener(appOpenAdLoadListener: AppOpenAdLoadListener?, appOpenAdEventListener: AppOpenAdEventListener?) {
        _appOpenAdLoadCallback = appOpenAdLoadListener
        _appOpenAdEventListener = appOpenAdEventListener
    }

    fun initOpenAdParams(adUnitId: String) {
        _adUnitId = adUnitId
    }

    fun initOpenAd() {
        _appOpenAdLoader = AppOpenAdLoader(_context).apply {
            setAdLoadListener(this@AdKGoogleOpenProxy)
        }
    }

    fun loadOpenAd() {
        // load new Ad if there is no loaded Ad and new ad isn't loading
//        if (_loadingInProgress.compareAndSet(false, true)) {
        _appOpenAdLoader?.loadAd(AdRequestConfiguration.Builder(_adUnitId).build()) ?: run {
            Log.d(TAG, "loadOpenAd: null")
        }
//        }
    }

    fun showAppOpenAd(activity: Activity?) {
        // show AppOpenAd when Application comes foreground if there is opened specific Activity
        if (activity != null && _appOpenAd != null) {
//            showAdIfAvailable(_activity)
            _appOpenAd!!.apply {
                setAdEventListener(this@AdKGoogleOpenProxy)
                show(activity)
            }
        }
    }

    fun destroyOpenAdLoader() {
        _appOpenAdLoader?.setAdLoadListener(null)
        _appOpenAdLoader = null
    }

    fun destroyOpenAd() {
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
        super.onDestroy(owner)
    }

///////////////////////////////////////////////////////////////////////////////

    override fun onAdLoaded(appOpenAd: AppOpenAd) {
        Log.d(TAG, "onAdLoaded: ")
//        _loadingInProgress.set(false)
        // save appOpenAd for future use
        _appOpenAd = appOpenAd

        _appOpenAdLoadCallback?.onAdLoaded(appOpenAd)

    }

    override fun onAdFailedToLoad(adRequestError: AdRequestError) {
        Log.d(TAG, "onAdFailedToLoad: AdRequestError $adRequestError")
//        _loadingInProgress.set(false)
        // use your own reload logic
        // NOTE: avoid continuous reloading when load errors occur
        _appOpenAdLoadCallback?.onAdFailedToLoad(adRequestError)
    }

///////////////////////////////////////////////////////////////////////////////

    override fun onAdShown() {
        Log.d(TAG, "onAdShown: ")
        // Called when an app open ad has been shown
        _appOpenAdEventListener?.onAdShown()
    }

    override fun onAdFailedToShow(p0: AdError) {
        Log.d(TAG, "onAdFailedToShow: AdError $p0")
        // Called when an app open ad failed to show
        _appOpenAdEventListener?.onAdFailedToShow(p0)
    }

    override fun onAdDismissed() {
        // Called when an app open ad has been dismissed
        Log.d(TAG, "onAdDismissed: ")
        destroyOpenAd()
        _appOpenAdEventListener?.onAdDismissed()
    }

    override fun onAdClicked() {
        Log.d(TAG, "onAdClicked: ")
        // Called when user clicked on the ad
        _appOpenAdEventListener?.onAdClicked()
    }

    override fun onAdImpression(p0: ImpressionData?) {
        Log.d(TAG, "onAdImpression: ")
        // Called when an impression was observed
        _appOpenAdEventListener?.onAdImpression(p0)
    }
}