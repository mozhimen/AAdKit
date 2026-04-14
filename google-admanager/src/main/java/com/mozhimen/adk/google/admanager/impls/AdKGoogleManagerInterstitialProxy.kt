package com.mozhimen.adk.google.admanager.impls

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.adk.google.impls.AdKGoogleInterstitialProxy
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy

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
class AdKGoogleManagerInterstitialProxy(
    activity: Activity?,
) : AdKGoogleInterstitialProxy(activity) {

    override fun loadInterstitialAd() {
        if (AdKGoogleMgr.isInitSuccess()) {
            // adUnitId为Admob后台创建的插屏广告的id
            AdManagerInterstitialAd.load(_context, _adUnitId/*"ca-app-pub-3940256099942544/1033173712"*/, getAdRequest(), InterstitialAdLoadCallbackImpl())
        }
    }

    override fun getAdRequest(): AdRequest {
        return AdManagerAdRequest.Builder().build()
    }
}