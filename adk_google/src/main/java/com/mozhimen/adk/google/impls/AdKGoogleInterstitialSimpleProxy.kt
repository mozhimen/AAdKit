package com.mozhimen.adk.google.impls

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mozhimen.adk.google.AdKGoogle
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.androidx.lifecycle.runOnMainThread
import java.lang.ref.WeakReference

/**
 * @ClassName AdKGoogleInterstitialSimpleProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
interface IAdKGoogleInterstitialListener {
    fun onAdLoaded()
    fun onAdDismissedFullScreenContent()
    fun onAdFailedToShowFullScreenContent()
}

@OApiInit_ByLazy
@OApiCall_BindLifecycle
class AdKGoogleInterstitialSimpleProxy<A>(private var _activityRef: WeakReference<A>, private val _adUnitId: String) : BaseWakeBefDestroyLifecycleObserver() where A : Activity, A : LifecycleOwner {
    private var _interstitialAd: InterstitialAd? = null

    private var _adkGoogleInterstitialListener: IAdKGoogleInterstitialListener? = null

    // 插屏广告加载状态的回调
    private val _interstitialAdLoadCallback = object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            super.onAdLoaded(interstitialAd)
            Log.i(TAG, "interstitial onAdLoaded")
            // 加载成功
            this@AdKGoogleInterstitialSimpleProxy._interstitialAd = interstitialAd
            // 设置广告事件回调
            this@AdKGoogleInterstitialSimpleProxy._interstitialAd?.fullScreenContentCallback = _interstitialAdCallback
            // 显示插屏广告
//            this@AdsInterstitialActivity.interstitialAd?.show(this@AdsInterstitialActivity)

            _adkGoogleInterstitialListener?.onAdLoaded()//vb.btnShowInterstitialAd.applyVisible()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            super.onAdFailedToLoad(loadAdError)
            // 加载失败
            Log.e(TAG, "interstitial onAdFailedToLoad error:${loadAdError.message}")
        }
    }

    // 插屏广告相关事件回调
    private val _interstitialAdCallback = object : FullScreenContentCallback() {
        override fun onAdImpression() {
            super.onAdImpression()
            // 被记录为展示成功时调用
            Log.i(TAG, "interstitial onAdImpression")
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            // 显示时调用
            Log.i(TAG, "interstitial onAdShowedFullScreenContent")
        }

        override fun onAdClicked() {
            super.onAdClicked()
            // 被点击时调用
            Log.i(TAG, "interstitial onAdClicked")
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            // 隐藏时调用，此时销毁当前的插屏广告对象，重新加载插屏广告
            _interstitialAd = null
//            vb.btnShowInterstitialAd.applyInVisible()
            _adkGoogleInterstitialListener?.onAdDismissedFullScreenContent()
            loadInterstitialAd()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            super.onAdFailedToShowFullScreenContent(adError)
            // 展示失败时调用，此时销毁当前的插屏广告对象，重新加载插屏广告
            Log.e(TAG, "interstitial onAdFailedToShowFullScreenContent error:${adError.message}")
            _interstitialAd = null
//            vb.btnShowInterstitialAd.applyInVisible()
            _adkGoogleInterstitialListener?.onAdFailedToShowFullScreenContent()
            loadInterstitialAd()
        }
    }

    //////////////////////////////////////////////////////////////////////////////////

    init {
        _activityRef.get()?.runOnMainThread(::initAdsInterstitial)
    }

    //////////////////////////////////////////////////////////////////////////////////

    fun setAdkGoogleInterstitialListener(listener: IAdKGoogleInterstitialListener) {
        _adkGoogleInterstitialListener = listener
    }

    fun showInterstitialAd() {
        if (_activityRef.get() != null) {
            _interstitialAd?.show(_activityRef.get()!!)
        }
    }

    //////////////////////////////////////////////////////////////////////////////////


    private fun initAdsInterstitial() {
        if (AdKGoogle.isInitSuccess()) {
            loadInterstitialAd()
        }
    }

    private fun loadInterstitialAd() {
        // adUnitId为Admob后台创建的插屏广告的id
        InterstitialAd.load(_context, _adUnitId/*"ca-app-pub-3940256099942544/1033173712"*/, AdRequest.Builder().build(), _interstitialAdLoadCallback)
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun onDestroy(owner: LifecycleOwner) {
        _interstitialAd = null
        _adkGoogleInterstitialListener = null
        super.onDestroy(owner)
    }
}