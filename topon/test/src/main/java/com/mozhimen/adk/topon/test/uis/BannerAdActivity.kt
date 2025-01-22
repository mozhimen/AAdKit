package com.mozhimen.adk.topon.test.uis

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.ScrollView
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.mozhimen.adk.topon.basic.bases.BaseATAdSourceStatusCallback
import com.mozhimen.adk.topon.basic.bases.BaseATBannerExCallback
import com.mozhimen.adk.topon.basic.impls.AdKTopOnBannerProxy
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.test.bases.BaseActivityVDB
import com.mozhimen.adk.topon.basic.test.databinding.ActivityBannerBinding
import com.mozhimen.adk.topon.test.mos.CommonViewBean
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.view.applyVisible

/**
 * @ClassName BannerAdActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */
@OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
class BannerAdActivity : com.mozhimen.adk.topon.test.bases.BaseActivityVDB<ActivityBannerBinding>() {

    private val _adKTopOnBannerProxy by lazy_ofNone { AdKTopOnBannerProxy() }
    private val _atBannerExListener = object : BaseATBannerExCallback() {
        override fun onBannerLoaded() {
            super.onBannerLoaded()
            printLogOnUI("onBannerLoaded")
            vdb.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }

        override fun onBannerFailed(adError: AdError) {
            super.onBannerFailed(adError)
            printLogOnUI("onBannerFailed" + adError.fullErrorInfo)
        }

        override fun onBannerClicked(entity: ATAdInfo) {
            super.onBannerClicked(entity)
            printLogOnUI("onBannerClicked")
        }

        override fun onBannerShow(entity: ATAdInfo) {
            super.onBannerShow(entity)
            printLogOnUI("onBannerShow")
        }

        override fun onBannerClose(entity: ATAdInfo) {
            super.onBannerClose(entity)
            printLogOnUI("onBannerClose")
        }

        override fun onBannerAutoRefreshed(entity: ATAdInfo) {
            super.onBannerAutoRefreshed(entity)
            printLogOnUI("onBannerAutoRefreshed")
        }

        override fun onBannerAutoRefreshFail(adError: AdError) {
            super.onBannerAutoRefreshFail(adError)
            printLogOnUI("onBannerAutoRefreshFail")
        }
    }

    ////////////////////////////////////////////////////////////////////

    override val contentViewId: Int
        get() = R.layout.activity_banner

    override val adType: Int
        get() = ATAdConst.ATMixedFormatAdType.BANNER

    override fun onSelectPlacementId(placementId: String?) {
        _adKTopOnBannerProxy.apply {
            placementId?.let { initBannerAdParams(placementId, "") }
            bindLifecycle(this@BannerAdActivity)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        vdb.rlType.setSelected(true)

        //Loading and displaying ads should keep the container and BannerView visible all the time
        vdb.adviewContainer.applyVisible()

        _adKTopOnBannerProxy.initBannerView(this, _atBannerExListener, BaseATAdSourceStatusCallback())


        if (mTVShowLog != null) {
            mTVShowLog!!.setOnTouchListener(OnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_MOVE) {
                    view.parent.requestDisallowInterceptTouchEvent(true)
                }
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                false
            })
        }
        vdb.bannerLoadAdBtn.setOnClickListener {
            printLogOnUI(getString(R.string.anythink_ad_status_loading))
            _adKTopOnBannerProxy.addBannerViewToContainer(vdb.adviewContainer)
        }
    }

    override fun getCommonViewBean(): com.mozhimen.adk.topon.test.mos.CommonViewBean {
        val commonViewBean = com.mozhimen.adk.topon.test.mos.CommonViewBean()
        commonViewBean.setTitleBar(findViewById(R.id.title_bar))
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log))
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1))
        commonViewBean.setTitleResId(R.string.anythink_title_banner)
        return commonViewBean
    }

    override fun onDestroy() {
        vdb.adviewContainer.removeAllViews()
        super.onDestroy()
    }
}

