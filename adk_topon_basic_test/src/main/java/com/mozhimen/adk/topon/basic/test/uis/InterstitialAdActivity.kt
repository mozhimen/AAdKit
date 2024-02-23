package com.mozhimen.adk.topon.basic.test.uis

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitial
import com.anythink.interstitial.api.ATInterstitialAutoAd
import com.anythink.interstitial.api.ATInterstitialAutoEventListener
import com.anythink.interstitial.api.ATInterstitialAutoLoadListener
import com.anythink.interstitial.api.ATInterstitialExListener
import com.mozhimen.adk.topon.basic.bases.BaseATAdSourceStatusCallback
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.basic.test.bases.BaseActivity
import com.mozhimen.adk.topon.basic.test.cons.AdConst
import com.mozhimen.adk.topon.basic.test.mos.CommonViewBean
import com.mozhimen.basick.utilk.bases.IUtilK

/**
 * @ClassName InterstitialAdActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/23
 * @Version 1.0
 */

class InterstitialAdActivity : BaseActivity(), View.OnClickListener,IUtilK {
    private var mInterstitialAd: ATInterstitial? = null
    private var mIsAutoLoad = false
    private val mAutoLoadPlacementIdMap: MutableMap<String, Boolean> = HashMap()
    private var mCBAutoLoad: CheckBox? = null
    private var mTVLoadAdBtn: TextView? = null
    private var mTVIsAdReadyBtn: TextView? = null
    private var mTVShowAdBtn: TextView? = null

    override val contentViewId: Int
        get() = R.layout.activity_interstitial
    override val adType: Int
        get() = ATAdConst.ATMixedFormatAdType.INTERSTITIAL

    override fun onSelectPlacementId(placementId: String?) {
        val isAutoLoad = java.lang.Boolean.TRUE == mAutoLoadPlacementIdMap[placementId]
        if (mCBAutoLoad != null) {
            mCBAutoLoad!!.setChecked(isAutoLoad)
        }
        if (placementId != null) {
            initInterstitialAd(placementId)
        }
    }

    override fun getCommonViewBean(): CommonViewBean {
        val commonViewBean = CommonViewBean()
        commonViewBean.setTitleBar(findViewById(R.id.title_bar))
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1))
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log))
        commonViewBean.setTitleResId(R.string.anythink_title_interstitial)
        return commonViewBean
    }

    override fun initView() {
        super.initView()
        mTVLoadAdBtn = findViewById(R.id.load_ad_btn)
        mTVIsAdReadyBtn = findViewById(R.id.is_ad_ready_btn)
        mTVShowAdBtn = findViewById(R.id.show_ad_btn)
        initAutoLoad()
    }

    override fun initListener() {
        super.initListener()
        mTVLoadAdBtn!!.setOnClickListener(this)
        mTVIsAdReadyBtn!!.setOnClickListener(this)
        mTVShowAdBtn!!.setOnClickListener(this)
    }

    private fun initInterstitialAd(placementId: String) {
        mInterstitialAd = ATInterstitial(this, placementId)
        mInterstitialAd!!.setAdListener(object : ATInterstitialExListener {
            override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
                Log.i(TAG, "onDeeplinkCallback:$adInfo--status:$isSuccess")
                printLogOnUI("onDeeplinkCallback")
            }

            override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
                Log.i(TAG, "onDownloadConfirm: adInfo=$adInfo")
                printLogOnUI("onDownloadConfirm")
            }

            override fun onInterstitialAdLoaded() {
                Log.i(TAG, "onInterstitialAdLoaded")
                printLogOnUI("onInterstitialAdLoaded")
            }

            override fun onInterstitialAdLoadFail(adError: AdError) {
                Log.i(
                    TAG, """
     onInterstitialAdLoadFail:
     ${adError.fullErrorInfo}
     """.trimIndent()
                )
                printLogOnUI("onInterstitialAdLoadFail:" + adError.fullErrorInfo)
            }

            override fun onInterstitialAdClicked(entity: ATAdInfo) {
                Log.i(TAG, "onInterstitialAdClicked:\n$entity")
                printLogOnUI("onInterstitialAdClicked")
            }

            override fun onInterstitialAdShow(entity: ATAdInfo) {
                Log.i(TAG, "onInterstitialAdShow:\n$entity")
                printLogOnUI("onInterstitialAdShow")
            }

            override fun onInterstitialAdClose(entity: ATAdInfo) {
                Log.i(TAG, "onInterstitialAdClose:\n$entity")
                printLogOnUI("onInterstitialAdClose")
            }

            override fun onInterstitialAdVideoStart(entity: ATAdInfo) {
                Log.i(TAG, "onInterstitialAdVideoStart:\n$entity")
                printLogOnUI("onInterstitialAdVideoStart")
            }

            override fun onInterstitialAdVideoEnd(entity: ATAdInfo) {
                Log.i(TAG, "onInterstitialAdVideoEnd:\n$entity")
                printLogOnUI("onInterstitialAdVideoEnd")
            }

            override fun onInterstitialAdVideoError(adError: AdError) {
                Log.i(
                    TAG, """
     onInterstitialAdVideoError:
     ${adError.fullErrorInfo}
     """.trimIndent()
                )
                printLogOnUI("onInterstitialAdVideoError")
            }
        })
        mInterstitialAd!!.setAdSourceStatusListener(BaseATAdSourceStatusCallback())
    }

    private fun initAutoLoad() {
        ATInterstitialAutoAd.init(this, null, autoLoadListener)
        mCBAutoLoad = findViewById(R.id.ck_auto_load)
        mCBAutoLoad!!.visibility = View.VISIBLE
        mCBAutoLoad!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mIsAutoLoad = true
                val curPlacementId: String = mCurrentPlacementId!!
                mAutoLoadPlacementIdMap[curPlacementId] = true
                ATInterstitialAutoAd.addPlacementId(curPlacementId)
                mTVLoadAdBtn!!.visibility = View.GONE
            } else {
                mIsAutoLoad = false
                val curPlacementId: String = mCurrentPlacementId!!
                mAutoLoadPlacementIdMap[curPlacementId] = false
                ATInterstitialAutoAd.removePlacementId(curPlacementId)
                mTVLoadAdBtn!!.visibility = View.VISIBLE
            }
        }
    }

    private fun loadAd() {
        if (mInterstitialAd == null) {
            printLogOnUI("ATInterstitial is not init.")
            return
        }
        printLogOnUI(getString(R.string.anythink_ad_status_loading))
        val localMap: Map<String, Any> = HashMap()

//        localMap.put(ATAdConst.KEY.AD_WIDTH, getResources().getDisplayMetrics().widthPixels);
//        localMap.put(ATAdConst.KEY.AD_HEIGHT, getResources().getDisplayMetrics().heightPixels);
        mInterstitialAd!!.setLocalExtra(localMap)
        mInterstitialAd!!.load()
    }

    private fun isAdReady() {
        if (mIsAutoLoad) {
            printLogOnUI("interstitial auto load ad ready status:" + ATInterstitialAutoAd.isAdReady(mCurrentPlacementId))
        } else {
//         boolean isReady = mInterstitialAd.isAdReady();
            val atAdStatusInfo = mInterstitialAd!!.checkAdStatus()
            printLogOnUI("interstitial ad ready status:" + atAdStatusInfo.isReady)
            val atAdInfoList = mInterstitialAd!!.checkValidAdCaches()
            Log.i(TAG, "Valid Cahce size:" + (atAdInfoList?.size ?: 0))
            if (atAdInfoList != null) {
                for (adInfo in atAdInfoList) {
                    Log.i(TAG, "\nCahce detail:$adInfo")
                }
            }
        }
    }

    private fun showAd() {
        if (mIsAutoLoad) {
//            ATInterstitialAutoAd.show(this, mCurrentPlacementId, autoEventListener);
            ATInterstitialAutoAd.show(this, mCurrentPlacementId, AdConst.SCENARIO_ID.INTERSTITIAL_AD_SCENARIO, autoEventListener)
        } else {
//            mInterstitialAd.show(InterstitialAdActivity.this);
            mInterstitialAd!!.show(this@InterstitialAdActivity, AdConst.SCENARIO_ID.INTERSTITIAL_AD_SCENARIO)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        for ((key) in mAutoLoadPlacementIdMap) {
            ATInterstitialAutoAd.removePlacementId(key)
        }
        if (mInterstitialAd != null) {
            mInterstitialAd!!.setAdSourceStatusListener(null)
            mInterstitialAd!!.setAdDownloadListener(null)
            mInterstitialAd!!.setAdListener(null)
        }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.load_ad_btn -> loadAd()
            R.id.is_ad_ready_btn -> isAdReady()
            R.id.show_ad_btn -> {
                ATInterstitial.entryAdScenario(mCurrentPlacementId, AdConst.SCENARIO_ID.INTERSTITIAL_AD_SCENARIO)
                if (mInterstitialAd!!.isAdReady) {
                    showAd()
                }
            }
        }
    }


    private val autoLoadListener: ATInterstitialAutoLoadListener = object : ATInterstitialAutoLoadListener {
        override fun onInterstitialAutoLoaded(placementId: String) {
            Log.i(TAG, "PlacementId:$placementId: onInterstitialAutoLoaded")
            printLogOnUI("PlacementId:$placementId: onInterstitialAutoLoaded")
        }

        override fun onInterstitialAutoLoadFail(placementId: String, adError: AdError) {
            Log.i(
                TAG, """
     PlacementId:$placementId: onInterstitialAutoLoadFail:
     ${adError.fullErrorInfo}
     """.trimIndent()
            )
            printLogOnUI(
                """
                        PlacementId:$placementId: onInterstitialAutoLoadFail:
                        ${adError.fullErrorInfo}
                        """.trimIndent()
            )
        }
    }
    private val autoEventListener: ATInterstitialAutoEventListener = object : ATInterstitialAutoEventListener() {
        override fun onInterstitialAdClicked(adInfo: ATAdInfo) {
            Log.i(TAG, "onInterstitialAdClicked:$adInfo")
            printLogOnUI("onInterstitialAdClicked:")
        }

        override fun onInterstitialAdShow(adInfo: ATAdInfo) {
            Log.i(TAG, "onInterstitialAdShow:$adInfo")
            printLogOnUI("onInterstitialAdShow")
        }

        override fun onInterstitialAdClose(adInfo: ATAdInfo) {
            Log.i(TAG, "onInterstitialAdClose:$adInfo")
            printLogOnUI("onInterstitialAdClose")
        }

        override fun onInterstitialAdVideoStart(adInfo: ATAdInfo) {
            Log.i(TAG, "onInterstitialAdVideoStart:$adInfo")
            printLogOnUI("onInterstitialAdVideoStart")
        }

        override fun onInterstitialAdVideoEnd(adInfo: ATAdInfo) {
            Log.i(TAG, "onInterstitialAdVideoEnd:$adInfo")
            printLogOnUI("onInterstitialAdVideoEnd")
        }

        override fun onInterstitialAdVideoError(adError: AdError) {
            Log.i(TAG, "onInterstitialAdVideoError:" + adError.fullErrorInfo)
            printLogOnUI("onInterstitialAdVideoError:" + adError.fullErrorInfo)
        }

        override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
            Log.i(TAG, "onDeeplinkCallback:\n$adInfo| isSuccess:$isSuccess")
            printLogOnUI("onDeeplinkCallback: isSuccess=$isSuccess")
        }

        override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
            Log.i(TAG, "onDownloadConfirm:\n$adInfo")
            printLogOnUI("onDownloadConfirm")
        }
    }
}


