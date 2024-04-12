package com.mozhimen.adk.topon.basic.test.bases

import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import com.anythink.core.api.ATAdConst
import com.mozhimen.adk.topon.basic.test.annors.AAdNativeType
import com.mozhimen.adk.topon.basic.test.annors.AnnotationAdType
import com.mozhimen.adk.topon.basic.test.mos.CommonViewBean
import com.mozhimen.adk.topon.basic.test.utils.PlacementIdUtil
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.widget.UtilKTextViewWrapper
import com.mozhimen.xmlk.bark.title.BarKTitle
import java.lang.ref.WeakReference

/**
 * @ClassName BaseActivityVDB
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/23
 * @Version 1.0
 */
abstract class BaseActivityVDB<VB : ViewDataBinding> : BaseActivityVDB<VB>() {
    companion object {
        private var mTVShowLogWR: WeakReference<TextView?>? = null

        @JvmStatic
        protected fun printLogOnUI(msg: String?) {
            if (mTVShowLogWR == null || mTVShowLogWR!!.get() == null || TextUtils.isEmpty(msg)) return
            msg?.let { UtilKTextViewWrapper.applyValue_ofLog(mTVShowLogWR!!.get()!!, it) }
        }
    }

    /////////////////////////////////////////////////////////////////

    protected var mCurrentPlacementName: String? = null
    protected var mCurrentPlacementId: String? = null
    protected var mTVShowLog: TextView? = null

    /////////////////////////////////////////////////////////////////

    protected abstract val contentViewId: Int

    @get:AnnotationAdType
    protected abstract val adType: Int

    protected abstract fun onSelectPlacementId(placementId: String?)

    /////////////////////////////////////////////////////////////////

    @CallSuper
    override fun initView(savedInstanceState: Bundle?) {
        initViewWithCommonView(getCommonViewBean())
    }

    @CallSuper
    override fun initObserver() {
        initPlacementIdMap(adType)
        if (mCommonViewBean != null) {
            initPlacementListAdapter(mCommonViewBean!!.getSpinnerSelectPlacement())
        }
    }

    /////////////////////////////////////////////////////////////////

    protected open val nativeAdType: String
        protected get() = AAdNativeType.NATIVE_SELF_RENDER_TYPE

    protected open fun getCommonViewBean(): CommonViewBean? {
        return null
    }

    /////////////////////////////////////////////////////////////////

    private var mCommonViewBean: CommonViewBean? = null
    private var mPlacementIdMap: Map<String, String>? = null

    private fun initPlacementListAdapter(spinner: Spinner?) {
        if (spinner == null || mPlacementIdMap == null || mPlacementIdMap!!.size == 0) return
        Log.d(TAG, "initPlacementListAdapter: mPlacementIdMap ${mPlacementIdMap!!.size}")
        val placementNameList: List<String> = ArrayList(mPlacementIdMap!!.keys)
        val adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, placementNameList
        )
        spinner.setAdapter(adapter)
        spinner.onItemSelectedListener = PlacementSelectListenerImpl()
    }

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

    private fun setTitleBar(titleBar: BarKTitle?, titleResId: Int) {
        if (titleBar != null && titleResId != 0) {
            titleBar.setTitle(titleResId)
            titleBar.setListener { v -> finish() }
        }
    }

    private fun initViewWithCommonView(commonViewBean: CommonViewBean?) {
        mCommonViewBean = commonViewBean
        if (commonViewBean != null) {
            Log.d(TAG, "initViewWithCommonView: ")
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
}