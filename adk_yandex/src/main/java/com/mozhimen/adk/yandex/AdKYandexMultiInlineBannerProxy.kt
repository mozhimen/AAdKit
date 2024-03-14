package com.mozhimen.adk.yandex

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.lifecycle.LifecycleOwner
import com.mozhimen.adk.yandex.optins.OMetaData_YANDEX_ADS_APPLICATION_ID
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.util.px2dp
import com.mozhimen.basick.utilk.android.view.UtilKScreen
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
@OMetaData_YANDEX_ADS_APPLICATION_ID
@OApiCall_BindViewLifecycle
@OApiCall_BindLifecycle
@OApiInit_ByLazy
class AdKYandexMultiInlineBannerProxy : BaseWakeBefDestroyLifecycleObserver(), BannerAdEventListener {
    private var _bannerAdEventListener: BannerAdEventListener? = null
    private var _adUnitId = ""
    private var _adFoxRequestParameters: Map<String, String>? = null
    private var _width: Int = 0
    private var _height: Int = 0
    private var _viewsCache = HashMap<String, BannerAdView>()

    ///////////////////////////////////////////////////////////////////////

    fun initBannerAdListener(bannerAdEventListener: BannerAdEventListener) {
        _bannerAdEventListener = bannerAdEventListener
    }

    fun initBannerParams(adUnitId: String, adFoxRequestParameters: Map<String, String>? = null) {
        _adUnitId = adUnitId
        _adFoxRequestParameters = adFoxRequestParameters
    }

    fun initBannerAdSize(@Px width: Int = 0, @Px height: Int = 0) {
        _width = width
        _height = height
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
            container.addView(_bannerAdView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }
    }

    ///////////////////////////////////////////////////////////////////////

    fun destroyBannerAd() {
        _bannerAdView?.destroy()
        _bannerAdView = null
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initBannerAd()
        loadBannerAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
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