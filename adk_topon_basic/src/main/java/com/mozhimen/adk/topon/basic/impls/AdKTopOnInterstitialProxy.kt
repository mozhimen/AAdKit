package com.mozhimen.adk.topon.basic.impls

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATAdSourceStatusListener
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitial
import com.anythink.interstitial.api.ATInterstitialAutoAd
import com.anythink.interstitial.api.ATInterstitialAutoEventListener
import com.anythink.interstitial.api.ATInterstitialAutoLoadListener
import com.anythink.interstitial.api.ATInterstitialExListener
import com.mozhimen.adk.basic.commons.IAdKInterstitialProxy
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.wrapper.UtilKScreen

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
class AdKTopOnInterstitialProxy(
    private var _activity: Activity? = null
) : BaseWakeBefDestroyLifecycleObserver(), ATInterstitialExListener, ATAdSourceStatusListener, IAdKInterstitialProxy {
    private var _interstitialAd: ATInterstitial? = null
    private val _autoLoadPlacementIdMap: MutableMap<String, Boolean> = HashMap()
    private var _placementId: String = ""
    private var _scenarioId: String = ""
    private var _interstitialAdSize: MutableMap<String, Any>? = null

    private var _atInterstitialExListener: ATInterstitialExListener? = null
    private var _atAdSourceStatusListener: ATAdSourceStatusListener? = null
    private var _atInterstitialAutoEventListener: ATInterstitialAutoEventListener? = null
//    private var _atInterstitialAutoEventCallback = ATInterstitialAutoEventCallback()

    ////////////////////////////////////////////////////////////////////////////
    //region # auto load
    fun isInterstitialAdAutoLoad(placementId: String, status: Boolean) {
        _autoLoadPlacementIdMap[placementId] = status
        if (status)
            ATInterstitialAutoAd.addPlacementId(placementId)
        else
            ATInterstitialAutoAd.removePlacementId(placementId)
    }

    fun isInterstitialAdAutoLoad(placementId: String): Boolean =
        java.lang.Boolean.TRUE == _autoLoadPlacementIdMap[placementId]

    fun isInterstitialAdAutoLoadReady(placementId: String): Boolean =
        ATInterstitialAutoAd.isAdReady(placementId)

    fun initInterstitialAdAutoLoad(context: Context, atInterstitialAutoLoadListener: ATInterstitialAutoLoadListener) {
        ATInterstitialAutoAd.init(context, null, atInterstitialAutoLoadListener)
    }

    fun destroyInterstitialAdAutoLoad() {
        for ((key) in _autoLoadPlacementIdMap) {
            ATInterstitialAutoAd.removePlacementId(key)
        }
    }
    //endregion
    ////////////////////////////////////////////////////////////////////////////

    fun initInterstitialAdListener(
        atInterstitialExListener: ATInterstitialExListener?,
        atAdSourceStatusListener: ATAdSourceStatusListener?,
        atInterstitialAutoEventListener: ATInterstitialAutoEventListener? = null
    ) {
        _atInterstitialExListener = atInterstitialExListener
        _atAdSourceStatusListener = atAdSourceStatusListener
        _atInterstitialAutoEventListener = atInterstitialAutoEventListener
    }

    fun initInterstitialAdParams(placementId: String, scenarioId: String) {
        _placementId = placementId
        _scenarioId = scenarioId
    }

    fun initOpenAdSize(width: Int, height: Int) {
        //Loading and displaying ads should keep the container and BannerView visible all the time
        val localMap: MutableMap<String, Any> = HashMap()
        localMap[ATAdConst.KEY.AD_WIDTH] = width
        localMap[ATAdConst.KEY.AD_HEIGHT] = height//50f.dp2px.toInt()
        _interstitialAdSize = localMap
    }

    override fun initInterstitialAd() {
        if (_activity != null) {
            _interstitialAd = ATInterstitial(_activity, _placementId).apply {
                _interstitialAdSize?.let { setLocalExtra(it) }
                setAdSourceStatusListener(this@AdKTopOnInterstitialProxy)
            }
            ATInterstitial.entryAdScenario(_placementId, _scenarioId)
        }
    }

    fun isInterstitialAdReady(): Boolean =
        (_interstitialAd?.isAdReady ?: false).also { Log.d(TAG, "isAdReady: $it") }

    override fun loadInterstitialAd(/*width: Int = 0, height: Int = 0*/) {
        /*        Log.d(TAG, "loadAd: ")
                if (width > 0 && height > 0) {
                    val localMap: HashMap<String, Any> = HashMap()
                    val widthOffset = Math.min(width, UtilKScreen.getWidth_ofDisplayMetrics_ofSys())
                    val heightOffset = Math.min(height, UtilKScreen.getHeight_ofDisplayMetrics_ofSys())
                    localMap.put(ATAdConst.KEY.AD_WIDTH, widthOffset )
                    localMap.put(ATAdConst.KEY.AD_HEIGHT, heightOffset)
                    _interstitialAd?.setLocalExtra(localMap)
                }*/
        _interstitialAd?.load()
    }

    override fun showInterstitialAd() {
        Log.d(TAG, "showInterstitialAd: ")
        if (_placementId.isNotEmpty() && _activity != null && isInterstitialAdReady()) {
            if (isInterstitialAdAutoLoad(_placementId)) {
                if (_scenarioId.isNotEmpty())
                    ATInterstitialAutoAd.show(_activity!!, _placementId, _scenarioId, ATInterstitialAutoEventCallback())
                else
                    ATInterstitialAutoAd.show(_activity!!, _placementId, ATInterstitialAutoEventCallback())
            } else {
                if (_scenarioId.isNotEmpty()) {
                    _interstitialAd?.apply {
                        setAdListener(this@AdKTopOnInterstitialProxy)
                        show(_activity!!, _scenarioId)
                    }
                } else {
                    _interstitialAd?.apply {
                        setAdListener(this@AdKTopOnInterstitialProxy)
                        show(_activity!!)
                    }
                }
            }
        }
    }

    override fun destroyInterstitialAd() {
        if (_interstitialAd != null) {
            _interstitialAd!!.setAdSourceStatusListener(null)
            _interstitialAd!!.setAdDownloadListener(null)
            _interstitialAd!!.setAdListener(null)
        }
        _interstitialAd = null
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
        _activity = null
        super.onDestroy(owner)
    }

    ////////////////////////////////////////////////////////////////////////////

    private inner class ATInterstitialAutoEventCallback : ATInterstitialAutoEventListener() {
        override fun onInterstitialAdClicked(adInfo: ATAdInfo) {
            Log.i(TAG, "onInterstitialAdClicked:$adInfo")
            _atInterstitialAutoEventListener?.onInterstitialAdClicked(adInfo)
        }

        override fun onInterstitialAdShow(adInfo: ATAdInfo) {
            Log.i(TAG, "onInterstitialAdShow:$adInfo")
            _atInterstitialAutoEventListener?.onInterstitialAdShow(adInfo)

        }

        override fun onInterstitialAdClose(adInfo: ATAdInfo) {
            Log.i(TAG, "onInterstitialAdClose:$adInfo")
            _atInterstitialAutoEventListener?.onInterstitialAdClose(adInfo)

        }

        override fun onInterstitialAdVideoStart(adInfo: ATAdInfo) {
            Log.i(TAG, "onInterstitialAdVideoStart:$adInfo")
            _atInterstitialAutoEventListener?.onInterstitialAdVideoStart(adInfo)

        }

        override fun onInterstitialAdVideoEnd(adInfo: ATAdInfo) {
            Log.i(TAG, "onInterstitialAdVideoEnd:$adInfo")
            _atInterstitialAutoEventListener?.onInterstitialAdVideoEnd(adInfo)

        }

        override fun onInterstitialAdVideoError(adError: AdError) {
            Log.i(TAG, "onInterstitialAdVideoError:" + adError.fullErrorInfo)
            _atInterstitialAutoEventListener?.onInterstitialAdVideoError(adError)
        }

        override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
            Log.i(TAG, "onDeeplinkCallback: $adInfo| isSuccess:$isSuccess")
            _atInterstitialAutoEventListener?.onDeeplinkCallback(adInfo, isSuccess)
        }

        override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
            Log.i(TAG, "onDownloadConfirm: $adInfo")
            _atInterstitialAutoEventListener?.onDownloadConfirm(context, adInfo, networkConfirmInfo)

        }
    }

    ////////////////////////////////////////////////////////////////////////////

    override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
        Log.i(TAG, "onDeeplinkCallback:$adInfo--status:$isSuccess")
        _atInterstitialExListener?.onDeeplinkCallback(adInfo, isSuccess)
    }

    override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
        Log.i(TAG, "onDownloadConfirm: adInfo=$adInfo")
        _atInterstitialExListener?.onDownloadConfirm(context, adInfo, networkConfirmInfo)
    }

    override fun onInterstitialAdLoaded() {
        Log.i(TAG, "onInterstitialAdLoaded")
        _atInterstitialExListener?.onInterstitialAdLoaded()
    }

    override fun onInterstitialAdLoadFail(adError: AdError) {
        Log.i(TAG, "onInterstitialAdLoadFail:${adError.fullErrorInfo}")
        _atInterstitialExListener?.onInterstitialAdLoadFail(adError)
    }

    override fun onInterstitialAdClicked(entity: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdClicked:$entity")
        _atInterstitialExListener?.onInterstitialAdClicked(entity)
    }

    override fun onInterstitialAdShow(entity: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdShow:$entity")
        _atInterstitialExListener?.onInterstitialAdShow(entity)
    }

    override fun onInterstitialAdClose(entity: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdClose:$entity")
        _atInterstitialExListener?.onInterstitialAdClose(entity)
    }

    override fun onInterstitialAdVideoStart(entity: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdVideoStart:$entity")
        _atInterstitialExListener?.onInterstitialAdVideoStart(entity)
    }

    override fun onInterstitialAdVideoEnd(entity: ATAdInfo) {
        Log.i(TAG, "onInterstitialAdVideoEnd:$entity")
        _atInterstitialExListener?.onInterstitialAdVideoEnd(entity)
    }

    override fun onInterstitialAdVideoError(adError: AdError) {
        Log.i(TAG, "onInterstitialAdVideoError:${adError.fullErrorInfo}")
        _atInterstitialExListener?.onInterstitialAdVideoError(adError)
    }

    ////////////////////////////////////////////////////////////////////////////

    override fun onAdSourceAttempt(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceAttempt: 广告源开始加载回调 $adInfo")
        _atAdSourceStatusListener?.onAdSourceAttempt(adInfo)
    }

    override fun onAdSourceBiddingAttempt(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceBiddingAttempt: 竞价广告源开始竞价回调 $adInfo")
        _atAdSourceStatusListener?.onAdSourceBiddingAttempt(adInfo)
    }

    override fun onAdSourceLoadFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError) {
        Log.e(TAG, "onAdSourceLoadFail 广告源加载失败回调 Info: $adInfo \n error: ${adError.getFullErrorInfo()}")
        _atAdSourceStatusListener?.onAdSourceLoadFail(adInfo, adError)
    }

    override fun onAdSourceBiddingFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError?) {
        Log.e(TAG, "onAdSourceBiddingFail 竞价广告源竞价失败回调 Info: $adInfo error: ${adError?.getFullErrorInfo()}")
        _atAdSourceStatusListener?.onAdSourceBiddingFail(adInfo, adError)
    }

    override fun onAdSourceLoadFilled(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceLoadFilled: 广告源广告填充回调 $adInfo")
        _atAdSourceStatusListener?.onAdSourceLoadFilled(adInfo)
    }

    override fun onAdSourceBiddingFilled(adInfo: ATAdInfo) {
        Log.i(TAG, "onAdSourceBiddingFilled: 竞价广告源竞价成功回调 $adInfo")
        _atAdSourceStatusListener?.onAdSourceBiddingFilled(adInfo)
    }
}