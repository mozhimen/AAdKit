package com.mozhimen.adk.google.impls

import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefPauseLifecycleObserver
import com.mozhimen.basick.elemk.commons.IA_Listener
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.androidx.lifecycle.runOnMainThread

/**
 * @ClassName AdKGoogleBannerProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
class AdKGoogleBannerSimpleProxy<A>(private val _activity: A, private val _adUnitId: String) :
    BaseWakeBefPauseLifecycleObserver() where A : Activity, A : LifecycleOwner {

    private var _bannerAdView: AdView? = null

    private var _onLoadListener: IA_Listener<Boolean>? = null

    private val _bannerAdListener = object : AdListener() {
        override fun onAdLoaded() {
            super.onAdLoaded()
            // 广告加载成功
            Log.i(TAG, "banner onAdLoaded")
//            vb.btnShowBannerAd.applyVisible()
//            vb.btnHideBannerAd.applyVisible()
            _onLoadListener?.invoke(true)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            super.onAdFailedToLoad(loadAdError)
            // 广告加载失败
            Log.e(TAG, "banner onAdFailedToLoad error:${loadAdError.message}")
        }

        override fun onAdImpression() {
            super.onAdImpression()
            // 被记录为展示成功时调用
            Log.i(TAG, "banner onAdImpression")
        }

        override fun onAdClicked() {
            super.onAdClicked()
            // 被点击时调用
            Log.i(TAG, "banner onAdClicked")
        }

        override fun onAdOpened() {
            super.onAdOpened()
            // 广告落地页打开时调用
            Log.i(TAG, "banner onAdOpened")
        }

        override fun onAdClosed() {
            super.onAdClosed()
            // 广告落地页关闭时调用
            Log.i(TAG, "banner onAdClosed")
        }
    }

    //////////////////////////////////////////////////////////////////////////////////

    init {
        _activity.runOnMainThread(::initAdsBanner)
    }

    //////////////////////////////////////////////////////////////////////////////////

    fun setOnLoadListener(listener: IA_Listener<Boolean>) {
        _onLoadListener = listener
    }

    fun showBanner() {
        _bannerAdView?.alpha = 1f
    }

    fun hideBanner() {
        _bannerAdView?.alpha = 0f
    }

    //////////////////////////////////////////////////////////////////////////////////

    private fun initAdsBanner() {
        if (AdKGoogleMgr.isInitSuccess()) {
            initBannerAdView()
        }
    }

    private fun initBannerAdView() {
        // 获取页面的根布局
        val rootView = _activity.findViewById<FrameLayout>(android.R.id.content)
        _bannerAdView = AdView(_activity)
        _bannerAdView!!.run {
            // 设置Banner的尺寸
            setAdSize(AdSize.BANNER)
            // adUnitId为Admob后台创建的Banner广告的id
            adUnitId = _adUnitId//"ca-app-pub-3940256099942544/6300978111"
            // 设置广告事件回调
            adListener = _bannerAdListener
            val bannerViewLayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            // 设置显示在页面的底部中间
            bannerViewLayoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            layoutParams = bannerViewLayoutParams
            alpha = 0f
            // 把 Banner Ad 添加到根布局
            rootView.addView(this)
            //加载广告
            loadAd(AdRequest.Builder().build())
        }
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun onPause(owner: LifecycleOwner) {
        hideBanner()
        _bannerAdView?.destroy()
        _bannerAdView = null
        super.onPause(owner)
    }
}