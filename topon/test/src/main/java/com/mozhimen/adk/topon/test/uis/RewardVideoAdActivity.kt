package com.mozhimen.adk.topon.test.uis

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.rewardvideo.api.ATRewardVideoAd
import com.anythink.rewardvideo.api.ATRewardVideoAutoAd
import com.anythink.rewardvideo.api.ATRewardVideoAutoEventListener
import com.anythink.rewardvideo.api.ATRewardVideoAutoLoadListener
import com.anythink.rewardvideo.api.ATRewardVideoExListener
import com.mozhimen.adk.topon.basic.bases.BaseATAdSourceStatusCallback
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.test.bases.BaseActivity
import com.mozhimen.adk.topon.test.cons.AdConst
import com.mozhimen.adk.topon.test.mos.CommonViewBean
import com.mozhimen.kotlin.utilk.commons.IUtilK

/**
 * @ClassName RewardVideoAdActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/23
 * @Version 1.0
 */

class RewardVideoAdActivity : com.mozhimen.adk.topon.test.bases.BaseActivity(), View.OnClickListener, IUtilK {
    private var mRewardVideoAd: ATRewardVideoAd? = null
    private val mAutoLoadPlacementIdMap: MutableMap<String, Boolean> = HashMap()
    private var mIsAutoLoad = false
    private var mTvLoadAdBtn: TextView? = null
    private var mTvIsAdReadyBtn: TextView? = null
    private var mTvShowAdBtn: TextView? = null
    private var mCbAutoLoad: CheckBox? = null

    override val contentViewId: Int
        get() =
            R.layout.activity_video

    override val adType: Int
        get() = ATAdConst.ATMixedFormatAdType.REWARDED_VIDEO

    override fun onSelectPlacementId(placementId: String?) {
        val isAutoLoad = java.lang.Boolean.TRUE == mAutoLoadPlacementIdMap[placementId]
        if (mCbAutoLoad != null) {
            mCbAutoLoad!!.setChecked(isAutoLoad)
        }
        placementId?.let { initRewardVideoAd(it) }
    }

    override fun getCommonViewBean(): com.mozhimen.adk.topon.test.mos.CommonViewBean {
        val commonViewBean = com.mozhimen.adk.topon.test.mos.CommonViewBean()
        commonViewBean.setTitleBar(findViewById(R.id.title_bar))
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log))
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1))
        commonViewBean.setTitleResId(R.string.anythink_title_rewarded_video)
        return commonViewBean
    }

    override fun initView() {
        super.initView()
        mTvLoadAdBtn = findViewById(R.id.load_ad_btn)
        mTvIsAdReadyBtn = findViewById(R.id.is_ad_ready_btn)
        mTvShowAdBtn = findViewById(R.id.show_ad_btn)
        initAutoLoad()
    }

    override fun initListener() {
        super.initListener()
        mTvLoadAdBtn!!.setOnClickListener(this)
        mTvIsAdReadyBtn!!.setOnClickListener(this)
        mTvShowAdBtn!!.setOnClickListener(this)
    }

    private fun initRewardVideoAd(placementId: String) {
        mRewardVideoAd = ATRewardVideoAd(this, placementId)
        mRewardVideoAd!!.setAdListener(object : ATRewardVideoExListener {
            override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
                UtilKLogWrapper.i(TAG, "onDeeplinkCallback:$adInfo--status:$isSuccess")
                printLogOnUI("onDeeplinkCallback")
            }

            override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
                UtilKLogWrapper.i(TAG, "onDownloadConfirm: $adInfo")
                printLogOnUI("onDownloadConfirm")
            }

            //-------------------------- Only for CSJ --------------------------
            override fun onRewardedVideoAdAgainPlayStart(entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdAgainPlayStart:\n$entity")
                printLogOnUI("onRewardedVideoAdAgainPlayStart")
            }

            override fun onRewardedVideoAdAgainPlayEnd(entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdAgainPlayEnd:\n$entity")
                printLogOnUI("onRewardedVideoAdAgainPlayEnd")
            }

            override fun onRewardedVideoAdAgainPlayFailed(errorCode: AdError, entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdAgainPlayFailed error: " + errorCode.fullErrorInfo)
                printLogOnUI("onRewardedVideoAdAgainPlayFailed:" + errorCode.fullErrorInfo)
            }

            override fun onRewardedVideoAdAgainPlayClicked(entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdAgainPlayClicked: $entity")
                printLogOnUI("onRewardedVideoAdAgainPlayClicked")
            }

            override fun onAgainReward(entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onAgainReward:\n$entity")
                printLogOnUI("onAgainReward")
            }

            //-------------------------- Only for CSJ --------------------------
            override fun onRewardedVideoAdLoaded() {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdLoaded")
                printLogOnUI("onRewardedVideoAdLoaded")
            }

            override fun onRewardedVideoAdFailed(errorCode: AdError) {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdFailed error:" + errorCode.fullErrorInfo)
                printLogOnUI("onRewardedVideoAdFailed:" + errorCode.fullErrorInfo)
            }

            override fun onRewardedVideoAdPlayStart(entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdPlayStart:\n$entity")
                printLogOnUI("onRewardedVideoAdPlayStart")
            }

            override fun onRewardedVideoAdPlayEnd(entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdPlayEnd:\n$entity")
                printLogOnUI("onRewardedVideoAdPlayEnd")
            }

            override fun onRewardedVideoAdPlayFailed(errorCode: AdError, entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdPlayFailed:\n$entity")
                printLogOnUI("onRewardedVideoAdPlayFailed:" + errorCode.fullErrorInfo)
            }

            override fun onRewardedVideoAdClosed(entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdClosed:\n$entity")
                printLogOnUI("onRewardedVideoAdClosed")
            }

            override fun onRewardedVideoAdPlayClicked(entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onRewardedVideoAdPlayClicked:\n$entity")
                printLogOnUI("onRewardedVideoAdPlayClicked")
            }

            override fun onReward(entity: ATAdInfo) {
                UtilKLogWrapper.e(TAG, "onReward:\n$entity")
                printLogOnUI("onReward")
            }
        })
        mRewardVideoAd!!.setAdSourceStatusListener(BaseATAdSourceStatusCallback())
    }

    private fun initAutoLoad() {
        ATRewardVideoAutoAd.init(this, null, autoLoadListener)
        mCbAutoLoad = findViewById(R.id.ck_auto_load)
        mCbAutoLoad!!.setVisibility(View.VISIBLE)
        mCbAutoLoad!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val placementId = mCurrentPlacementId!!
            if (isChecked) {
                mIsAutoLoad = true
                mAutoLoadPlacementIdMap[placementId] = true
                ATRewardVideoAutoAd.addPlacementId(placementId)
                mTvLoadAdBtn!!.visibility = View.GONE
            } else {
                mIsAutoLoad = false
                mAutoLoadPlacementIdMap[placementId] = false
                ATRewardVideoAutoAd.removePlacementId(placementId)
                mTvLoadAdBtn!!.visibility = View.VISIBLE
            }
        })
    }

    private fun loadAd() {
        if (mRewardVideoAd == null) {
            printLogOnUI("ATRewardVideoAd is not init.")
            return
        }
        printLogOnUI(getString(R.string.anythink_ad_status_loading))
        val userid = "test_userid_001"
        val userdata = "test_userdata_001"
        val localMap: MutableMap<String, Any> = HashMap()
        localMap[ATAdConst.KEY.USER_ID] = userid
        localMap[ATAdConst.KEY.USER_CUSTOM_DATA] = userdata
        mRewardVideoAd!!.setLocalExtra(localMap)
        mRewardVideoAd!!.load()
    }

    private fun isAdReady() {
        if (mRewardVideoAd == null) {
            return
        }
        if (mIsAutoLoad) {
            printLogOnUI("video auto load ad ready status:" + ATRewardVideoAutoAd.isAdReady(mCurrentPlacementId))
        } else {
//        boolean isReady = mRewardVideoAd.isAdReady();
            val atAdStatusInfo = mRewardVideoAd!!.checkAdStatus()
            printLogOnUI("video ad ready status:" + atAdStatusInfo.isReady)
            val atAdInfoList = mRewardVideoAd!!.checkValidAdCaches()
            UtilKLogWrapper.i(TAG, "Valid Cahce size:" + (atAdInfoList?.size ?: 0))
            if (atAdInfoList != null) {
                for (adInfo in atAdInfoList) {
                    UtilKLogWrapper.i(TAG, "\nCahce detail:$adInfo")
                }
            }
        }
    }

    private fun showAd() {
        if (mIsAutoLoad) {
//            ATRewardVideoAutoAd.show(this, mCurrentPlacementId, autoEventListener);
            ATRewardVideoAutoAd.show(this, mCurrentPlacementId, com.mozhimen.adk.topon.test.cons.AdConst.SCENARIO_ID.REWARD_VIDEO_AD_SCENARIO, autoEventListener)
        } else {
//            mRewardVideoAd.show(RewardVideoAdActivity.this);
            mRewardVideoAd!!.show(this@RewardVideoAdActivity, com.mozhimen.adk.topon.test.cons.AdConst.SCENARIO_ID.REWARD_VIDEO_AD_SCENARIO)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        for ((key) in mAutoLoadPlacementIdMap) {
            ATRewardVideoAutoAd.removePlacementId(key)
        }
        if (mRewardVideoAd != null) {
            mRewardVideoAd!!.setAdSourceStatusListener(null)
            mRewardVideoAd!!.setAdDownloadListener(null)
            mRewardVideoAd!!.setAdListener(null)
        }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.load_ad_btn -> loadAd()
            R.id.is_ad_ready_btn -> isAdReady()
            R.id.show_ad_btn -> {
                ATRewardVideoAd.entryAdScenario(mCurrentPlacementId, com.mozhimen.adk.topon.test.cons.AdConst.SCENARIO_ID.REWARD_VIDEO_AD_SCENARIO)
                if (mRewardVideoAd != null && mRewardVideoAd!!.isAdReady) {
                    showAd()
                } else {
                    printLogOnUI(getString(R.string.anythink_ad_status_not_load))
                }
            }
        }
    }

    val autoLoadListener: ATRewardVideoAutoLoadListener = object : ATRewardVideoAutoLoadListener {
        override fun onRewardVideoAutoLoaded(placementId: String) {
            initPlacementIdLocalExtra(placementId)
            UtilKLogWrapper.i(TAG, "PlacementId:$placementId: onRewardVideoAutoLoaded")
            printLogOnUI("PlacementId:$placementId: onRewardVideoAutoLoaded")
        }

        override fun onRewardVideoAutoLoadFail(placementId: String, adError: AdError) {
            UtilKLogWrapper.i(
                TAG, """
     PlacementId:$placementId: onRewardVideoAutoLoadFail:
     ${adError.fullErrorInfo}
     """.trimIndent()
            )
            printLogOnUI(
                """
                        PlacementId:$placementId: onRewardVideoAutoLoadFail:
                        ${adError.fullErrorInfo}
                        """.trimIndent()
            )
        }
    }

    fun initPlacementIdLocalExtra(placementId: String) {
        val userid = "test_userid_001"
        val userdata = "test_userdata_001_" + placementId + "_" + System.currentTimeMillis()
        val localMap: MutableMap<String, Any> = HashMap()
        localMap[ATAdConst.KEY.USER_ID] = userid
        localMap[ATAdConst.KEY.USER_CUSTOM_DATA] = userdata
        UtilKLogWrapper.i(TAG, "Set PlacementId:$placementId: UserId:$userid| userdata:$userdata")
        ATRewardVideoAutoAd.setLocalExtra(placementId, localMap)
    }

    private val autoEventListener: ATRewardVideoAutoEventListener = object : ATRewardVideoAutoEventListener() {
        override fun onRewardedVideoAdPlayStart(adInfo: ATAdInfo) {
            UtilKLogWrapper.i(TAG, "onRewardedVideoAdPlayStart:\n$adInfo")
            printLogOnUI("onRewardedVideoAdPlayStart:")
        }

        override fun onRewardedVideoAdPlayEnd(adInfo: ATAdInfo) {
            UtilKLogWrapper.i(TAG, "onRewardedVideoAdPlayEnd:\n$adInfo")
            printLogOnUI("onRewardedVideoAdPlayEnd")
        }

        override fun onRewardedVideoAdPlayFailed(errorCode: AdError, adInfo: ATAdInfo) {
            UtilKLogWrapper.i(TAG, "onRewardedVideoAdPlayFailed:\n$adInfo")
            printLogOnUI("onRewardedVideoAdPlayFailed")
        }

        override fun onRewardedVideoAdClosed(adInfo: ATAdInfo) {
            UtilKLogWrapper.i(TAG, "onRewardedVideoAdClosed:\n$adInfo")
            printLogOnUI("onRewardedVideoAdClosed")
        }

        override fun onRewardedVideoAdPlayClicked(adInfo: ATAdInfo) {
            UtilKLogWrapper.i(TAG, "onRewardedVideoAdPlayClicked:\n$adInfo")
            printLogOnUI("onRewardedVideoAdPlayClicked")
        }

        override fun onReward(adInfo: ATAdInfo) {
            UtilKLogWrapper.e(TAG, "onReward:\n$adInfo")
            printLogOnUI("onReward")
        }

        override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
            UtilKLogWrapper.i(TAG, "onDeeplinkCallback:\n$adInfo| isSuccess:$isSuccess")
            printLogOnUI("onDeeplinkCallback")
        }

        override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
            UtilKLogWrapper.i(TAG, "onDownloadConfirm:\n$adInfo")
            printLogOnUI("onDownloadConfirm")
        }

        //again listener
        override fun onRewardedVideoAdAgainPlayStart(adInfo: ATAdInfo) {
            UtilKLogWrapper.i(TAG, "onRewardedVideoAdAgainPlayStart:\n$adInfo")
            printLogOnUI("onRewardedVideoAdAgainPlayStart")
        }

        override fun onRewardedVideoAdAgainPlayEnd(adInfo: ATAdInfo) {
            UtilKLogWrapper.i(TAG, "onRewardedVideoAdAgainPlayEnd:\n$adInfo")
            printLogOnUI("onRewardedVideoAdAgainPlayEnd")
        }

        override fun onRewardedVideoAdAgainPlayFailed(adError: AdError, adInfo: ATAdInfo) {
            UtilKLogWrapper.i(
                TAG, """
     onRewardedVideoAdAgainPlayFailed:
     $adInfo｜error：${adError.fullErrorInfo}
     """.trimIndent()
            )
            printLogOnUI("onRewardedVideoAdAgainPlayFailed")
        }

        override fun onRewardedVideoAdAgainPlayClicked(adInfo: ATAdInfo) {
            UtilKLogWrapper.i(TAG, "onRewardedVideoAdAgainPlayClicked:\n$adInfo")
            printLogOnUI("onRewardedVideoAdAgainPlayClicked")
        }

        override fun onAgainReward(adInfo: ATAdInfo) {
            UtilKLogWrapper.i(TAG, "onAgainReward:\n$adInfo")
            printLogOnUI("onAgainReward")
        }
    }
}


