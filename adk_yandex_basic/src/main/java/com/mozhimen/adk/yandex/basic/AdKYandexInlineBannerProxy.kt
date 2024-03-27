package com.mozhimen.adk.yandex.basic

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.lifecycle.LifecycleOwner
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.util.px2dp
import com.mozhimen.basick.utilk.wrapper.UtilKScreen
import com.mozhimen.basick.utilk.android.view.addView_ofMatchParent
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import kotlin.math.max
import kotlin.math.min

/**
 * @ClassName AdKYandexInlineBannerProxy
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/3/9 0:02
 * @Version 1.0
 */
@OApiCall_BindViewLifecycle
@OApiCall_BindLifecycle
@OApiInit_ByLazy
class AdKYandexInlineBannerProxy : BaseWakeBefDestroyLifecycleObserver(), BannerAdEventListener {
    private var _bannerAdView: BannerAdView? = null
    val bannerAdView get() = _bannerAdView
    private var _bannerAdSize: BannerAdSize? = null
    private var _bannerAdEventListener: BannerAdEventListener? = null
    private var _adUnitId = ""
    private var _adFoxRequestParameters: Map<String, String>? = null

    ///////////////////////////////////////////////////////////////////////

    fun initBannerAdListener(bannerAdEventListener: BannerAdEventListener) {
        Log.d(TAG, "initBannerAdListener: ")
        _bannerAdEventListener = bannerAdEventListener
    }

    fun initBannerParams(adUnitId: String, adFoxRequestParameters: Map<String, String>? = null) {
        _adUnitId = adUnitId
        _adFoxRequestParameters = adFoxRequestParameters
    }

    fun initBannerAdSize(@Px width: Int = 0, @Px height: Int = 0) {
//        val screenHeight = resources.displayMetrics.run { heightPixels / density }.roundToInt()
        // Calculate the width of the ad, taking into account the padding in the ad container.
//        val adWidthPixels = binding.coordinatorLayout.width
//        val adWidth = (adWidthPixels / resources.displayMetrics.density).roundToInt()
//        val maxAdHeight = screenHeight / 3
        val screenWidth = min(UtilKScreen.getWidth_ofDisplayMetrics_ofSys(), UtilKScreen.getHeight_ofDisplayMetrics_ofSys())
        val screenHeight = max(UtilKScreen.getWidth_ofDisplayMetrics_ofSys(), UtilKScreen.getHeight_ofDisplayMetrics_ofSys())
        var adWidth = screenWidth
        if (width > 0)
            adWidth = width
        var adHeight = (screenHeight / 3)
        if (height > 0)
            adHeight = min(adHeight, height)
        Log.d(TAG, "initBannerAdSize: adWidth $adWidth adHeight $adHeight")
        _bannerAdSize = BannerAdSize.inlineSize(_context, adWidth.px2dp.toInt(), adHeight.px2dp.toInt())
    }

    fun initBannerAd() {
        if (_bannerAdSize != null) {
            _bannerAdView = BannerAdView(_context).apply {
                setAdUnitId(_adUnitId)
                setAdSize(_bannerAdSize!!)
                setBannerAdEventListener(this@AdKYandexInlineBannerProxy)
            }
        }
    }

    fun loadBannerAd() {
        if (_bannerAdSize != null) {
            val adRequest = if (_adFoxRequestParameters != null) {
                AdRequest.Builder().setParameters(_adFoxRequestParameters!!)
            } else {
                AdRequest.Builder()
            }.build()
            _bannerAdView?.loadAd(adRequest)
        }
    }

    ///////////////////////////////////////////////////////////////////////

    fun addBannerViewToContainer(container: ViewGroup) {
//        val params = ConstraintLayout.LayoutParams(
//            ConstraintLayout.LayoutParams.MATCH_PARENT,
//            ConstraintLayout.LayoutParams.WRAP_CONTENT,
//        ).apply {
//            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
//        }
//        binding.root.addView(bannerAd, params)
        if (_bannerAdView != null) {
            container.addView_ofMatchParent(_bannerAdView!!)
        }
    }

    ///////////////////////////////////////////////////////////////////////

    fun destroyBannerAd() {
        _bannerAdView?.destroy()
        _bannerAdView = null
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        Log.d(TAG, "onCreate: ")
        initBannerAd()
        loadBannerAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(TAG, "onDestroy: ")
        destroyBannerAd()
        super.onDestroy(owner)
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onAdLoaded() {
        Log.d(TAG, "onAdLoaded: ")
        _bannerAdEventListener?.onAdLoaded()
    }

    override fun onAdFailedToLoad(error: AdRequestError) {
        Log.e(TAG, "onAdFailedToLoad: AdRequestError $error")
        _bannerAdEventListener?.onAdFailedToLoad(error)
    }

    override fun onAdClicked() {
        Log.d(TAG, "onAdClicked: ")
        _bannerAdEventListener?.onAdClicked()
    }

    override fun onLeftApplication() {
        Log.d(TAG, "onLeftApplication: ")
        _bannerAdEventListener?.onLeftApplication()
    }

    override fun onReturnedToApplication() {
        Log.d(TAG, "onReturnedToApplication: ")
        _bannerAdEventListener?.onReturnedToApplication()
    }

    override fun onImpression(data: ImpressionData?) {
        Log.d(TAG, "onImpression: ImpressionData $data")
        _bannerAdEventListener?.onImpression(data)
    }


}