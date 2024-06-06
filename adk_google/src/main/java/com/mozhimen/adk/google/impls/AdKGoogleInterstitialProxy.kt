package com.mozhimen.adk.google.impls

import android.app.Activity
import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mozhimen.adk.basic.commons.IAdKInterstitialProxy
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy

/**
 * @ClassName AdKGoogleInterstitialSimpleProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class AdKGoogleInterstitialProxy(
    private var _activity: Activity?,
) : BaseWakeBefDestroyLifecycleObserver(), IAdKInterstitialProxy {
    private var _interstitialAd: InterstitialAd? = null
    private var _adUnitId: String = ""

    private var _interstitialAdLoadCallback: InterstitialAdLoadCallback? = null
    private var _fullScreenContentCallback: FullScreenContentCallback? = null

    //////////////////////////////////////////////////////////////////////////////////

    fun initInterstitialAdListener(interstitialAdLoadCallback: InterstitialAdLoadCallback, fullScreenContentCallback: FullScreenContentCallback?) {
        _interstitialAdLoadCallback = interstitialAdLoadCallback
        _fullScreenContentCallback = fullScreenContentCallback
    }

    fun initInterstitialAdParams(adUnitId: String) {
        _adUnitId = adUnitId
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun initInterstitialAd() {
    }

    override fun loadInterstitialAd() {
        if (AdKGoogleMgr.isInitSuccess()) {
            // adUnitId为Admob后台创建的插屏广告的id
            InterstitialAd.load(_context, _adUnitId/*"ca-app-pub-3940256099942544/1033173712"*/, AdRequest.Builder().build(), InterstitialAdLoadListener())
        }
    }
    override fun showInterstitialAd() {
        if (_activity != null && _interstitialAd != null) {
            _interstitialAd!!.apply {
                fullScreenContentCallback = FullScreenContentListener()// 设置广告事件回调
                show(_activity!!)
            }
        }
    }

    override fun destroyInterstitialAd() {
        // don't forget to clean up event listener to null?
        _interstitialAd?.fullScreenContentCallback = null
        _interstitialAd = null
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initInterstitialAd()
        loadInterstitialAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyInterstitialAd()
        _interstitialAdLoadCallback = null
        _fullScreenContentCallback = null
        _activity = null
        super.onDestroy(owner)
    }

    //////////////////////////////////////////////////////////////////////////////////

    // 插屏广告加载状态的回调
    private inner class InterstitialAdLoadListener : InterstitialAdLoadCallback() {
        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            UtilKLogWrapper.i(TAG, "interstitial onAdLoaded")
            _interstitialAdLoadCallback?.onAdLoaded(interstitialAd)//vdb.btnShowInterstitialAd.applyVisible()

            // 加载成功
            _interstitialAd = interstitialAd
            // 显示插屏广告
//            this@AdsInterstitialActivity.interstitialAd?.show(this@AdsInterstitialActivity)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            UtilKLogWrapper.e(TAG, "onAdFailedToLoad error:${loadAdError.message}")// 加载失败
            _interstitialAdLoadCallback?.onAdFailedToLoad(loadAdError)
        }
    }

    //////////////////////////////////////////////////////////////////////////////////

    // 插屏广告相关事件回调
    private inner class FullScreenContentListener() : FullScreenContentCallback() {
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

            destroyInterstitialAd()
//            _interstitialAd = null
//            vdb.btnShowInterstitialAd.applyInVisible()
//            loadInterstitialAd()
        }

        override fun onAdClicked() {
            UtilKLogWrapper.i(TAG, "interstitial onAdClicked")// 被点击时调用
            _fullScreenContentCallback?.onAdClicked()
        }

        override fun onAdShowedFullScreenContent() {
            UtilKLogWrapper.i(TAG, "interstitial onAdShowedFullScreenContent")// 显示时调用
            _fullScreenContentCallback?.onAdShowedFullScreenContent()
        }
    }
}