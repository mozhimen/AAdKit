package com.mozhimen.adk.topon.basic.impls

import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATAdSourceStatusListener
import com.anythink.core.api.AdError
import com.anythink.nativead.api.ATNative
import com.anythink.nativead.api.ATNativeNetworkListener
import com.anythink.nativead.api.ATNativeView
import com.anythink.nativead.api.NativeAd
import com.mozhimen.adk.basic.commons.IAdKNativeProxy
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy

/**
 * @ClassName AdKTopOnNativeProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/11
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class AdKTopOnNativeProxy :
    BaseWakeBefDestroyLifecycleObserver(), IAdKNativeProxy, ATNativeNetworkListener, ATAdSourceStatusListener {
    private var _aTNative: ATNative? = null
    private var _nativeAd: NativeAd? = null
    private var _aTNativeView: ATNativeView? = null
    private var _placementId = ""
    private var _scenarioId: String = ""
    private var _nativeAdSize: MutableMap<String, Any>? = null

    private var _aTNativeNetworkListener: ATNativeNetworkListener? = null
    private var _aTAdSourceStatusListener: ATAdSourceStatusListener? = null

    ///////////////////////////////////////////////////////////////////////

    fun setVideoMute(boolean: Boolean) {
        _nativeAd?.setVideoMute(boolean)
    }

    fun resumeVideo() {
        _nativeAd?.resumeVideo()
    }

    fun pauseVideo() {
        _nativeAd?.pauseVideo()
    }

    ///////////////////////////////////////////////////////////////////////

    fun initNativeAdListener(aTNativeNetworkListener: ATNativeNetworkListener, aTAdSourceStatusListener: ATAdSourceStatusListener) {
        _aTNativeNetworkListener = aTNativeNetworkListener
        _aTAdSourceStatusListener = aTAdSourceStatusListener
    }

    fun initNativeAdParams(placementId: String, scenarioId: String) {
        _placementId = placementId
        _scenarioId = scenarioId
    }

    fun initNativeAdSize(width: Int, height: Int) {
        val localMap: MutableMap<String, Any> = HashMap()
        localMap[ATAdConst.KEY.AD_WIDTH] = width
        localMap[ATAdConst.KEY.AD_HEIGHT] = height//50f.dp2px.toInt()
        _nativeAdSize = localMap
    }

    override fun initNativeAd() {
        _aTNative = ATNative(_context, _placementId, this).apply {
            _nativeAdSize?.let { setLocalExtra(it) }
            setAdSourceStatusListener(this@AdKTopOnNativeProxy)
        }
        ATNative.entryAdScenario(_placementId, _scenarioId)
        _aTNative?.makeAdRequest()
    }

    override fun loadNativeAd() {

    }

    fun isNativeAdReady(): Boolean =
        _aTNative?.checkAdStatus()?.isReady ?: false

    override fun addNativeViewToContainer(container: ViewGroup) {
    }

    ///////////////////////////////////////////////////////////////////////

    fun destroyATNative() {
        if (_aTNative != null) {
            _aTNative!!.setAdListener(null)
            _aTNative!!.setAdSourceStatusListener(null)
        }
        _aTNative = null
    }

    override fun destroyNativeAd() {
        if (_nativeAd != null) {
            _nativeAd!!.setNativeEventListener(null)
            _nativeAd!!.destory()
        }
        _nativeAd = null
    }

    fun destroyNativeAdView() {
        _aTNativeView?.destory()
        _aTNativeView = null
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initNativeAd()
    }

    override fun onResume(owner: LifecycleOwner) {
        _nativeAd?.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        _nativeAd?.onPause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyATNative()
        destroyNativeAd()
        destroyNativeAdView()
        _aTNativeNetworkListener = null
        _aTAdSourceStatusListener = null
        super.onDestroy(owner)
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onNativeAdLoaded() {
        Log.d(TAG, "onNativeAdLoaded: ")

        _aTNativeNetworkListener?.onNativeAdLoaded()
    }

    override fun onNativeAdLoadFail(p0: AdError?) {
        Log.e(TAG, "onNativeAdLoadFail: AdError $p0")

        _aTNativeNetworkListener?.onNativeAdLoadFail(p0)
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onAdSourceBiddingAttempt(p0: ATAdInfo?) {
        Log.d(TAG, "onAdSourceBiddingAttempt: ")

        _aTAdSourceStatusListener?.onAdSourceBiddingAttempt(p0)
    }

    override fun onAdSourceBiddingFilled(p0: ATAdInfo?) {
        Log.d(TAG, "onAdSourceBiddingFilled: ")

        _aTAdSourceStatusListener?.onAdSourceBiddingFilled(p0)
    }

    override fun onAdSourceBiddingFail(p0: ATAdInfo?, p1: AdError?) {
        Log.e(TAG, "onAdSourceBiddingFail: AdError $p1")

        _aTAdSourceStatusListener?.onAdSourceBiddingFail(p0, p1)
    }

    override fun onAdSourceAttempt(p0: ATAdInfo?) {
        Log.d(TAG, "onAdSourceAttempt: ")

        _aTAdSourceStatusListener?.onAdSourceAttempt(p0)
    }

    override fun onAdSourceLoadFilled(p0: ATAdInfo?) {
        Log.d(TAG, "onAdSourceLoadFilled: ")

        _aTAdSourceStatusListener?.onAdSourceLoadFilled(p0)
    }

    override fun onAdSourceLoadFail(p0: ATAdInfo?, p1: AdError?) {
        Log.e(TAG, "onAdSourceLoadFail: AdError $p1")

        _aTAdSourceStatusListener?.onAdSourceLoadFail(p0, p1)
    }
}