package com.mozhimen.adk.topon.impls

import android.app.Activity
import android.content.Context
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATAdSourceStatusListener
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdExtraInfo
import com.anythink.splashad.api.ATSplashExListener
import com.anythink.splashad.api.ATSplashEyeAdListener
import com.anythink.splashad.api.ATSplashSkipInfo
import com.anythink.splashad.api.IATSplashEyeAd
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.adk.topon.helpers.SplashZoomOutManager
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.app.UtilKActivityWrapper
import com.mozhimen.kotlin.utilk.android.app.isFinishingOrDestroyed
import java.lang.ref.WeakReference

/**
 * @ClassName AdKTopOnOpenProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/11
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
class AdKTopOnOpenProxy : BaseWakeBefDestroyLifecycleObserver(), IAdKOpenProxy, ATSplashExListener, ATAdSourceStatusListener {
    private var _atOpenAd: ATSplashAd? = null
    private var _placementId: String = ""
    private var _scenarioId: String = ""
    private var _openAdSize: MutableMap<String, Any>? = null
    private var _activityRef: WeakReference<Activity>? = null

    private var _atSplashExListener: ATSplashExListener? = null
    private var _atAdSourceStatusListener: ATAdSourceStatusListener? = null

    ///////////////////////////////////////////////////////////////////////////////

    fun initOpenAdListener(atSplashExListener: ATSplashExListener?, atAdSourceStatusListener: ATAdSourceStatusListener?) {
        _atSplashExListener = atSplashExListener
        _atAdSourceStatusListener = atAdSourceStatusListener
    }

    fun initOpenAdParams(placementId: String, scenarioId: String) {
        UtilKLogWrapper.d(TAG, "initOpenAdParams: ")
        _placementId = placementId
        _scenarioId = scenarioId
    }

    fun initOpenAdSize(width: Int, height: Int) {
        //Loading and displaying ads should keep the container and BannerView visible all the time
        val localMap: MutableMap<String, Any> = HashMap()
        localMap[ATAdConst.KEY.AD_WIDTH] = width
        localMap[ATAdConst.KEY.AD_HEIGHT] = height//50f.dp2px.toInt()
        _openAdSize = localMap
    }

    override fun initOpenAd() {
        _atOpenAd = ATSplashAd(_context, _placementId, this, 5000/*defaultConfig*/).apply {
            _openAdSize?.let { setLocalExtra(it) }
            setAdSourceStatusListener(this@AdKTopOnOpenProxy)
        }
        ATSplashAd.entryAdScenario(_placementId, "")
    }

    fun isOpenAdReady(): Boolean =
        (_atOpenAd?.isAdReady ?: false).also { UtilKLogWrapper.d(TAG, "isAdReady: $it") }


    override fun loadOpenAd() {
        _atOpenAd?.loadAd()
    }

    @Deprecated("use showOpenAd(activity: Activity?, viewGroup: ViewGroup)")
    override fun showOpenAd(activity: Activity?) {
        if (activity != null) {
            showOpenAd(activity, UtilKActivityWrapper.getContentView(activity))
        }
    }

    fun showOpenAd(activity: Activity?, viewGroup: ViewGroup) {
        if (activity != null && isOpenAdReady()) {
            _activityRef = WeakReference(activity)
            if (_scenarioId.isNotEmpty()) {
                _atOpenAd?.show(activity, viewGroup, _scenarioId)
            } else
                _atOpenAd?.show(activity, viewGroup)
        }
    }

    fun showOpenAd_ofSkipInfo(activity: Activity?, viewGroup: ViewGroup, atSplashSkipInfo: ATSplashSkipInfo) {
        if (activity != null && isOpenAdReady()) {
            _activityRef = WeakReference(activity)
            if (_scenarioId.isNotEmpty()) {
                _atOpenAd?.show(activity, viewGroup, atSplashSkipInfo, _scenarioId)
            } else
                _atOpenAd?.show(activity, viewGroup, atSplashSkipInfo)
        }
    }

    override fun destroyOpenAd() {
        _atOpenAd?.apply {
            setAdListener(null)
            setAdDownloadListener(null)
            setAdSourceStatusListener(null)
            onDestory()
        }
        _atOpenAd = null
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        UtilKLogWrapper.d(TAG, "onCreate: ")
        initOpenAd()
        loadOpenAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        UtilKLogWrapper.d(TAG, "onDestroy: ")
        destroyOpenAd()
        _atSplashExListener = null
        _atAdSourceStatusListener = null
        super.onDestroy(owner)
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun onAdLoaded(p0: Boolean) {
        UtilKLogWrapper.d(TAG, "_atSplashExListener______ onAdLoaded: $p0")

        _atSplashExListener?.onAdLoaded(p0)
    }

    override fun onAdLoadTimeout() {
        UtilKLogWrapper.d(TAG, "_atSplashExListener______ onAdLoadTimeout: ")

        _atSplashExListener?.onAdLoadTimeout()
    }

    override fun onNoAdError(p0: AdError?) {
        UtilKLogWrapper.e(TAG, "_atSplashExListener______ onNoAdError: AdError $p0")

        _atSplashExListener?.onNoAdError(p0)
    }

    override fun onAdShow(p0: ATAdInfo?) {
        UtilKLogWrapper.d(TAG, "_atSplashExListener______ onAdShow: ")

        _atSplashExListener?.onAdShow(p0)
    }

    override fun onAdClick(p0: ATAdInfo?) {
        UtilKLogWrapper.d(TAG, "_atSplashExListener______ onAdClick: ")

        _atSplashExListener?.onAdClick(p0)
    }

    override fun onAdDismiss(p0: ATAdInfo?, p1: ATSplashAdExtraInfo?) {
        UtilKLogWrapper.d(TAG, "_atSplashExListener______ onAdDismiss: ATAdInfo $p0")

        showSplashEyeAd(p1?.getAtSplashEyeAd())

        _atSplashExListener?.onAdDismiss(p0, p1)
    }

    override fun onDeeplinkCallback(p0: ATAdInfo?, p1: Boolean) {
        UtilKLogWrapper.d(TAG, "_atSplashExListener______ onDeeplinkCallback: ")

        _atSplashExListener?.onDeeplinkCallback(p0, p1)
    }

    override fun onDownloadConfirm(p0: Context?, p1: ATAdInfo?, p2: ATNetworkConfirmInfo?) {
        UtilKLogWrapper.d(TAG, "_atSplashExListener______ onDownloadConfirm: ")

        _atSplashExListener?.onDownloadConfirm(p0, p1, p2)
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun onAdSourceBiddingAttempt(p0: ATAdInfo?) {
        UtilKLogWrapper.d(TAG, "_atAdSourceStatusListener onAdSourceBiddingAttempt: ")

        _atAdSourceStatusListener?.onAdSourceBiddingAttempt(p0)
    }
    override fun onAdSourceAttempt(p0: ATAdInfo?) {
        UtilKLogWrapper.d(TAG, "_atAdSourceStatusListener onAdSourceAttempt: ")

        _atAdSourceStatusListener?.onAdSourceAttempt(p0)
    }

    override fun onAdSourceBiddingFilled(p0: ATAdInfo?) {
        UtilKLogWrapper.w(TAG, "_atAdSourceStatusListener onAdSourceBiddingFilled: ")

        _atAdSourceStatusListener?.onAdSourceBiddingFilled(p0)
    }

    override fun onAdSourceLoadFilled(p0: ATAdInfo?) {
        UtilKLogWrapper.w(TAG, "_atAdSourceStatusListener onAdSourceLoadFilled: ATAdInfo $p0")

        _atAdSourceStatusListener?.onAdSourceLoadFilled(p0)
    }

    override fun onAdSourceBiddingFail(p0: ATAdInfo?, p1: AdError?) {
        UtilKLogWrapper.e(TAG, "_atAdSourceStatusListener onAdSourceBiddingFail: AdError $p1")

        _atAdSourceStatusListener?.onAdSourceBiddingFail(p0, p1)
    }

    override fun onAdSourceLoadFail(p0: ATAdInfo?, p1: AdError?) {
        UtilKLogWrapper.e(TAG, "_atAdSourceStatusListener onAdSourceLoadFail: AdError $p1")

        _atAdSourceStatusListener?.onAdSourceLoadFail(p0, p1)
    }

    ///////////////////////////////////////////////////////////////////////////////

    fun showSplashEyeAd(splashEyeAd: IATSplashEyeAd?) {
        if (_activityRef?.get() != null && _activityRef?.get()?.isFinishingOrDestroyed() != true)
            splashEyeAd?.show(_activityRef?.get(), null, object : ATSplashEyeAdListener {
                override fun onAnimationStart(splashView: View) {
                    val zoomOutManager: SplashZoomOutManager = SplashZoomOutManager.getInstance(_context)
                    val suggestedSize = splashEyeAd.getSuggestedSize(_context)
                    if (suggestedSize != null) {
                        zoomOutManager.setSplashEyeAdViewSize(suggestedSize[0], suggestedSize[1])
                    }
                    zoomOutManager.setSplashInfo(splashView, _activityRef?.get()?.let { UtilKActivityWrapper.getDecorView(it) } ?: return)
                    val contentView: ViewGroup? = _activityRef?.get()?.let { UtilKActivityWrapper.getContentView(it) }
                    zoomOutManager.startZoomOut(splashView, contentView, contentView,
                        object : SplashZoomOutManager.AnimationCallBack {
                            override fun animationStart(animationTime: Int) {}
                            override fun animationEnd() {
                                UtilKLogWrapper.i(TAG, "animationEnd---------: eye")

                                splashEyeAd.onFinished()
                            }
                        })
                }

                override fun onAdDismiss(isSupportEyeSplash: Boolean, errorMsg: String) {
                    UtilKLogWrapper.i(TAG, "onAdDismiss---------: eye")

                    val zoomOutManager: SplashZoomOutManager = SplashZoomOutManager.getInstance(_context)
                    zoomOutManager.clearStaticData()
                    splashEyeAd.destroy()
                }
            })
    }
}