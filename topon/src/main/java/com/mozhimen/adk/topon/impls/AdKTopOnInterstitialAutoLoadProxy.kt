package com.mozhimen.adk.topon.impls

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATAdSourceStatusListener
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitialAutoAd
import com.anythink.interstitial.api.ATInterstitialAutoEventListener
import com.anythink.interstitial.api.ATInterstitialAutoLoadListener
import com.anythink.interstitial.api.ATInterstitialExListener
import com.mozhimen.adk.basic.commons.IAdKInterstitialProxy
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.app.UtilKApplicationWrapper
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone

/**
 * @ClassName AdKTopOnInterstitialProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/28
 * @Version 1.0
 */
@OApiCall_BindViewLifecycle
@OApiCall_BindLifecycle
@OApiInit_ByLazy
class AdKTopOnInterstitialAutoLoadProxy : BaseWakeBefDestroyLifecycleObserver(), ATInterstitialAutoLoadListener, IAdKInterstitialProxy {
    private val _autoLoadPlacementIdMap: MutableMap<String, Boolean> = HashMap()
    private var _atInterstitialExListener: ATInterstitialExListener? = null
    private var _atAdSourceStatusListener: ATAdSourceStatusListener? = null
    private var _atInterstitialAutoEventListener: ATInterstitialAutoEventListener? = null
    private var _atInterstitialAutoLoadListener: ATInterstitialAutoLoadListener? = null
    private val _context by lazy_ofNone { UtilKApplicationWrapper.instance.applicationContext }

    ////////////////////////////////////////////////////////////////////////////

    fun isInterstitialAdAutoLoad(placementId: String): Boolean =
        java.lang.Boolean.TRUE == _autoLoadPlacementIdMap[placementId]

    fun isInterstitialAdReady(placementId: String): Boolean =
        ATInterstitialAutoAd.isAdReady(placementId).also { UtilKLogWrapper.d(TAG, "isAdReady: $it") }

    ////////////////////////////////////////////////////////////////////////////

    fun initInterstitialAdListener(
        atInterstitialExListener: ATInterstitialExListener?,
        atAdSourceStatusListener: ATAdSourceStatusListener?,
        atInterstitialAutoEventListener: ATInterstitialAutoEventListener? = null,
        atInterstitialAutoLoadListener: ATInterstitialAutoLoadListener? = null
    ) {
        _atInterstitialExListener = atInterstitialExListener
        _atAdSourceStatusListener = atAdSourceStatusListener
        _atInterstitialAutoEventListener = atInterstitialAutoEventListener
        _atInterstitialAutoLoadListener = atInterstitialAutoLoadListener
    }

    fun initInterstitialAdParams(placementId: String) {
        _autoLoadPlacementIdMap[placementId] = true
    }

    override fun initInterstitialAd() {
        ATInterstitialAutoAd.init(_context, null, this)
    }


    override fun loadInterstitialAd(/*width: Int = 0, height: Int = 0*/) {
        for ((key) in _autoLoadPlacementIdMap) {
            ATInterstitialAutoAd.addPlacementId(key)
        }
    }

    override fun showInterstitialAd() {

    }

    fun showInterstitialAd(activity: Activity, placementId: String,atInterstitialAutoEventListener:ATInterstitialAutoEventListener, scenarioId: String = "") {
        UtilKLogWrapper.d(TAG, "showInterstitialAd: ")
        if (placementId.isNotEmpty() && isInterstitialAdAutoLoad(placementId) && isInterstitialAdReady(placementId)) {
            if (scenarioId.isNotEmpty())
                ATInterstitialAutoAd.show(activity, placementId, scenarioId, atInterstitialAutoEventListener)
            else
                ATInterstitialAutoAd.show(activity, placementId, atInterstitialAutoEventListener)
        }
    }

    fun destroyInterstitialAdAutoLoad() {
        for ((key) in _autoLoadPlacementIdMap) {
            ATInterstitialAutoAd.removePlacementId(key)
        }
        _autoLoadPlacementIdMap.clear()
    }

    override fun destroyInterstitialAd() {
    }

    ////////////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initInterstitialAd()
        loadInterstitialAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyInterstitialAdAutoLoad()
        destroyInterstitialAd()
        _atInterstitialAutoEventListener = null
        _atInterstitialExListener = null
        _atAdSourceStatusListener = null
        _atInterstitialAutoLoadListener = null
        super.onDestroy(owner)
    }

    ////////////////////////////////////////////////////////////////////////////

    override fun onInterstitialAutoLoaded(p0: String?) {
        UtilKLogWrapper.d(TAG, "_atInterstitialAutoLoadListener onInterstitialAutoLoaded:$p0")

        _atInterstitialAutoLoadListener?.onInterstitialAutoLoaded(p0)
    }

    override fun onInterstitialAutoLoadFail(p0: String?, p1: AdError?) {
        UtilKLogWrapper.d(TAG, "_atInterstitialAutoLoadListener onInterstitialAutoLoadFail:$p0 AdError $p1")

        _atInterstitialAutoLoadListener?.onInterstitialAutoLoadFail(p0, p1)
    }

/*    private inner class ATInterstitialAutoEventCallback : ATInterstitialAutoEventListener() {
        override fun onInterstitialAdClicked(adInfo: ATAdInfo) {
            UtilKLogWrapper.d(TAG, "_atInterstitialAutoEventListener onInterstitialAdClicked:$adInfo")

            _atInterstitialAutoEventListener?.onInterstitialAdClicked(adInfo)
        }

        override fun onInterstitialAdShow(adInfo: ATAdInfo) {
            UtilKLogWrapper.d(TAG, "_atInterstitialAutoEventListener onInterstitialAdShow:$adInfo")

            _atInterstitialAutoEventListener?.onInterstitialAdShow(adInfo)

        }

        override fun onInterstitialAdClose(adInfo: ATAdInfo) {
            UtilKLogWrapper.d(TAG, "_atInterstitialAutoEventListener onInterstitialAdClose:$adInfo")

            _atInterstitialAutoEventListener?.onInterstitialAdClose(adInfo)

        }

        override fun onInterstitialAdVideoStart(adInfo: ATAdInfo) {
            UtilKLogWrapper.d(TAG, "_atInterstitialAutoEventListener onInterstitialAdVideoStart:$adInfo")

            _atInterstitialAutoEventListener?.onInterstitialAdVideoStart(adInfo)

        }

        override fun onInterstitialAdVideoEnd(adInfo: ATAdInfo) {
            UtilKLogWrapper.d(TAG, "_atInterstitialAutoEventListener onInterstitialAdVideoEnd:$adInfo")

            _atInterstitialAutoEventListener?.onInterstitialAdVideoEnd(adInfo)

        }

        override fun onInterstitialAdVideoError(adError: AdError) {
            UtilKLogWrapper.e(TAG, "_atInterstitialAutoEventListener onInterstitialAdVideoError:" + adError.fullErrorInfo)

            _atInterstitialAutoEventListener?.onInterstitialAdVideoError(adError)
        }

        override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
            UtilKLogWrapper.d(TAG, "_atInterstitialAutoEventListener onDeeplinkCallback: $adInfo| isSuccess:$isSuccess")

            _atInterstitialAutoEventListener?.onDeeplinkCallback(adInfo, isSuccess)
        }

        override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
            UtilKLogWrapper.d(TAG, "_atInterstitialAutoEventListener onDownloadConfirm: $adInfo")

            _atInterstitialAutoEventListener?.onDownloadConfirm(context, adInfo, networkConfirmInfo)
        }
    }*/
}