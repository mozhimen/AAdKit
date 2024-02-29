package com.mozhimen.adk.topon.basic.helpers

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdSourceStatusListener
import com.anythink.interstitial.api.ATInterstitial
import com.anythink.interstitial.api.ATInterstitialAutoAd
import com.anythink.interstitial.api.ATInterstitialAutoEventListener
import com.anythink.interstitial.api.ATInterstitialAutoLoadListener
import com.anythink.interstitial.api.ATInterstitialExListener
import com.mozhimen.adk.topon.basic.bases.BaseATInterstitialAutoLoadCallback
import com.mozhimen.adk.topon.basic.bases.BaseATInterstitialExCallback
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.annors.ACallFirstApi
import com.mozhimen.basick.lintk.annors.ACallForthApi
import com.mozhimen.basick.lintk.annors.ACallSecondApi
import com.mozhimen.basick.lintk.annors.ACallThirdApi
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_ViewReady
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
class AdKTopOnInterstitialProxy(private var _activity: Activity? = null) : BaseWakeBefDestroyLifecycleObserver() {
    private var mInterstitialAd: ATInterstitial? = null
    val atInterstitial: ATInterstitial? get() = mInterstitialAd
    private val mAutoLoadPlacementIdMap: MutableMap<String, Boolean> = HashMap()
    private var _placementId: String = ""
    private var _scenarioId: String = ""
    private var _atInterstitialAutoEventListener: ATInterstitialAutoEventListener? = null

    ////////////////////////////////////////////////////////////////////////////

    fun isAutoLoad(placementId: String, status: Boolean) {
        mAutoLoadPlacementIdMap[placementId] = status
        if (status) ATInterstitialAutoAd.addPlacementId(placementId)
        else ATInterstitialAutoAd.removePlacementId(placementId)
    }

    fun isAutoLoad(placementId: String): Boolean =
        java.lang.Boolean.TRUE == mAutoLoadPlacementIdMap[placementId]

    fun isAutoLoadReady(placementId: String): Boolean =
        ATInterstitialAutoAd.isAdReady(placementId)

    fun initAutoLoad(context: Context, atInterstitialAutoLoadListener: ATInterstitialAutoLoadListener) {
        ATInterstitialAutoAd.init(context, null, atInterstitialAutoLoadListener)
    }

    ////////////////////////////////////////////////////////////////////////////

    fun isInit(): Boolean =
        mInterstitialAd != null

    fun isAdReady(): Boolean =
        (mInterstitialAd?.isAdReady ?: false).also { Log.d(TAG, "isAdReady: $it") }

    @ACallFirstApi
    fun initInterstitialAd(
        context: Context, placementId: String, scenarioId: String,
        atInterstitialExListener: ATInterstitialExListener,
        atAdSourceStatusListener: ATAdSourceStatusListener,
        atInterstitialAutoEventListener: ATInterstitialAutoEventListener
    ) {
        Log.d(TAG, "initInterstitialAd: ")
        _placementId = placementId
        _scenarioId = scenarioId
        _atInterstitialAutoEventListener = atInterstitialAutoEventListener
        mInterstitialAd = ATInterstitial(context, placementId)
        mInterstitialAd!!.setAdListener(atInterstitialExListener)
        mInterstitialAd!!.setAdSourceStatusListener(atAdSourceStatusListener)
    }

    @OApiCall_ViewReady
    fun loadAd(width: Int = 0, height: Int = 0) {
        Log.d(TAG, "loadAd: ")
        if (width > 0 && height > 0) {
            val localMap: HashMap<String, Any> = HashMap()
            val widthOffset = Math.min(width, UtilKScreen.getWidth_ofSysMetrics())
            val heightOffset = Math.min(height, UtilKScreen.getHeight_ofSysMetrics())
            localMap.put(ATAdConst.KEY.AD_WIDTH, widthOffset /*getResources().getDisplayMetrics().widthPixels*/)
            localMap.put(ATAdConst.KEY.AD_HEIGHT, heightOffset/*getResources().getDisplayMetrics().heightPixels*/)
            mInterstitialAd?.setLocalExtra(localMap)
        }
        mInterstitialAd?.load()
    }

    @ACallThirdApi
    fun showAd(activity: Activity) {
        Log.d(TAG, "showAd: ")
        if (_placementId.isNotEmpty()) {
            ATInterstitial.entryAdScenario(_placementId, _scenarioId)
            if (isAdReady()) {
                if (isAutoLoad(_placementId)) {
                    if (_scenarioId.isNotEmpty()) ATInterstitialAutoAd.show(activity, _placementId, _scenarioId, _atInterstitialAutoEventListener)
                    else ATInterstitialAutoAd.show(activity, _placementId, _atInterstitialAutoEventListener)
                } else {
                    if (_scenarioId.isNotEmpty()) mInterstitialAd!!.show(activity, _scenarioId)
                    else mInterstitialAd?.show(activity);
                }
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.d(TAG, "onResume: _placementId $_placementId")
        if (_activity != null && _placementId.isNotEmpty()) {
            showAd(_activity!!)
            Log.d(TAG, "onResume: showAd")
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        for ((key) in mAutoLoadPlacementIdMap) {
            ATInterstitialAutoAd.removePlacementId(key)
        }
        if (mInterstitialAd != null) {
            mInterstitialAd!!.setAdSourceStatusListener(null)
            mInterstitialAd!!.setAdDownloadListener(null)
            mInterstitialAd!!.setAdListener(null)
        }
        _activity = null
        super.onDestroy(owner)
    }
}