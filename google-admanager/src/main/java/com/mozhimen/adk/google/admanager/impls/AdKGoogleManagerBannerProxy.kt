package com.mozhimen.adk.google.admanager.impls

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.mozhimen.adk.basic.commons.IAdKBannerProxy
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.adk.google.impls.AdKGoogleBannerProxy
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy

/**
 * @ClassName AdKGoogleBannerProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class AdKGoogleManagerBannerProxy : AdKGoogleBannerProxy() {
    override fun initBannerAd() {
        // 获取页面的根布局
        if (AdKGoogleMgr.isInitSuccess()) {
            _bannerAdView = AdManagerAdView(_context).apply {
                // 设置Banner的尺寸
                setAdSize(AdSize.BANNER)
                // adUnitId为Admob后台创建的Banner广告的id
                adUnitId = _adUnitId//"ca-app-pub-3940256099942544/6300978111"
                // 设置广告事件回调
                adListener = BannerAdCallback()
            }
        }
    }

    override fun getAdRequest(): AdRequest {
        return AdManagerAdRequest.Builder().build()
    }
}