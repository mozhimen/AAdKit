package com.mozhimen.adk.inmobi.impls

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiNative
import com.inmobi.ads.listeners.NativeAdEventListener
import com.inmobi.ads.listeners.VideoEventListener
import com.mozhimen.adk.basic.commons.IAdKNativeProxy
import com.mozhimen.adk.inmobi.AdkInmobiMgr
import com.mozhimen.adk.inmobi.commons.INativeAdLoadedListener
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.basick.utilk.android.view.addViewSafe_ofMatchParent

/**
 * @ClassName AdKInmobiNativeProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/6/6
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class AdKInmobiNativeProxy : BaseWakeBefDestroyLifecycleObserver(), IAdKNativeProxy {

    private var _nativeAd: InMobiNative? = null
    private var _nativeAdView: View? = null
    private var _adUnitId: Long = 0L

    private var _nativeAdEventListener: NativeAdEventListener? = null
    private var _nativeAdLoadedListener: INativeAdLoadedListener? = null
    private var _videoEventListener: VideoEventListener? = null

    ///////////////////////////////////////////////////////////////////////

    fun initNativeAdListener(nativeAdEventListener: NativeAdEventListener?, nativeAdLoadedListener: INativeAdLoadedListener?, videoEventListener: VideoEventListener?) {
        _nativeAdEventListener = nativeAdEventListener
        _nativeAdLoadedListener = nativeAdLoadedListener
        _videoEventListener = videoEventListener
    }

    fun initNativeAdParams(adUnitId: Long) {
        _adUnitId = adUnitId
    }

    ///////////////////////////////////////////////////////////////////////

    override fun initNativeAd() {
        if (AdkInmobiMgr.isInitSuccess()) {
            _nativeAd = InMobiNative(_context, _adUnitId, NativeAdEventCallback()).apply {
                setVideoEventListener(VideoEventCallback())
            }
        }
    }

    override fun loadNativeAd() {
        _nativeAd?.load()
    }

    fun loadNativeAdView() {
        if (_nativeAd != null) {
            UtilKLogWrapper.d(TAG, "loadNativeAdView: ")
            try {
                //Pass the old ad view as the first parameter to facilitate view reuse.
                _nativeAdView = _nativeAdLoadedListener?.onNativeAdViewLoad(_nativeAd)
            } catch (e: Exception) {
                UtilKLogWrapper.e(TAG, "loadNativeAd: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    override fun addNativeViewToContainer(container: ViewGroup): Boolean {
        if (_nativeAdView != null) {
            container.addViewSafe_ofMatchParent(_nativeAdView!!)// 把 Banner Ad 添加到根布局
            return true
        } else {
            UtilKLogWrapper.d(TAG, "addNativeViewToContainer _nativeAdView null")
            return false
        }
    }

    override fun destroyNativeAd() {
        _nativeAd?.destroy()
        _nativeAd = null
    }

    fun destroyNativeAdView() {
        _nativeAdView = null
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initNativeAd()
        loadNativeAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyNativeAd()
        destroyNativeAdView()
        _videoEventListener = null
        _nativeAdEventListener = null
        _nativeAdLoadedListener = null
        super.onDestroy(owner)
    }

    ///////////////////////////////////////////////////////////////////////

    private inner class NativeAdEventCallback : NativeAdEventListener() {
        override fun onAdLoadSucceeded(inMobiNative: InMobiNative, adMetaInfo: AdMetaInfo) {
            UtilKLogWrapper.d(TAG, "onAdLoadSucceeded Could not render Strand!")

            _nativeAdEventListener?.onAdLoadSucceeded(inMobiNative, adMetaInfo)

            loadNativeAdView()

            _nativeAdLoadedListener?.onNativeAdViewLoaded(_nativeAd)
        }

        override fun onAdLoadFailed(inMobiNative: InMobiNative, inMobiAdRequestStatus: InMobiAdRequestStatus) {
            UtilKLogWrapper.e(TAG, "onAdLoadFailed (" + inMobiAdRequestStatus.message + ")")

            _nativeAdEventListener?.onAdLoadFailed(inMobiNative, inMobiAdRequestStatus)
        }

        override fun onAdFetchSuccessful(inMobiNative: InMobiNative, adMetaInfo: AdMetaInfo) {
            UtilKLogWrapper.d(TAG, "onAdFetchSuccessful with bid " + adMetaInfo.bid)

            _nativeAdEventListener?.onAdFetchSuccessful(inMobiNative, adMetaInfo)
        }

        override fun onAdFullScreenDismissed(p0: InMobiNative) {
            UtilKLogWrapper.d(TAG, "onAdFullScreenDismissed: ")

            _nativeAdEventListener?.onAdFullScreenDismissed(p0)
        }

        override fun onAdFullScreenWillDisplay(p0: InMobiNative) {
            UtilKLogWrapper.d(TAG, "onAdFullScreenWillDisplay Ad going fullscreen.")

            _nativeAdEventListener?.onAdFullScreenWillDisplay(p0)
        }

        override fun onAdFullScreenDisplayed(p0: InMobiNative) {
            UtilKLogWrapper.d(TAG, "onAdFullScreenDisplayed: ")

            _nativeAdEventListener?.onAdFullScreenDisplayed(p0)
        }

        override fun onUserWillLeaveApplication(p0: InMobiNative) {
            UtilKLogWrapper.d(TAG, "onUserWillLeaveApplication: ")

            _nativeAdEventListener?.onUserWillLeaveApplication(p0)
        }

        override fun onAdClicked(inMobiNative: InMobiNative) {
            UtilKLogWrapper.d(TAG, "onAdClicked")

            _nativeAdEventListener?.onAdClicked(inMobiNative)
        }

        override fun onAdStatusChanged(inMobiNative: InMobiNative) {
            UtilKLogWrapper.d(TAG, "onAdStatusChanged: ")

            _nativeAdEventListener?.onAdStatusChanged(inMobiNative)
        }

        override fun onAdImpression(inMobiNative: InMobiNative) {
            UtilKLogWrapper.d(TAG, "onAdImpression: ")

            _nativeAdEventListener?.onAdImpression(inMobiNative)
        }

        override fun onRequestPayloadCreationFailed(p0: InMobiAdRequestStatus) {
            UtilKLogWrapper.d(TAG, "onRequestPayloadCreationFailed: ")

            _nativeAdEventListener?.onRequestPayloadCreationFailed(p0)
        }

        override fun onRequestPayloadCreated(p0: ByteArray?) {
            UtilKLogWrapper.d(TAG, "onRequestPayloadCreated: ")

            _nativeAdEventListener?.onRequestPayloadCreated(p0)
        }

        override fun onAdClicked(p0: InMobiNative, p1: MutableMap<Any, Any>?) {
            UtilKLogWrapper.d(TAG, "onAdClicked: ")

            _nativeAdEventListener?.onAdClicked(p0, p1)
        }

        @Deprecated("Deprecated in Java")
        override fun onAdImpressed(p0: InMobiNative) {
            UtilKLogWrapper.d(TAG, "onAdImpressed: ")

            _nativeAdEventListener?.onAdImpressed(p0)
        }
    }

    private inner class VideoEventCallback : VideoEventListener() {
        override fun onVideoCompleted(ad: InMobiNative) {
            UtilKLogWrapper.d(TAG, "onVideoCompleted: ")

            _videoEventListener?.onVideoCompleted(ad)
        }

        override fun onVideoSkipped(ad: InMobiNative) {
            UtilKLogWrapper.d(TAG, "onVideoSkipped: ")

            _videoEventListener?.onVideoSkipped(ad)
        }

        override fun onAudioStateChanged(inMobiNative: InMobiNative, isMuted: Boolean) {
            UtilKLogWrapper.d(TAG, "onAudioStateChanged: ")

            _videoEventListener?.onAudioStateChanged(inMobiNative, isMuted)
        }
    }
}