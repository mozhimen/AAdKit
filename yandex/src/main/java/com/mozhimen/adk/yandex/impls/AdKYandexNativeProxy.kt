package com.mozhimen.adk.yandex.impls

import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.mozhimen.adk.basic.commons.IAdKNativeProxy
import com.mozhimen.adk.yandex.commons.INativeAdLoadedListener
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.app.UtilKApplicationWrapper
import com.mozhimen.kotlin.utilk.android.view.addViewSafe_MATCH_MATCH
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdEventListener
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration
import com.yandex.mobile.ads.nativeads.NativeAdView

/**
 * @ClassName AdKYandexNativeProxy
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/4/2 22:56
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class AdKYandexNativeProxy :
    BaseWakeBefDestroyLifecycleObserver(), IAdKNativeProxy, NativeAdLoadListener, NativeAdEventListener {
    private var _nativeAdLoader: NativeAdLoader? = null
    private var _nativeAd: NativeAd? = null
    private var _nativeAdView: NativeAdView? = null
    private var _adUnitId: String = ""
    private var _adFoxRequestParameters: Map<String, String>? = null

    private var _nativeAdLoadListener: NativeAdLoadListener? = null
    private var _nativeAdLoadedListener: INativeAdLoadedListener? = null
    private var _nativeAdEventListener: NativeAdEventListener? = null
    private val _context by lazy_ofNone { UtilKApplicationWrapper.instance.applicationContext }

    //////////////////////////////////////////////////////////////////

    fun initNativeAdListener(nativeAdListener: NativeAdLoadListener?, nativeAdLoadedListener: INativeAdLoadedListener?, nativeAdEventListener: NativeAdEventListener?) {
        UtilKLogWrapper.d(TAG, "initNativeAdListener: ")
        _nativeAdLoadListener = nativeAdListener
        _nativeAdLoadedListener = nativeAdLoadedListener
        _nativeAdEventListener = nativeAdEventListener
    }

    fun initNativeAdParams(adUnitId: String, adFoxRequestParameters: Map<String, String>? = null) {
        _adUnitId = adUnitId
        _adFoxRequestParameters = adFoxRequestParameters
    }

    //////////////////////////////////////////////////////////////////

    override fun initNativeAd() {
        _nativeAdLoader = NativeAdLoader(_context)
        _nativeAdLoader?.setNativeAdLoadListener(this)
        val adRequest = NativeAdRequestConfiguration
            .Builder(_adUnitId)
            .setShouldLoadImagesAutomatically(true)
            .apply {
                if (_adFoxRequestParameters != null) {
                    setParameters(_adFoxRequestParameters!!)
                }
            }.build()
        _nativeAdLoader?.loadAd(adRequest)
    }

    override fun loadNativeAd() {
        UtilKLogWrapper.d(TAG, "loadNativeAd: ")
        try {
            _nativeAd?.let { nativeAd ->
                nativeAd.setNativeAdEventListener(this)
                _nativeAdLoadedListener?.onNativeAdViewLoad(nativeAd)?.apply {
                    nativeAd.bindNativeAd(this.second)
                    _nativeAdView = this.first
                }
            }
        } catch (e: Exception) {
            UtilKLogWrapper.e(TAG, "loadNativeAd: e $e")
            e.printStackTrace()
        }
    }

    override fun addNativeViewToContainer(container: ViewGroup): Boolean {
        if (_nativeAdView != null) {
            container.addViewSafe_MATCH_MATCH(_nativeAdView!!)// 把 Banner Ad 添加到根布局
            return true
        } else {
            UtilKLogWrapper.d(TAG, "addNativeViewToContainer _nativeAdView null")
            return false
        }
    }

    fun destroyNativeAdLoader() {
        _nativeAdLoader = null
    }

    override fun destroyNativeAd() {
        _nativeAd?.setNativeAdEventListener(null)
        _nativeAd = null
    }

    fun destroyNativeAdView() {
        _nativeAdView = null
    }
    //////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initNativeAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyNativeAdView()
        destroyNativeAd()
        destroyNativeAdLoader()
        _nativeAdLoadListener = null
        _nativeAdLoadedListener = null
        _nativeAdEventListener = null
        super.onDestroy(owner)
    }

    //////////////////////////////////////////////////////////////////

    override fun onAdLoaded(p0: NativeAd) {
        UtilKLogWrapper.d(TAG, "onAdLoaded: ")

        _nativeAdLoadListener?.onAdLoaded(p0)

        _nativeAd = p0

        loadNativeAd()

        _nativeAdLoadedListener?.onNativeAdViewLoaded(p0, p0.info)
    }

    override fun onAdFailedToLoad(p0: AdRequestError) {
        UtilKLogWrapper.e(TAG, "onAdFailedToLoad: $p0")

        _nativeAdLoadListener?.onAdFailedToLoad(p0)
    }

    //////////////////////////////////////////////////////////////////

    override fun onAdClicked() {
        UtilKLogWrapper.d(TAG, "onAdClicked: ")

        _nativeAdEventListener?.onAdClicked()
    }

    override fun onLeftApplication() {
        UtilKLogWrapper.d(TAG, "onLeftApplication: ")

        _nativeAdEventListener?.onLeftApplication()
    }

    override fun onReturnedToApplication() {
        UtilKLogWrapper.d(TAG, "onReturnedToApplication: ")

        _nativeAdEventListener?.onReturnedToApplication()
    }

    override fun onImpression(data: ImpressionData?) {
        UtilKLogWrapper.d(TAG, "onImpression: ")

        _nativeAdEventListener?.onImpression(data)
    }
}