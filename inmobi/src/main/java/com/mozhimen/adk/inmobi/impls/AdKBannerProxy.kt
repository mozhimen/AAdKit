package com.mozhimen.adk.inmobi.impls

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiBanner
import com.inmobi.ads.listeners.BannerAdEventListener
import com.mozhimen.adk.basic.commons.IAdKBannerProxy
import com.mozhimen.adk.inmobi.AdkInmobiMgr
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.android.view.addViewSafe

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
class AdKBannerProxy : BaseWakeBefDestroyLifecycleObserver(), IAdKBannerProxy {
    private var _bannerAdView: InMobiBanner? = null
    val bannerAdView get() = _bannerAdView
    private var _adUnitId: Long = 0L
    private var _enableAutoRefresh = false
    private var _adSize: Pair<Int, Int>? = null
    private var _bannerAdListener: BannerAdEventListener? = null

    //////////////////////////////////////////////////////////////////////////////////

    fun initBannerAdListener(listener: BannerAdEventListener) {
        _bannerAdListener = listener
    }

    override fun initBannerAdSize(width: Int, height: Int) {
        _adSize = width to height
    }

    fun initBannerAdParams(adUnitId: Long) {
        _adUnitId = adUnitId
    }

    fun initBannerAdAutoRefresh(enableAutoRefresh: Boolean) {
        _enableAutoRefresh = enableAutoRefresh
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun initBannerAd() {
        // 获取页面的根布局
        if (AdkInmobiMgr.isInitSuccess()) {
            _bannerAdView = InMobiBanner(_context, _adUnitId).apply {
                setAnimationType(InMobiBanner.AnimationType.ROTATE_HORIZONTAL_AXIS)
                setEnableAutoRefresh(_enableAutoRefresh)
                _adSize?.let {
                    setBannerSize(it.first, it.second)
                }
                setListener(BannerAdEventCallback())
            }
        }
    }

    override fun loadBannerAd() {
        _bannerAdView?.load()
    }

    override fun addBannerViewToContainer(container: ViewGroup) {
        UtilKLogWrapper.d(TAG, "addBannerViewToContainer: ")
        if (_bannerAdView != null) {
            container.addViewSafe(_bannerAdView!!, _adSize?.first ?: ViewGroup.LayoutParams.MATCH_PARENT, _adSize?.second ?: ViewGroup.LayoutParams.MATCH_PARENT)
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
        _bannerAdListener = null
        super.onDestroy(owner)
    }

    ///////////////////////////////////////////////////////////////////////

    private inner class BannerAdEventCallback : BannerAdEventListener() {
        override fun onAdDismissed(p0: InMobiBanner) {
            UtilKLogWrapper.d(TAG, "onAdDismissed:")
            _bannerAdListener?.onAdDismissed(p0)
        }

        override fun onAdDisplayed(p0: InMobiBanner) {
            UtilKLogWrapper.d(TAG, "onAdDisplayed:")
            _bannerAdListener?.onAdDisplayed(p0)
        }

        override fun onUserLeftApplication(p0: InMobiBanner) {
            UtilKLogWrapper.d(TAG, "onUserLeftApplication:")
            _bannerAdListener?.onUserLeftApplication(p0)
        }

        override fun onAdImpression(p0: InMobiBanner) {
            UtilKLogWrapper.d(TAG, "onAdImpression:")
            _bannerAdListener?.onAdImpression(p0)
        }

        override fun onRequestPayloadCreated(p0: ByteArray?) {
            UtilKLogWrapper.d(TAG, "onRequestPayloadCreated:")
            _bannerAdListener?.onRequestPayloadCreated(p0)
        }

        override fun onRequestPayloadCreationFailed(p0: InMobiAdRequestStatus) {
            UtilKLogWrapper.e(TAG, "onRequestPayloadCreationFailed: code ${p0.statusCode} message ${p0.message}")
            _bannerAdListener?.onRequestPayloadCreationFailed(p0)
        }

        override fun onAdFetchFailed(p0: InMobiBanner, p1: InMobiAdRequestStatus) {
            UtilKLogWrapper.e(TAG, "onAdFetchFailed: code ${p1.statusCode} message ${p1.message}")
            _bannerAdListener?.onAdFetchFailed(p0, p1)
        }

        override fun onRewardsUnlocked(p0: InMobiBanner, p1: MutableMap<Any, Any>?) {
            UtilKLogWrapper.d(TAG, "onRewardsUnlocked:")
            _bannerAdListener?.onRewardsUnlocked(p0, p1)
        }

        override fun onAdClicked(p0: InMobiBanner, p1: MutableMap<Any, Any>?) {
            UtilKLogWrapper.d(TAG, "onAdClicked:")
            _bannerAdListener?.onAdClicked(p0, p1)
        }

        override fun onAdFetchSuccessful(p0: InMobiBanner, p1: AdMetaInfo) {
            UtilKLogWrapper.d(TAG, "onAdFetchSuccessful:")
            _bannerAdListener?.onAdFetchSuccessful(p0, p1)
        }

        override fun onAdLoadFailed(p0: InMobiBanner, p1: InMobiAdRequestStatus) {
            UtilKLogWrapper.e(TAG, "onAdLoadFailed: code ${p1.statusCode} message ${p1.message}")
            _bannerAdListener?.onAdLoadFailed(p0, p1)
        }

        override fun onAdLoadSucceeded(p0: InMobiBanner, p1: AdMetaInfo) {
            UtilKLogWrapper.d(TAG, "onAdLoadSucceeded: with bid ${p1.bid}")
            _bannerAdListener?.onAdLoadSucceeded(p0, p1)
        }
    }
}