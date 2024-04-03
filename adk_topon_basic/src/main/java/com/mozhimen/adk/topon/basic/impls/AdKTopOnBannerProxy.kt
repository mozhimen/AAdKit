package com.mozhimen.adk.topon.basic.impls

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.lifecycle.LifecycleOwner
import com.anythink.banner.api.ATBannerExListener
import com.anythink.banner.api.ATBannerView
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATAdSourceStatusListener
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.mozhimen.adk.basic.commons.IAdKBannerProxy
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.util.UtilKDisplayMetrics
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.view.addView_ofMatchParent

/**
 * @ClassName BannerAdProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/23
 * @Version 1.0
 */
@OApiCall_BindViewLifecycle
@OApiCall_BindLifecycle
@OApiInit_ByLazy
class AdKTopOnBannerProxy : BaseWakeBefDestroyLifecycleObserver(), ATBannerExListener, ATAdSourceStatusListener, IAdKBannerProxy {
    private var _atBannerView: ATBannerView? = null
    val bannerAdView get() = _atBannerView

    private var _placementId = ""
    private var _scenarioId = ""
    private var _bannerAdSize: MutableMap<String, Any>? = null

    private var _atBannerExListener: ATBannerExListener? = null
    private var _adSourceStatusListener: ATAdSourceStatusListener? = null

    ///////////////////////////////////////////////////////////////////////

    fun initBannerAdListener(atBannerExListener: ATBannerExListener, adSourceStatusListener: ATAdSourceStatusListener?) {
        _atBannerExListener = atBannerExListener
        _adSourceStatusListener = adSourceStatusListener
    }

    fun initBannerAdParams(placementId: String, scenarioId: String) {
        _placementId = placementId
        _scenarioId = scenarioId
    }

    override fun initBannerAdSize(width: Int, height: Int) {
        //Loading and displaying ads should keep the container and BannerView visible all the time
        val localMap: MutableMap<String, Any> = HashMap()
        localMap[ATAdConst.KEY.AD_WIDTH] = width
        localMap[ATAdConst.KEY.AD_HEIGHT] = height//50f.dp2px.toInt()
        _bannerAdSize = localMap
    }

    override fun initBannerAd() {
        Log.d(TAG, "initBannerAd: ")
        _atBannerView = ATBannerView(_context)
        //Loading and displaying ads should keep the container and BannerView visible all the time
        _atBannerView?.apply {
            _bannerAdSize?.let {
                _atBannerView?.setLocalExtra(it)
            }
            setBannerAdListener(this@AdKTopOnBannerProxy)
            setAdSourceStatusListener(this@AdKTopOnBannerProxy)
            setPlacementId(_placementId)
            ATBannerView.entryAdScenario(_placementId, _scenarioId)
        }
    }

    override fun loadBannerAd() {
        //横幅广告使用原生自渲染广告，只需要在发起请求时额外设置setNativeAdCustomRender即可，请求、展示广告流程同横幅广告接入流程相同。
//        mBannerView!!.setNativeAdCustomRender { mixNativeAd, atAdInfo -> MediationNativeAdUtil.getViewFromNativeAd(_activity, mixNativeAd, atAdInfo, false) }
        _atBannerView?.loadAd()
    }

    //////////////////////////////////////////////////////////////////////

    override fun addBannerViewToContainer(container: ViewGroup) {
        Log.d(TAG, "addBannerViewToContainer: ")
        if (_atBannerView != null) {
            container.addView_ofMatchParent(_atBannerView!!)
        }
    }

    //////////////////////////////////////////////////////////////////////

    override fun destroyBannerAd() {
        _atBannerView?.apply {
            setBannerAdListener(null)
            setAdDownloadListener(null)
            setAdSourceStatusListener(null)
            destroy()
        }
        _atBannerView = null
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initBannerAd()
        loadBannerAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyBannerAd()
        _atBannerExListener = null
        _adSourceStatusListener = null
        super.onDestroy(owner)
    }


    ///////////////////////////////////////////////////////////////////////

    override fun onBannerLoaded() {
        Log.i(TAG, "onBannerLoaded")
        _atBannerExListener?.onBannerLoaded()
    }

    override fun onBannerFailed(adError: AdError) {
        Log.i(TAG, "onBannerFailed: 横幅广告加载失败 ${adError.fullErrorInfo}")
        _atBannerExListener?.onBannerFailed(adError)
    }

    override fun onBannerAutoRefreshed(entity: ATAdInfo) {
        Log.i(TAG, "onBannerAutoRefreshed: $entity")
        _atBannerExListener?.onBannerAutoRefreshed(entity)
    }

    override fun onBannerAutoRefreshFail(adError: AdError) {
        Log.i(TAG, "onBannerAutoRefreshFail: 横幅广告自动刷新失败 ${adError.fullErrorInfo}")
        _atBannerExListener?.onBannerAutoRefreshFail(adError)
    }

    override fun onDeeplinkCallback(isRefresh: Boolean, adInfo: ATAdInfo, isSuccess: Boolean) {
        Log.i(TAG, "onDeeplinkCallback: $adInfo status: $isSuccess")
        _atBannerExListener?.onDeeplinkCallback(isRefresh, adInfo, isSuccess)
    }

    override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
        Log.i(TAG, "onDownloadConfirm: $adInfo networkConfirmInfo: $networkConfirmInfo")
        _atBannerExListener?.onDownloadConfirm(context, adInfo, networkConfirmInfo)
    }

    override fun onBannerClicked(entity: ATAdInfo) {
        Log.i(TAG, "onBannerClicked: $entity")
        _atBannerExListener?.onBannerClicked(entity)
    }

    override fun onBannerShow(entity: ATAdInfo) {
        Log.i(TAG, "onBannerShow: $entity")
        _atBannerExListener?.onBannerShow(entity)
    }

    override fun onBannerClose(entity: ATAdInfo) {
        Log.i(TAG, "onBannerClose: $entity")
        _atBannerExListener?.onBannerClose(entity)
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onAdSourceAttempt(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceAttempt: 广告源开始加载回调 $adInfo")
        _adSourceStatusListener?.onAdSourceAttempt(adInfo)
    }

    override fun onAdSourceBiddingAttempt(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceBiddingAttempt: 竞价广告源开始竞价回调 $adInfo")
        _adSourceStatusListener?.onAdSourceBiddingAttempt(adInfo)
    }

    override fun onAdSourceLoadFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError) {
        Log.e(TAG, "onAdSourceLoadFail 广告源加载失败回调 Info: $adInfo")
        Log.e(TAG, "onAdSourceLoadFail 广告源加载失败回调 error: ${adError.getFullErrorInfo()}")
        _adSourceStatusListener?.onAdSourceLoadFail(adInfo, adError)
    }

    override fun onAdSourceBiddingFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError?) {
        Log.e(TAG, "onAdSourceBiddingFail 竞价广告源竞价失败回调 Info: $adInfo")
        if (adError != null) {
            Log.e(TAG, "onAdSourceBiddingFail error: ${adError.getFullErrorInfo()}")
        }
        _adSourceStatusListener?.onAdSourceBiddingFail(adInfo, adError)
    }

    override fun onAdSourceLoadFilled(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceLoadFilled: 广告源广告填充回调 $adInfo")
        _adSourceStatusListener?.onAdSourceLoadFilled(adInfo)
    }

    override fun onAdSourceBiddingFilled(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceBiddingFilled: 竞价广告源竞价成功回调 $adInfo")
        _adSourceStatusListener?.onAdSourceBiddingFilled(adInfo)
    }
}