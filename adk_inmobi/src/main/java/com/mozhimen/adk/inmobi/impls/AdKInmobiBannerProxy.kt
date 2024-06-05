package com.mozhimen.adk.inmobi.impls

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiBanner
import com.inmobi.ads.listeners.BannerAdEventListener
import com.mozhimen.adk.basic.commons.IAdKBannerProxy
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.basick.utilk.android.view.addView_ofMatchParent

/**
 * @ClassName AdKInmobiBannerProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/6/5
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class AdKInmobiBannerProxy : BaseWakeBefDestroyLifecycleObserver(), IAdKBannerProxy {
    private var _bannerAdView: InMobiBanner? = null
    val bannerAdView get() = _bannerAdView
    private var _adUnitId: String = ""

    //////////////////////////////////////////////////////////////////////////////////

    fun initBannerAdListener(listener: AdListener) {
        UtilKLogWrapper.d(TAG, "initBannerAdListener: ")
        _bannerAdListener = listener
    }

    fun initBannerAdParams(adUnitId: String) {
        _adUnitId = adUnitId
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun initBannerAd() {
    }

    override fun loadBannerAd() {
    }

    override fun initBannerAdSize(width: Int, height: Int) {
    }

    override fun addBannerViewToContainer(container: ViewGroup) {
        UtilKLogWrapper.d(TAG, "addBannerViewToContainer: ")
        if (_bannerAdView != null) {
            container.addView_ofMatchParent(_bannerAdView!!)
        }
    }

    override fun destroyBannerAd() {
        _bannerAdView?.apply {
            destroy()
        }
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

    private inner class BannerAdEventCallback() : BannerAdEventListener() {
        override fun onAdDismissed(p0: InMobiBanner) {
            UtilKLogWrapper.d(TAG, "onAdDismissed:")
        }

        override fun onAdDisplayed(p0: InMobiBanner) {
            UtilKLogWrapper.d(TAG, "onAdDisplayed:")
        }

        override fun onUserLeftApplication(p0: InMobiBanner) {
            UtilKLogWrapper.d(TAG, "onUserLeftApplication:")
        }

        override fun onAdImpression(p0: InMobiBanner) {
            UtilKLogWrapper.d(TAG, "onAdImpression:")
        }

        override fun onRequestPayloadCreated(p0: ByteArray?) {
            UtilKLogWrapper.d(TAG, "onRequestPayloadCreated:")
        }

        override fun onRequestPayloadCreationFailed(p0: InMobiAdRequestStatus) {
            UtilKLogWrapper.d(TAG, "onRequestPayloadCreationFailed:")
        }

        override fun onAdFetchFailed(p0: InMobiBanner, p1: InMobiAdRequestStatus) {
            UtilKLogWrapper.d(TAG, "onAdFetchFailed:")
        }

        override fun onRewardsUnlocked(p0: InMobiBanner, p1: MutableMap<Any, Any>?) {
            UtilKLogWrapper.d(TAG, "onRewardsUnlocked:")
        }

        override fun onAdClicked(p0: InMobiBanner, p1: MutableMap<Any, Any>?) {
            UtilKLogWrapper.d(TAG, "onAdClicked:")
        }

        override fun onAdFetchSuccessful(p0: InMobiBanner, p1: AdMetaInfo) {
            UtilKLogWrapper.d(TAG, "onAdFetchSuccessful:")
        }

        override fun onAdLoadFailed(p0: InMobiBanner, p1: InMobiAdRequestStatus) {
            UtilKLogWrapper.d(TAG, "onAdLoadFailed:")
        }

        override fun onAdLoadSucceeded(p0: InMobiBanner, p1: AdMetaInfo) {
            UtilKLogWrapper.d(TAG, "onAdLoadSucceeded:")
        }

        @Deprecated("Deprecated in Java")
        override fun onAdLoadSucceeded(p0: InMobiBanner) {
            UtilKLogWrapper.d(TAG, "onAdLoadSucceeded:")
        }
    }
}