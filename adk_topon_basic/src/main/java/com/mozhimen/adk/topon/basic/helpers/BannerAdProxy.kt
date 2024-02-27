package com.mozhimen.adk.topon.basic.helpers

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.anythink.banner.api.ATBannerExListener
import com.anythink.banner.api.ATBannerView
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdSourceStatusListener
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
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
class BannerAdProxy(private var _activity: Context?, private val _owner: LifecycleOwner) : BaseWakeBefDestroyLifecycleObserver() {
    private var mBannerView: ATBannerView? = null

    ///////////////////////////////////////////////////////////////////////

    fun initBanner(placementId: String, scenarioId: String) {
        mBannerView!!.setPlacementId(placementId)
        ATBannerView.entryAdScenario(placementId, scenarioId)
    }

    fun initBannerView(atBannerExListener: ATBannerExListener, adSourceStatusListener: ATAdSourceStatusListener) {
        mBannerView = ATBannerView(_activity)
        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView!!.applyVisible()
        mBannerView!!.setBannerAdListener(atBannerExListener)
        mBannerView!!.setAdSourceStatusListener(adSourceStatusListener)
    }

    fun loadBannerAd(container: ViewGroup) {
        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView!!.applyVisible()
        container.applyVisible()
        val padding = 12f.dp2px.toInt()
        val localMap: MutableMap<String, Any> = HashMap()
        localMap[ATAdConst.KEY.AD_WIDTH] = UtilKDisplayMetrics.getSysWidthPixels() - 2 * padding
        localMap[ATAdConst.KEY.AD_HEIGHT] = 50f.dp2px.toInt()
        mBannerView!!.setLocalExtra(localMap)

        //横幅广告使用原生自渲染广告，只需要在发起请求时额外设置setNativeAdCustomRender即可，请求、展示广告流程同横幅广告接入流程相同。
//        mBannerView!!.setNativeAdCustomRender { mixNativeAd, atAdInfo -> MediationNativeAdUtil.getViewFromNativeAd(_activity, mixNativeAd, atAdInfo, false) }
        mBannerView!!.loadAd()
    }

    fun addBannerViewToContainer(container: ViewGroup) {
        if (mBannerView != null) {
            container.addView(mBannerView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        }
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(TAG, "onDestroy: ")
        mBannerView?.destroy()
        mBannerView = null
        _activity = null
        super.onDestroy(owner)
    }
}