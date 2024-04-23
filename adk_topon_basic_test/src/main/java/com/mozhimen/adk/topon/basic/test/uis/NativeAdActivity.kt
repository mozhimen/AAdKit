package com.mozhimen.adk.topon.basic.test.uis

import android.annotation.SuppressLint
import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.nativead.api.ATNative
import com.anythink.nativead.api.ATNativeAdView
import com.anythink.nativead.api.ATNativeDislikeListener
import com.anythink.nativead.api.ATNativeEventExListener
import com.anythink.nativead.api.ATNativeNetworkListener
import com.anythink.nativead.api.ATNativePrepareExInfo
import com.anythink.nativead.api.ATNativePrepareInfo
import com.anythink.nativead.api.ATNativeView
import com.anythink.nativead.api.NativeAd
import com.anythink.nativead.unitgroup.api.CustomNativeAd
import com.mozhimen.adk.topon.basic.bases.BaseATAdSourceStatusCallback
import com.mozhimen.adk.topon.basic.cons.CVideoAction
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.basic.test.annors.AAdNativeType
import com.mozhimen.adk.topon.basic.test.bases.BaseActivity
import com.mozhimen.adk.topon.basic.test.cons.AdConst
import com.mozhimen.adk.topon.basic.test.mos.CommonViewBean
import com.mozhimen.adk.topon.basic.test.utils.SelfRenderViewUtil

/**
 * @ClassName NativeAdActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

class NativeAdActivity : BaseActivity(), View.OnClickListener {
    private val mData: MutableList<String> = ArrayList()
    private var mATNative: ATNative? = null
    private var mNativeAd: NativeAd? = null
    private var mATNativeView: ATNativeView? = null
    private var mSelfRenderView: View? = null
    private var mTVLoadAdBtn: TextView? = null
    private var mTVIsAdReadyBtn: TextView? = null
    private var mTVShowAdBtn: TextView? = null
    private var mPanel: View? = null

    override val contentViewId: Int
        get() = R.layout.activity_native

    override val adType: Int
        get() = ATAdConst.ATMixedFormatAdType.NATIVE

    override fun onSelectPlacementId(placementId: String?) {
        placementId?.let { initATNativeAd(it) }
    }

    override fun getCommonViewBean(): CommonViewBean {
        val commonViewBean = CommonViewBean()
        commonViewBean.setTitleBar(findViewById(R.id.title_bar))
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log))
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1))
        val nativeType = getNativeAdTypeFromIntent()
        if (nativeType == NATIVE_SELF_RENDER_TYPE) {
            commonViewBean.setTitleResId(R.string.anythink_native_self)
        } else {
            commonViewBean.setTitleResId(R.string.anythink_native_express)
        }
        return commonViewBean
    }

    override val nativeAdType: String
        get() = getNativeAdTypeFromIntent()

    private fun getNativeAdTypeFromIntent(): String {
        return getIntent().getStringExtra("native_type") ?: AAdNativeType.NATIVE_SELF_RENDER_TYPE
    }

    override fun initView() {
        super.initView()
        mTVLoadAdBtn = findViewById(R.id.load_ad_btn)
        mTVIsAdReadyBtn = findViewById(R.id.is_ad_ready_btn)
        mTVShowAdBtn = findViewById(R.id.show_ad_btn)
        initPanel()
    }

    override fun initListener() {
        super.initListener()
        mTVLoadAdBtn!!.setOnClickListener(this)
        mTVIsAdReadyBtn!!.setOnClickListener(this)
        mTVShowAdBtn!!.setOnClickListener(this)
    }

    private fun initPanel() {
        mPanel = findViewById(R.id.rl_panel)
        mATNativeView = findViewById(R.id.native_ad_view)
        mSelfRenderView = findViewById(R.id.native_selfrender_view)
        val rvButtonList: RecyclerView = findViewById(R.id.rv_button)
        val manager = GridLayoutManager(this, 2)
        rvButtonList.layoutManager = manager
        val isMute = booleanArrayOf(true)
        val adapter = NativeVideoButtonAdapter(mData, object : NativeVideoButtonAdapter.OnNativeVideoButtonCallback {
            override fun onClick(action: String?) {
                if (action == CVideoAction.VOICE_CHANGE) {
                    if (mNativeAd != null) {
                        mNativeAd!!.setVideoMute(!isMute[0])
                        isMute[0] = !isMute[0]
                    }
                } else if (action == CVideoAction.VIDEO_RESUME) {
                    if (mNativeAd != null) {
                        mNativeAd!!.resumeVideo()
                    }
                } else if (action == CVideoAction.VIDEO_PAUSE) {
                    if (mNativeAd != null) {
                        mNativeAd!!.pauseVideo()
                    }
                } else if (action == CVideoAction.VIDEO_PROGRESS) {
                    if (mNativeAd != null) {
                        val tips = "video duration: " + mNativeAd!!.videoDuration + ", progress: " + mNativeAd!!.videoProgress
                        UtilKLogWrapper.i(TAG, tips)
                        Toast.makeText(this@NativeAdActivity, tips, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        rvButtonList.adapter = adapter
    }

    private fun initATNativeAd(placementId: String) {
        mATNative = ATNative(this, placementId, object : ATNativeNetworkListener {
            override fun onNativeAdLoaded() {
                UtilKLogWrapper.i(TAG, "onNativeAdLoaded")
                printLogOnUI("load success...")
            }

            override fun onNativeAdLoadFail(adError: AdError) {
                UtilKLogWrapper.i(TAG, "onNativeAdLoadFail, " + adError.fullErrorInfo)
                printLogOnUI("load fail...：" + adError.fullErrorInfo)
            }
        })
        mATNative!!.setAdSourceStatusListener(BaseATAdSourceStatusCallback())
    }

    private fun loadAd(adViewWidth: Int, adViewHeight: Int) {
        printLogOnUI(getString(R.string.anythink_ad_status_loading))
        val localExtra: MutableMap<String, Any> = HashMap()
        localExtra[ATAdConst.KEY.AD_WIDTH] = adViewWidth
        localExtra[ATAdConst.KEY.AD_HEIGHT] = adViewHeight
        mATNative!!.setLocalExtra(localExtra)
        mATNative!!.makeAdRequest()
    }

    private fun isAdReady(): Boolean {
        val isReady = mATNative!!.checkAdStatus().isReady
        UtilKLogWrapper.i(TAG, "isAdReady: $isReady")
        printLogOnUI("isAdReady：$isReady")
        val atAdInfoList = mATNative!!.checkValidAdCaches()
        UtilKLogWrapper.i(TAG, "Valid Cahce size:" + (atAdInfoList?.size ?: 0))
        if (atAdInfoList != null) {
            for (adInfo in atAdInfoList) {
                UtilKLogWrapper.i(TAG, "\nCahce detail:$adInfo")
            }
        }
        return isReady
    }

    private fun showAd() {
//        NativeAd nativeAd = mATNative.getNativeAd();
        val nativeAd = mATNative!!.getNativeAd(AdConst.SCENARIO_ID.NATIVE_AD_SCENARIO)
        if (nativeAd != null) {
            if (mNativeAd != null) {
                mNativeAd!!.destory()
            }
            mNativeAd = nativeAd
            mNativeAd!!.setNativeEventListener(object : ATNativeEventExListener {
                override fun onDeeplinkCallback(view: ATNativeAdView, adInfo: ATAdInfo, isSuccess: Boolean) {
                    UtilKLogWrapper.i(TAG, "onDeeplinkCallback:$adInfo--status:$isSuccess")
                    printLogOnUI("onDeeplinkCallback")
                }

                override fun onAdImpressed(view: ATNativeAdView, entity: ATAdInfo) {
                    UtilKLogWrapper.i(TAG, "native ad onAdImpressed:\n$entity")
                    printLogOnUI("onAdImpressed")
                }

                override fun onAdClicked(view: ATNativeAdView, entity: ATAdInfo) {
                    UtilKLogWrapper.i(TAG, "native ad onAdClicked:\n$entity")
                    printLogOnUI("onAdClicked")
                }

                override fun onAdVideoStart(view: ATNativeAdView) {
                    UtilKLogWrapper.i(TAG, "native ad onAdVideoStart")
                    printLogOnUI("onAdVideoStart")
                }

                override fun onAdVideoEnd(view: ATNativeAdView) {
                    UtilKLogWrapper.i(TAG, "native ad onAdVideoEnd")
                    printLogOnUI("onAdVideoEnd")
                }

                override fun onAdVideoProgress(view: ATNativeAdView, progress: Int) {
                    UtilKLogWrapper.i(TAG, "native ad onAdVideoProgress:$progress")
                    printLogOnUI("onAdVideoProgress")
                }
            })
            mNativeAd!!.setDislikeCallbackListener(object : ATNativeDislikeListener() {
                override fun onAdCloseButtonClick(view: ATNativeAdView, entity: ATAdInfo) {
                    UtilKLogWrapper.i(TAG, "native ad onAdCloseButtonClick")
                    printLogOnUI("native ad onAdCloseButtonClick")
                    exitNativePanel()
                }
            })
            mATNativeView!!.removeAllViews()
            var mNativePrepareInfo: ATNativePrepareInfo? = null
            try {
                mNativePrepareInfo = ATNativePrepareExInfo()
                if (mNativeAd!!.isNativeExpress) {
                    mNativeAd!!.renderAdContainer(mATNativeView, null)
                } else {
                    SelfRenderViewUtil.bindSelfRenderView(this, mNativeAd!!.adMaterial, mSelfRenderView!!, mNativePrepareInfo)
                    mNativeAd!!.renderAdContainer(mATNativeView, mSelfRenderView)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mNativeAd!!.prepare(mATNativeView, mNativePrepareInfo)
            mATNativeView!!.visibility = View.VISIBLE
            mPanel!!.visibility = View.VISIBLE
            initPanelButtonList(mNativeAd!!.adMaterial.adType)
        } else {
            printLogOnUI("this placement no cache!")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyAd()
        if (mATNative != null) {
            mATNative!!.setAdListener(null)
            mATNative!!.setAdSourceStatusListener(null)
        }
    }

    private fun destroyAd() {
        if (mNativeAd != null) {
            mNativeAd!!.destory()
        }
    }

    override fun onPause() {
        if (mNativeAd != null) {
            mNativeAd!!.onPause()
        }
        super.onPause()
    }

    override fun onResume() {
        if (mNativeAd != null) {
            mNativeAd!!.onResume()
        }
        super.onResume()
    }

    private fun initPanelButtonList(adType: String) {
        if (adType == CustomNativeAd.NativeAdConst.VIDEO_TYPE) {
            var isNativeExpress = true
            if (mNativeAd != null) {
                isNativeExpress = mNativeAd!!.isNativeExpress
            }
            if (isNativeExpress) {
                return
            }
            val atAdInfo = mNativeAd!!.adInfo
            val networkId = atAdInfo.networkFirmId
            when (networkId) {
                8 -> {
                    mData.add(CVideoAction.VOICE_CHANGE)
                    mData.add(CVideoAction.VIDEO_RESUME)
                    mData.add(CVideoAction.VIDEO_PAUSE)
                    mData.add(CVideoAction.VIDEO_PROGRESS)
                }

                22, 28 -> mData.add(CVideoAction.VIDEO_PROGRESS)
                66, 67 -> {
                    mData.add(CVideoAction.VOICE_CHANGE)
                    mData.add(CVideoAction.VIDEO_RESUME)
                    mData.add(CVideoAction.VIDEO_PAUSE)
                    mData.add(CVideoAction.VIDEO_PROGRESS)
                }
            }
        }
    }

    private fun exitNativePanel() {
        mData.clear()
        destroyAd()
        mPanel!!.visibility = View.GONE
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mPanel!!.visibility == View.VISIBLE) {
            exitNativePanel()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.load_ad_btn -> {
                val adViewWidth = if (mATNativeView!!.width != 0) mATNativeView!!.width else getResources().getDisplayMetrics().widthPixels
                val adViewHeight = adViewWidth * 3 / 4
                loadAd(adViewWidth, adViewHeight)
            }

            R.id.is_ad_ready_btn -> isAdReady()
            R.id.show_ad_btn -> {
                ATNative.entryAdScenario(mCurrentPlacementId, AdConst.SCENARIO_ID.NATIVE_AD_SCENARIO)
                if (isAdReady()) {
                    showAd()
                }
            }
        }
    }

    companion object {
        const val NATIVE_SELF_RENDER_TYPE = "1"
        const val NATIVE_EXPRESS_TYPE = "2"
    }
}

