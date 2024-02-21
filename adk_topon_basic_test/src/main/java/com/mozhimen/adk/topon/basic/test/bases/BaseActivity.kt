package com.mozhimen.adk.topon.basic.test.bases

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.CallSuper
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATAdSourceStatusListener
import com.mozhimen.adk.topon.basic.test.annors.AAdNativeType
import com.mozhimen.adk.topon.basic.test.annors.AnnotationAdType
import com.mozhimen.adk.topon.basic.test.mos.CommonViewBean
import com.mozhimen.adk.topon.basic.test.utils.PlacementIdUtil
import com.mozhimen.basick.utilk.android.widget.applyPrintLog
import com.mozhimen.uicorek.bark.title.BarKTitle
import java.lang.ref.WeakReference

abstract class BaseActivity : Activity() {
    protected var mCurrentPlacementName: String? = null
    protected var mCurrentPlacementId: String? = null
    private var mPlacementIdMap: Map<String, String>? = null
    private var mCommonViewBean: CommonViewBean? = null
    protected var mTVShowLog: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(contentViewId)
        initView()
        initListener()
        initData()
    }

    protected abstract val contentViewId: Int

    @get:AnnotationAdType
    protected abstract val adType: Int

    protected abstract fun onSelectPlacementId(placementId: String?)
    protected fun initView() {
        initViewWithCommonView(commonViewBean)
    }

    protected fun initListener() {}

    @CallSuper
    protected fun initData() {
        initPlacementIdMap(adType)
        if (mCommonViewBean != null) {
            initPlacementListAdapter(mCommonViewBean!!.getSpinnerSelectPlacement())
        }
    }

    protected val commonViewBean: CommonViewBean?
        protected get() = null

    private fun setTitleBar(titleBar: BarKTitle?, titleResId: Int) {
        if (titleBar != null && titleResId != 0) {
            titleBar.setTitle(titleResId)
            titleBar.setListener { v -> finish() }
        }
    }

    private fun initViewWithCommonView(commonViewBean: CommonViewBean?) {
        mCommonViewBean = commonViewBean
        if (commonViewBean != null) {
            val titleBar: BarKTitle? = commonViewBean.getTitleBar()
            if (titleBar != null) {
                setTitleBar(titleBar, commonViewBean.getTitleResId())
            }
            mTVShowLog = commonViewBean.getTvLogView()
            if (mTVShowLog != null) {
                mTVShowLogWR = WeakReference(mTVShowLog)
                mTVShowLog!!.movementMethod = ScrollingMovementMethod.getInstance()
            }
        }
    }

    protected val nativeAdType: String
        protected get() = AAdNativeType.NATIVE_SELF_RENDER_TYPE

    private fun initPlacementIdMap(@AnnotationAdType adType: Int) {
        when (adType) {
            ATAdConst.ATMixedFormatAdType.SPLASH -> mPlacementIdMap = PlacementIdUtil.getSplashPlacements()
            ATAdConst.ATMixedFormatAdType.NATIVE -> mPlacementIdMap =
                if (nativeAdType == AAdNativeType.NATIVE_SELF_RENDER_TYPE) PlacementIdUtil.getNativeSelfrenderPlacements() else PlacementIdUtil.getNativeExpressPlacements()

            ATAdConst.ATMixedFormatAdType.BANNER -> mPlacementIdMap = PlacementIdUtil.getBannerPlacements()
            ATAdConst.ATMixedFormatAdType.INTERSTITIAL -> mPlacementIdMap = PlacementIdUtil.getInterstitialPlacements()
            ATAdConst.ATMixedFormatAdType.REWARDED_VIDEO -> mPlacementIdMap = PlacementIdUtil.getRewardedVideoPlacements()
        }
    }

    protected fun initPlacementListAdapter(spinner: Spinner?) {
        if (spinner == null || mPlacementIdMap == null || mPlacementIdMap!!.size == 0) return
        val placementNameList: List<String> = ArrayList(mPlacementIdMap!!.keys)
        val adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, placementNameList
        )
        spinner.setAdapter(adapter)
        spinner.onItemSelectedListener = PlacementSelectListenerImpl()
    }

    private inner class PlacementSelectListenerImpl : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>, view: View,
            position: Int, id: Long
        ) {
            mCurrentPlacementName = parent.getSelectedItem().toString()
            if (mPlacementIdMap != null && mPlacementIdMap!!.size > 0) {
                mCurrentPlacementId = mPlacementIdMap!![mCurrentPlacementName!!]
            }
            if (!TextUtils.isEmpty(mCurrentPlacementId)) {
                onSelectPlacementId(mCurrentPlacementId)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    class ATAdSourceStatusListenerImpl : ATAdSourceStatusListener {
        private val TAG = javaClass.getSimpleName()
        override fun onAdSourceBiddingAttempt(adInfo: ATAdInfo) {
            Log.i(TAG, "onAdSourceBiddingAttempt: $adInfo")
        }

        override fun onAdSourceBiddingFilled(adInfo: ATAdInfo) {
            Log.i(TAG, "onAdSourceBiddingFilled: $adInfo")
        }

        override fun onAdSourceBiddingFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError?) {
            Log.i(TAG, "onAdSourceBiddingFail Info: $adInfo")
            if (adError != null) {
                Log.i(TAG, "onAdSourceBiddingFail error: " + adError.getFullErrorInfo())
            }
        }

        override fun onAdSourceAttempt(adInfo: ATAdInfo) {
            Log.i(TAG, "onAdSourceAttempt: $adInfo")
        }

        override fun onAdSourceLoadFilled(adInfo: ATAdInfo) {
            Log.i(TAG, "onAdSourceLoadFilled: $adInfo")
        }

        override fun onAdSourceLoadFail(adInfo: ATAdInfo, adError: com.anythink.core.api.AdError) {
            Log.i(TAG, "onAdSourceLoadFail Info: $adInfo")
            Log.i(TAG, "onAdSourceLoadFail error: " + adError.getFullErrorInfo())
        }
    }

    companion object {
        private var mTVShowLogWR: WeakReference<TextView?>? = null
        protected fun printLogOnUI(msg: String?) {
            if (mTVShowLogWR == null || mTVShowLogWR!!.get() == null || TextUtils.isEmpty(msg)) return
            msg?.let { mTVShowLogWR!!.get()!!.applyPrintLog(it) }
        }
    }
}
