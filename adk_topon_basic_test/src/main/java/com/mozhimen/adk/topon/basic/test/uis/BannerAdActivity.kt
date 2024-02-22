package com.mozhimen.adk.topon.basic.test.uis

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import com.anythink.banner.api.ATBannerExListener
import com.anythink.banner.api.ATBannerView
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.basic.test.bases.BaseActivity
import com.mozhimen.adk.topon.basic.test.cons.AdConst
import com.mozhimen.adk.topon.basic.test.mos.CommonViewBean
import com.mozhimen.adk.topon.basic.test.utils.MediationNativeAdUtil
import com.mozhimen.basick.utilk.android.util.dp2px

/**
 * @ClassName BannerAdActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

class BannerAdActivity : BaseActivity(), View.OnClickListener {
    private var mBannerView: ATBannerView? = null
    private var tvLoadAdBtn: TextView? = null
    private var scrollView: ScrollView? = null
    private var mBannerViewContainer: FrameLayout? = null

    override val contentViewId: Int
        get() = R.layout.activity_banner

    override val adType: Int
        get() = ATAdConst.ATMixedFormatAdType.BANNER


    override fun onSelectPlacementId(placementId: String?) {
        mBannerView!!.setPlacementId(placementId)
        ATBannerView.entryAdScenario(placementId, AdConst.SCENARIO_ID.BANNER_AD_SCENARIO)
    }

    override fun initView() {
        super.initView()
        findViewById<RelativeLayout>(R.id.rl_type).setSelected(true)
        tvLoadAdBtn = findViewById(R.id.banner_load_ad_btn)
        mBannerViewContainer = findViewById(R.id.adview_container)

        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerViewContainer!!.visibility = View.VISIBLE
        scrollView = findViewById(R.id.scroll_view)
        initBannerView()
        addBannerViewToContainer()
    }

    override fun getCommonViewBean(): CommonViewBean {
        val commonViewBean = CommonViewBean()
        commonViewBean.setTitleBar(findViewById(R.id.title_bar))
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log))
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1))
        commonViewBean.setTitleResId(R.string.anythink_title_banner)
        return commonViewBean
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        super.initListener()
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
        tvLoadAdBtn!!.setOnClickListener(this)
    }

    private fun initBannerView() {
        mBannerView = ATBannerView(this)
        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView!!.visibility = View.VISIBLE
        mBannerView!!.setBannerAdListener(object : ATBannerExListener {
            override fun onDeeplinkCallback(isRefresh: Boolean, adInfo: ATAdInfo, isSuccess: Boolean) {
                Log.i(TAG, "onDeeplinkCallback:$adInfo--status:$isSuccess")
            }

            override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
                Log.i(TAG, "onDownloadConfirm:$adInfo networkConfirmInfo:$networkConfirmInfo")
            }

            override fun onBannerLoaded() {
                Log.i(TAG, "onBannerLoaded")
                printLogOnUI("onBannerLoaded")
                if (scrollView != null) {
                    scrollView!!.fullScroll(ScrollView.FOCUS_DOWN)
                }
            }

            override fun onBannerFailed(adError: AdError) {
                Log.i(TAG, "onBannerFailed: " + adError.fullErrorInfo)
                printLogOnUI("onBannerFailed" + adError.fullErrorInfo)
            }

            override fun onBannerClicked(entity: ATAdInfo) {
                Log.i(TAG, "onBannerClicked:$entity")
                printLogOnUI("onBannerClicked")
            }

            override fun onBannerShow(entity: ATAdInfo) {
                Log.i(TAG, "onBannerShow:$entity")
                printLogOnUI("onBannerShow")
            }

            override fun onBannerClose(entity: ATAdInfo) {
                Log.i(TAG, "onBannerClose:$entity")
                printLogOnUI("onBannerClose")
            }

            override fun onBannerAutoRefreshed(entity: ATAdInfo) {
                Log.i(TAG, "onBannerAutoRefreshed:$entity")
                printLogOnUI("onBannerAutoRefreshed")
            }

            override fun onBannerAutoRefreshFail(adError: AdError) {
                Log.i(TAG, "onBannerAutoRefreshFail: " + adError.fullErrorInfo)
                printLogOnUI("onBannerAutoRefreshFail")
            }
        })
        mBannerView!!.setAdSourceStatusListener(ATAdSourceStatusListenerImpl())
    }

    private fun loadAd() {
        printLogOnUI(getString(R.string.anythink_ad_status_loading))
        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView!!.visibility = View.VISIBLE
        mBannerViewContainer!!.visibility = View.VISIBLE
        val padding = 12f.dp2px.toInt()
        val localMap: MutableMap<String, Any> = HashMap()
        localMap[ATAdConst.KEY.AD_WIDTH] = getResources().getDisplayMetrics().widthPixels - 2 * padding
        localMap[ATAdConst.KEY.AD_HEIGHT] = 60f.dp2px.toInt()
        mBannerView!!.setLocalExtra(localMap)

        //横幅广告使用原生自渲染广告，只需要在发起请求时额外设置setNativeAdCustomRender即可，请求、展示广告流程同横幅广告接入流程相同。
        mBannerView!!.setNativeAdCustomRender { mixNativeAd, atAdInfo -> MediationNativeAdUtil.getViewFromNativeAd(this@BannerAdActivity, mixNativeAd, atAdInfo, false) }
        mBannerView!!.loadAd()
    }

    private fun addBannerViewToContainer() {
        if (mBannerViewContainer != null && mBannerView != null) {
            mBannerViewContainer!!.addView(mBannerView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mBannerViewContainer!!.layoutParams.height))
        }
    }

    override fun onDestroy() {
        if (mBannerViewContainer != null) {
            mBannerViewContainer!!.removeAllViews()
        }
        if (mBannerView != null) {
            mBannerView!!.destroy()
        }
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        if (v == null) return
        if (v.id == R.id.banner_load_ad_btn) {
            loadAd()
        }
    }

    companion object {
        private val TAG = BannerAdActivity::class.java.getSimpleName()
    }
}

