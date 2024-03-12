package com.mozhimen.adk.topon.basic.helpers

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
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.view.UtilKScreen

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
class AdKTopOnInterstitialProxy(private var _activity: Activity? = null) : BaseWakeBefDestroyLifecycleObserver(), ATInterstitialExListener, ATAdSourceStatusListener {
    private var _interstitialAd: ATInterstitial? = null
    private val _autoLoadPlacementIdMap: MutableMap<String, Boolean> = HashMap()
    private var _placementId: String = ""
    private var _scenarioId: String = ""
    private var _atInterstitialAutoEventListener: ATInterstitialAutoEventListener? = null
    private var _atInterstitialExListener: ATInterstitialExListener? = null
    private var _atAdSourceStatusListener: ATAdSourceStatusListener? = null

    private var _atInterstitialAutoEventCallback = ATInterstitialAutoEventCallback()

    ////////////////////////////////////////////////////////////////////////////

    fun isInterstitialAdAutoLoad(placementId: String, status: Boolean) {
        _autoLoadPlacementIdMap[placementId] = status
        if (status) ATInterstitialAutoAd.addPlacementId(placementId)
        else ATInterstitialAutoAd.removePlacementId(placementId)
    }

    fun isInterstitialAdAutoLoad(placementId: String): Boolean =
        java.lang.Boolean.TRUE == _autoLoadPlacementIdMap[placementId]

    fun isInterstitialAdAutoLoadReady(placementId: String): Boolean =
        ATInterstitialAutoAd.isAdReady(placementId)

    fun initInterstitialAdAutoLoad(context: Context, atInterstitialAutoLoadListener: ATInterstitialAutoLoadListener) {
        ATInterstitialAutoAd.init(context, null, atInterstitialAutoLoadListener)
    }

    fun isInterstitialAdReady(): Boolean =
        (_interstitialAd?.isAdReady ?: false).also { Log.d(TAG, "isAdReady: $it") }

    fun destroyInterstitialAdAutoLoad() {
        for ((key) in _autoLoadPlacementIdMap) {
            ATInterstitialAutoAd.removePlacementId(key)
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    fun initInterstitialAdListener(
        atInterstitialExListener: ATInterstitialExListener,
        atAdSourceStatusListener: ATAdSourceStatusListener,
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

    fun initInterstitialAd() {
        if (_activity != null) {
            Log.d(TAG, "initInterstitialAd: ")
            _interstitialAd = ATInterstitial(_activity, _placementId).apply {
                setAdSourceStatusListener(this@AdKTopOnInterstitialProxy)
            }
        }
    }

    fun loadInterstitialAd(width: Int = 0, height: Int = 0) {
        Log.d(TAG, "loadAd: ")
        if (width > 0 && height > 0) {
            val localMap: HashMap<String, Any> = HashMap()
            val widthOffset = Math.min(width, UtilKScreen.getWidth_ofSysMetrics())
            val heightOffset = Math.min(height, UtilKScreen.getHeight_ofSysMetrics())
            localMap.put(ATAdConst.KEY.AD_WIDTH, widthOffset /*getResources().getDisplayMetrics().widthPixels*/)
            localMap.put(ATAdConst.KEY.AD_HEIGHT, heightOffset/*getResources().getDisplayMetrics().heightPixels*/)
            _interstitialAd?.setLocalExtra(localMap)
        }
        _interstitialAd?.load()
    }

    fun showInterstitialAd() {
        Log.d(TAG, "showInterstitialAd: ")
        if (_placementId.isNotEmpty() && _activity != null) {
            ATInterstitial.entryAdScenario(_placementId, _scenarioId)
            if (isInterstitialAdReady()) {
                if (isInterstitialAdAutoLoad(_placementId)) {
                    if (_scenarioId.isNotEmpty()) ATInterstitialAutoAd.show(_activity!!, _placementId, _scenarioId, _atInterstitialAutoEventCallback)
                    else ATInterstitialAutoAd.show(_activity!!, _placementId, _atInterstitialAutoEventCallback)
                } else {
                    if (_scenarioId.isNotEmpty()) {
                        _interstitialAd?.apply {
                            setAdListener(this@AdKTopOnInterstitialProxy)
                            show(_activity!!)
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
    }

    fun destroyInterstitialAd() {
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
        Log.e(TAG, "onAdSourceLoadFail 广告源加载失败回调 Info: $adInfo")
        Log.e(TAG, "onAdSourceLoadFail 广告源加载失败回调 error: ${adError.getFullErrorInfo()}")
        _atAdSourceStatusListener?.onAdSourceLoadFail(adInfo, adError)
    }

    override fun onAdSourceBiddingFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError?) {
        Log.e(TAG, "onAdSourceBiddingFail 竞价广告源竞价失败回调 Info: $adInfo")
        if (adError != null) {
            Log.e(TAG, "onAdSourceBiddingFail error: ${adError.getFullErrorInfo()}")
        }
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