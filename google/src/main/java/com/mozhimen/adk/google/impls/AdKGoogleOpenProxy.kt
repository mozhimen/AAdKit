package com.mozhimen.adk.google.impls

import android.app.Activity
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy

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
open class AdKGoogleOpenProxy : BaseWakeBefDestroyLifecycleObserver(), IAdKOpenProxy {
    private var _appOpenAd: AppOpenAd? = null
    private var _adUnitId = ""

    private var _appOpenAdLoadCallback: AppOpenAdLoadCallback? = null
    private var _fullScreenContentCallback: FullScreenContentCallback? = null

    ///////////////////////////////////////////////////////////////////////////////

    fun initOpenAdListener(appOpenAdLoadCallback: AppOpenAdLoadCallback?, fullScreenContentCallback: FullScreenContentCallback?) {
        _appOpenAdLoadCallback = appOpenAdLoadCallback
        _fullScreenContentCallback = fullScreenContentCallback
    }

    fun initOpenAdParams(adUnitId: String) {
        _adUnitId = adUnitId
    }

    override fun initOpenAd() {
    }

    override fun loadOpenAd() {
        if (AdKGoogleMgr.isInitSuccess()) {
            AppOpenAd.load(_context, _adUnitId, getAdRequest(), AppOpenAdLoadCallbackImpl())
        }
    }

    open fun getAdRequest(): AdRequest =
        AdRequest.Builder().build()

    ///////////////////////////////////////////////////////////////////////////////

    override fun showOpenAd(activity: Activity?) {
        if (activity != null && _appOpenAd != null) {
            _appOpenAd!!.apply {
                UtilKLogWrapper.d(TAG, "showOpenAd: ")
                fullScreenContentCallback = FullScreenContentCallbackImpl()
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
    private inner class AppOpenAdLoadCallbackImpl : AppOpenAdLoadCallback() {
        override fun onAdLoaded(p0: AppOpenAd) {
            UtilKLogWrapper.i(TAG, "onAdLoaded mediationAdapterClassName ${p0.responseInfo.mediationAdapterClassName}")
            // 加载成功
            _appOpenAd = p0
            _appOpenAdLoadCallback?.onAdLoaded(p0)//vdb.btnShowInterstitialAd.applyVisible()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            UtilKLogWrapper.e(TAG, "onAdFailedToLoad error:${loadAdError.message}")// 加载失败
            _appOpenAdLoadCallback?.onAdFailedToLoad(loadAdError)
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    // 插屏广告相关事件回调
    private inner class FullScreenContentCallbackImpl : FullScreenContentCallback() {
        override fun onAdImpression() {
            UtilKLogWrapper.i(TAG, "interstitial onAdImpression")// 被记录为展示成功时调用
            _fullScreenContentCallback?.onAdImpression()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            // 展示失败时调用，此时销毁当前的插屏广告对象，重新加载插屏广告
            UtilKLogWrapper.e(TAG, "onAdFailedToShowFullScreenContent error:${adError.message}")
            _fullScreenContentCallback?.onAdFailedToShowFullScreenContent(adError)

//            _interstitialAd = null
//            vdb.btnShowInterstitialAd.applyInVisible()
//            loadInterstitialAd()
        }

        override fun onAdDismissedFullScreenContent() {
            UtilKLogWrapper.i(TAG, "onAdShowedFullScreenContent")// 隐藏时调用，此时销毁当前的插屏广告对象，重新加载插屏广告
            _fullScreenContentCallback?.onAdDismissedFullScreenContent()

            destroyOpenAd()
//            _interstitialAd = null
//            vdb.btnShowInterstitialAd.applyInVisible()
//            loadInterstitialAd()
        }

        override fun onAdClicked() {
            UtilKLogWrapper.i(TAG, "onAdClicked")// 被点击时调用
            _fullScreenContentCallback?.onAdClicked()
        }

        override fun onAdShowedFullScreenContent() {
            UtilKLogWrapper.i(TAG, "onAdShowedFullScreenContent")// 显示时调用
            _fullScreenContentCallback?.onAdShowedFullScreenContent()
        }
    }
}