package com.mozhimen.adk.topon.basic.helpers

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Px
import androidx.lifecycle.LifecycleOwner
import com.anythink.banner.api.ATBannerExListener
import com.anythink.banner.api.ATBannerView
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdSourceStatusListener
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.elemk.commons.I_Listener
import com.mozhimen.basick.lintk.annors.ACallFirstApi
import com.mozhimen.basick.lintk.annors.ACallSecondApi
import com.mozhimen.basick.lintk.annors.ACallThirdApi
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_ViewReady
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.util.UtilKDisplayMetrics
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.view.applyVisible

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
class AdKTopOnBannerProxy : BaseWakeBefDestroyLifecycleObserver() {
    private var mBannerView: ATBannerView? = null

    ///////////////////////////////////////////////////////////////////////


    @ACallFirstApi
    fun initBannerView(atBannerExListener: ATBannerExListener, adSourceStatusListener: ATAdSourceStatusListener) {
        Log.d(TAG, "initBannerView: ")
        mBannerView = ATBannerView(_context)
        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView?.apply {
            applyVisible()
            setBannerAdListener(atBannerExListener)
            setAdSourceStatusListener(adSourceStatusListener)
        }
    }

    @ACallSecondApi
    fun addBannerViewToContainer(container: ViewGroup) {
        Log.d(TAG, "addBannerViewToContainer: ")
        if (mBannerView != null) {
            container.addView(mBannerView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        }
    }

    @ACallThirdApi
    fun initBanner(placementId: String, scenarioId: String) {
        Log.d(TAG, "initBanner: ")
        mBannerView?.setPlacementId(placementId)
        ATBannerView.entryAdScenario(placementId, scenarioId)
    }

    @OApiCall_ViewReady
    fun loadBannerAd(@Px paddingHorizontal: Int = 23f.dp2px.toInt()) {
        Log.d(TAG, "loadBannerAd: ")
        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView?.applyVisible()
        val localMap: MutableMap<String, Any> = HashMap()
        localMap[ATAdConst.KEY.AD_WIDTH] = UtilKDisplayMetrics.getSysWidthPixels() - 2 * paddingHorizontal
        localMap[ATAdConst.KEY.AD_HEIGHT] = 50f.dp2px.toInt()
        mBannerView?.setLocalExtra(localMap)

        //横幅广告使用原生自渲染广告，只需要在发起请求时额外设置setNativeAdCustomRender即可，请求、展示广告流程同横幅广告接入流程相同。
//        mBannerView!!.setNativeAdCustomRender { mixNativeAd, atAdInfo -> MediationNativeAdUtil.getViewFromNativeAd(_activity, mixNativeAd, atAdInfo, false) }
        mBannerView?.loadAd()
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onDestroy(owner: LifecycleOwner) {
        mBannerView?.apply {
            Log.d(TAG, "onPause: destroy")
            setBannerAdListener(null)
            setAdDownloadListener(null)
            setAdSourceStatusListener(null)
            destroy()
        }
        mBannerView = null
        super.onDestroy(owner)
    }
}