package com.mozhimen.adk.google.impls

import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.mozhimen.adk.basic.commons.IAdKBannerProxy
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.annors.Dp
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.view.addView_ofMatchParent

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
class AdKGoogleBannerProxy :
    BaseWakeBefDestroyLifecycleObserver(), IAdKBannerProxy {
    private var _bannerAdView: AdView? = null
    val bannerAdView get() = _bannerAdView
    private var _bannerAdSize: AdSize? = null
    private var _adUnitId: String = ""
    private var _bannerAdListener: AdListener? = null

    //////////////////////////////////////////////////////////////////////////////////

    fun initBannerAdListener(listener: AdListener) {
        Log.d(TAG, "initBannerAdListener: ")
        _bannerAdListener = listener
    }

    fun initBannerAdParams(adUnitId: String) {
        _adUnitId = adUnitId
    }

    fun initBannerAdSize(@Dp width: Int) {
        initBannerAdSize(width, 0)
    }

    override fun initBannerAdSize(@Dp width: Int,@Dp height: Int) {
        _bannerAdSize = AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(_context, width)
    }

    override fun initBannerAd() {
        // 获取页面的根布局
        if (AdKGoogleMgr.isInitSuccess()) {
            _bannerAdView = AdView(_context).apply {
                // 设置Banner的尺寸
                setAdSize(AdSize.BANNER)
                // adUnitId为Admob后台创建的Banner广告的id
                adUnitId = _adUnitId//"ca-app-pub-3940256099942544/6300978111"
                // 设置广告事件回调
                adListener = BannerAdListener()
            }
        }
    }

    fun showBannerAd() {
        _bannerAdView?.alpha = 1f
    }

    fun hideBannerAd() {
        _bannerAdView?.alpha = 0f
    }

    override fun loadBannerAd() {
        _bannerAdView?.loadAd(AdRequest.Builder().build())
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun addBannerViewToContainer(container: ViewGroup) {
        if (_bannerAdView != null) {
//            val bannerViewLayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
//            // 设置显示在页面的底部中间
//            bannerViewLayoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
//            layoutParams = bannerViewLayoutParams
            container.addView_ofMatchParent(_bannerAdView!!)// 把 Banner Ad 添加到根布局
        }
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun destroyBannerAd() {
        _bannerAdView?.destroy()
        _bannerAdView = null
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        Log.d(TAG, "onCreate: ")
        initBannerAd()
        loadBannerAd()
    }

    override fun onResume(owner: LifecycleOwner) {
        _bannerAdView?.resume()
    }

    override fun onPause(owner: LifecycleOwner) {
        _bannerAdView?.pause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(TAG, "onDestroy: ")
//        hideBanner()
        destroyBannerAd()
        _bannerAdListener = null
        super.onDestroy(owner)
    }

    //////////////////////////////////////////////////////////////////////////////////

    private inner class BannerAdListener : AdListener() {
        override fun onAdLoaded() {
            // 广告加载成功
            Log.i(TAG, "banner onAdLoaded")
//            vdb.btnShowBannerAd.applyVisible()
//            vdb.btnHideBannerAd.applyVisible()
            _bannerAdListener?.onAdLoaded()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            // 广告加载失败
            Log.e(TAG, "banner onAdFailedToLoad error:${loadAdError.message}")
            _bannerAdListener?.onAdFailedToLoad(loadAdError)
        }

        override fun onAdImpression() {
            // 被记录为展示成功时调用
            Log.i(TAG, "banner onAdImpression")
            _bannerAdListener?.onAdImpression()
        }

        override fun onAdClicked() {
            // 被点击时调用
            Log.i(TAG, "banner onAdClicked")
            _bannerAdListener?.onAdClicked()
        }

        override fun onAdOpened() {
            // 广告落地页打开时调用
            Log.i(TAG, "banner onAdOpened")
            _bannerAdListener?.onAdOpened()
        }

        override fun onAdClosed() {
            // 广告落地页关闭时调用
            Log.i(TAG, "banner onAdClosed")
            _bannerAdListener?.onAdClosed()
        }
    }
}