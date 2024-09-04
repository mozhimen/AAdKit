package com.mozhimen.adk.inmobi.impls

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiInterstitial
import com.inmobi.ads.listeners.InterstitialAdEventListener
import com.mozhimen.adk.basic.commons.IAdKInterstitialProxy
import com.mozhimen.adk.inmobi.AdkInmobiMgr
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper

/**
 * @ClassName AdKInmobiInterstitialProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/6/6
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class AdKInmobiInterstitialProxy(
    private var _activity: Activity?,
) : BaseWakeBefDestroyLifecycleObserver(), IAdKInterstitialProxy {
    private var _interstitialAd: InMobiInterstitial? = null
    private var _adUnitId: Long = 0L
    private var _interstitialAdEventListener: InterstitialAdEventListener? = null

    //////////////////////////////////////////////////////////////////////////////////

    fun initInterstitialAdListener(interstitialAdEventListener: InterstitialAdEventListener) {
        _interstitialAdEventListener = interstitialAdEventListener
    }

    fun isReady(): Boolean =
        _interstitialAd?.isReady() == true

    //////////////////////////////////////////////////////////////////////////////////

    override fun initInterstitialAd() {
        if (AdkInmobiMgr.isInitSuccess() && _activity != null) {
            _interstitialAd = InMobiInterstitial(_activity!!, _adUnitId, InterstitialAdEventCallback())
        }
    }

    override fun loadInterstitialAd() {
        _interstitialAd?.load()
    }

    override fun showInterstitialAd() {
        _interstitialAd?.show()
    }

    override fun destroyInterstitialAd() {
        _interstitialAd = null
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initInterstitialAd()
        loadInterstitialAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyInterstitialAd()
        _interstitialAdEventListener = null
        _activity = null
        super.onDestroy(owner)
    }

    //////////////////////////////////////////////////////////////////////////////////

    private inner class InterstitialAdEventCallback() : InterstitialAdEventListener() {
        override fun onAdLoadSucceeded(inMobiInterstitial: InMobiInterstitial, adMetaInfo: AdMetaInfo) {
            UtilKLogWrapper.d(TAG, "onAdLoadSuccessful with bid ${adMetaInfo.bid} ready ${isReady()}")
            _interstitialAdEventListener?.onAdLoadSucceeded(inMobiInterstitial, adMetaInfo)
        }

        override fun onAdLoadFailed(inMobiInterstitial: InMobiInterstitial, inMobiAdRequestStatus: InMobiAdRequestStatus) {
            UtilKLogWrapper.d(TAG, "Unable to load interstitial ad (error message: ${inMobiAdRequestStatus.message}")
            _interstitialAdEventListener?.onAdLoadFailed(inMobiInterstitial, inMobiAdRequestStatus)
        }

        override fun onAdFetchSuccessful(inMobiInterstitial: InMobiInterstitial, adMetaInfo: AdMetaInfo) {
            UtilKLogWrapper.d(TAG, "onAdFetchSuccessful with bid " + adMetaInfo.bid)
            _interstitialAdEventListener?.onAdFetchSuccessful(inMobiInterstitial, adMetaInfo)
        }

        override fun onAdClicked(inMobiInterstitial: InMobiInterstitial, map: Map<Any, Any>) {
            UtilKLogWrapper.d(TAG, "onAdClicked " + map.size)
            _interstitialAdEventListener?.onAdClicked(inMobiInterstitial, map)
        }

        override fun onAdWillDisplay(inMobiInterstitial: InMobiInterstitial) {
            UtilKLogWrapper.d(TAG, "onAdWillDisplay")
            _interstitialAdEventListener?.onAdWillDisplay(inMobiInterstitial)
        }

        override fun onAdDisplayed(inMobiInterstitial: InMobiInterstitial, adMetaInfo: AdMetaInfo) {
            UtilKLogWrapper.d(TAG, "onAdDisplayed")
            _interstitialAdEventListener?.onAdDisplayed(inMobiInterstitial, adMetaInfo)
        }

        override fun onAdDisplayFailed(inMobiInterstitial: InMobiInterstitial) {
            UtilKLogWrapper.d(TAG, "onAdDisplayFailed")
            _interstitialAdEventListener?.onAdDisplayFailed(inMobiInterstitial)
        }

        override fun onAdDismissed(inMobiInterstitial: InMobiInterstitial) {
            UtilKLogWrapper.d(TAG, "onAdDismissed")
            _interstitialAdEventListener?.onAdDismissed(inMobiInterstitial)
        }

        override fun onUserLeftApplication(inMobiInterstitial: InMobiInterstitial) {
            UtilKLogWrapper.d(TAG, "onUserWillLeaveApplication")
            _interstitialAdEventListener?.onUserLeftApplication(inMobiInterstitial)
        }

        override fun onRewardsUnlocked(inMobiInterstitial: InMobiInterstitial, map: Map<Any, Any>) {
            UtilKLogWrapper.d(TAG, "onRewardsUnlocked $map")
            _interstitialAdEventListener?.onRewardsUnlocked(inMobiInterstitial, map)
        }

        override fun onAdImpression(inMobiInterstitial: InMobiInterstitial) {
            UtilKLogWrapper.d(TAG, "onAdImpression")
            _interstitialAdEventListener?.onAdImpression(inMobiInterstitial)
        }

        override fun onRequestPayloadCreationFailed(p0: InMobiAdRequestStatus) {
            UtilKLogWrapper.e(TAG, "onRequestPayloadCreationFailed $p0")
            _interstitialAdEventListener?.onRequestPayloadCreationFailed(p0)
        }

        override fun onRequestPayloadCreated(p0: ByteArray?) {
            UtilKLogWrapper.d(TAG, "onRequestPayloadCreated")
            _interstitialAdEventListener?.onRequestPayloadCreated(p0)
        }

        override fun onAdFetchFailed(p0: InMobiInterstitial, p1: InMobiAdRequestStatus) {
            UtilKLogWrapper.e(TAG, "onAdFetchFailed:")
            _interstitialAdEventListener?.onAdFetchFailed(p0, p1)
        }
    }
}