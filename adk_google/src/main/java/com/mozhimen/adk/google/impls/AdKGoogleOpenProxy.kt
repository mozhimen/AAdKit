package com.mozhimen.adk.google.impls

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.adk.google.AdKGoogleMgr
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
class AdKGoogleOpenProxy : BaseWakeBefDestroyLifecycleObserver(), IAdKOpenProxy {
    private var _appOpenAd: AppOpenAd? = null
    private var _adUnitId = ""

    private var _appOpenAdLoadCallback: AppOpenAdLoadCallback? = null
    private var _fullScreenContentCallback: FullScreenContentCallback? = null

    ///////////////////////////////////////////////////////////////////////////////

    fun initOpenAdListener(appOpenAdLoadListener: AppOpenAdLoadCallback?, fullScreenContentCallback: FullScreenContentCallback?) {
        _appOpenAdLoadCallback = appOpenAdLoadListener
        _fullScreenContentCallback = fullScreenContentCallback
    }

    fun initOpenAdParams(adUnitId: String) {
        _adUnitId = adUnitId
    }

    override fun initOpenAd() {
    }

    override fun loadOpenAd() {
        if (AdKGoogleMgr.isInitSuccess()) {
            AppOpenAd.load(_context, _adUnitId, AdRequest.Builder().build(), AppOpenAdLoadListener())
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun showOpenAd(activity: Activity?) {
        if (activity != null && _appOpenAd != null) {
            _appOpenAd!!.apply {
                Log.d(TAG, "showOpenAd: ")
                fullScreenContentCallback = FullScreenContentListener()
                show(activity)
            }
        }
    }

    override fun destroyOpenAd() {
        _appOpenAd?.fullScreenContentCallback = null
        _appOpenAd = null
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initOpenAd()
        loadOpenAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyOpenAd()
        _appOpenAdLoadCallback = null
        _fullScreenContentCallback = null
        super.onDestroy(owner)
    }

    ///////////////////////////////////////////////////////////////////////////////

    // 插屏广告加载状态的回调
    private inner class AppOpenAdLoadListener : AppOpenAdLoadCallback() {
        override fun onAdLoaded(p0: AppOpenAd) {
            Log.i(TAG, "open onAdLoaded")
            // 加载成功
            _appOpenAd = p0
            _appOpenAdLoadCallback?.onAdLoaded(p0)//vdb.btnShowInterstitialAd.applyVisible()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            Log.e(TAG, "onAdFailedToLoad error:${loadAdError.message}")// 加载失败
            _appOpenAdLoadCallback?.onAdFailedToLoad(loadAdError)
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    // 插屏广告相关事件回调
    private inner class FullScreenContentListener() : FullScreenContentCallback() {
        override fun onAdImpression() {
            Log.i(TAG, "interstitial onAdImpression")// 被记录为展示成功时调用
            _fullScreenContentCallback?.onAdImpression()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            // 展示失败时调用，此时销毁当前的插屏广告对象，重新加载插屏广告
            Log.e(TAG, "onAdFailedToShowFullScreenContent error:${adError.message}")
            _fullScreenContentCallback?.onAdFailedToShowFullScreenContent(adError)

//            _interstitialAd = null
//            vdb.btnShowInterstitialAd.applyInVisible()
//            loadInterstitialAd()
        }

        override fun onAdDismissedFullScreenContent() {
            Log.i(TAG, "onAdShowedFullScreenContent")// 隐藏时调用，此时销毁当前的插屏广告对象，重新加载插屏广告
            _fullScreenContentCallback?.onAdDismissedFullScreenContent()

            destroyOpenAd()
//            _interstitialAd = null
//            vdb.btnShowInterstitialAd.applyInVisible()
//            loadInterstitialAd()
        }

        override fun onAdClicked() {
            Log.i(TAG, "onAdClicked")// 被点击时调用
            _fullScreenContentCallback?.onAdClicked()
        }

        override fun onAdShowedFullScreenContent() {
            Log.i(TAG, "onAdShowedFullScreenContent")// 显示时调用
            _fullScreenContentCallback?.onAdShowedFullScreenContent()
        }
    }
}