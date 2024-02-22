package com.mozhimen.adk.topon.basic.test.uis

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdExtraInfo
import com.anythink.splashad.api.ATSplashExListener
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.basic.test.bases.BaseActivity
import com.mozhimen.adk.topon.basic.test.cons.AdConst
import com.mozhimen.adk.topon.basic.test.mos.CommonViewBean
import com.mozhimen.basick.utilk.bases.IUtilK

/**
 * @ClassName SplashAdActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */
class SplashAdActivity : BaseActivity(), View.OnClickListener, IUtilK {

    private var mSplashAd: ATSplashAd? = null

    override val contentViewId: Int
        get() = R.layout.activity_splash

    override val adType: Int
        get() = ATAdConst.ATMixedFormatAdType.SPLASH

    override fun initView() {
        super.initView()
        findViewById<RelativeLayout>(R.id.rl_type).setSelected(true)
    }

    override fun getCommonViewBean(): CommonViewBean {
        val commonViewBean = CommonViewBean()
        commonViewBean.titleBar = findViewById(R.id.title_bar)
        commonViewBean.tvLogView = findViewById(R.id.tv_show_log)
        commonViewBean.spinnerSelectPlacement = findViewById(R.id.spinner_1)
        commonViewBean.titleResId = R.string.anythink_title_splash
        return commonViewBean
    }

    override fun initListener() {
        findViewById<TextView>(R.id.is_ad_ready_btn).setOnClickListener(this)
        findViewById<TextView>(R.id.load_ad_btn).setOnClickListener(this)
        findViewById<TextView>(R.id.show_ad_btn).setOnClickListener(this)
    }

    override fun onSelectPlacementId(placementId: String?) {
        if (placementId != null) {
            initSplashAd(placementId)
        }
    }

    private fun initSplashAd(placementId: String) {
        val defaultConfig = ""
        //Mintegral
//        defaultConfig = "{\"unit_id\":1333033,\"nw_firm_id\":6,\"adapter_class\":\"com.anythink.network.mintegral.MintegralATSplashAdapter\",\"content\":\"{\\\"placement_id\\\":\\\"210169\\\",\\\"unitid\\\":\\\"276803\\\",\\\"countdown\\\":\\\"5\\\",\\\"allows_skip\\\":\\\"1\\\",\\\"orientation\\\":\\\"1\\\",\\\"appkey\\\":\\\"ef13ef712aeb0f6eb3d698c4c08add96\\\",\\\"suport_video\\\":\\\"1\\\",\\\"appid\\\":\\\"100947\\\"}\"}";

        //Tencent Ads
//        defaultConfig = "{\"unit_id\":1333176,\"nw_firm_id\":8,\"adapter_class\":\"com.anythink.network.gdt.GDTATSplashAdapter\",\"content\":\"{\\\"unit_id\\\":\\\"8863364436303842593\\\",\\\"zoomoutad_sw\\\":\\\"1\\\",\\\"app_id\\\":\\\"1101152570\\\"}\"}";

        //CSJ
//        defaultConfig = "{\"unit_id\":1333195,\"nw_firm_id\":15,\"adapter_class\":\"com.anythink.network.toutiao.TTATSplashAdapter\",\"content\":\"{\\\"personalized_template\\\":\\\"0\\\",\\\"zoomoutad_sw\\\":\\\"2\\\",\\\"button_type\\\":\\\"1\\\",\\\"dl_type\\\":\\\"2\\\",\\\"slot_id\\\":\\\"801121648\\\",\\\"app_id\\\":\\\"5001121\\\"}\"}";

        //Sigmob
//        defaultConfig = "{\"unit_id\":1333222,\"nw_firm_id\":29,\"adapter_class\":\"com.anythink.network.sigmob.SigmobATSplashAdapter\",\"content\":\"{\\\"placement_id\\\":\\\"ea1f8f21300\\\",\\\"app_id\\\":\\\"6878\\\",\\\"app_key\\\":\\\"8ebc1fd1c27e650c\\\"}\"}";

        //Baidu
//        defaultConfig = "{\"unit_id\":1329553,\"nw_firm_id\":22,\"adapter_class\":\"com.anythink.network.baidu.BaiduATSplashAdapter\",\"content\":\"{\\\"button_type\\\":\\\"0\\\",\\\"ad_place_id\\\":\\\"7854679\\\",\\\"app_id\\\":\\\"a7dd29d3\\\"}\"}";

        //Kuaishou
//        defaultConfig = "{\"unit_id\":1333246,\"nw_firm_id\":28,\"adapter_class\":\"com.anythink.network.ks.KSATSplashAdapter\",\"content\":\"{\\\"zoomoutad_sw\\\":\\\"1\\\",\\\"position_id\\\":\\\"4000000042\\\",\\\"app_id\\\":\\\"90009\\\",\\\"app_name\\\":\\\"90009\\\"}\"}";

        //Klevin
//        defaultConfig = "{\"unit_id\":1333253,\"nw_firm_id\":51,\"adapter_class\":\"com.anythink.network.klevin.KlevinATSplashAdapter\",\"content\":\"{\\\"pos_id\\\":\\\"30029\\\",\\\"app_id\\\":\\\"30008\\\"}\"}";
        Log.d(TAG, "initSplashAd: placementId $placementId")
        mSplashAd = ATSplashAd(this, placementId, ATSplashExListenerImpl(), 5000, defaultConfig)
        val localMap: Map<String, Any> = HashMap()
        mSplashAd!!.setLocalExtra(localMap)
        mSplashAd!!.setAdSourceStatusListener(ATAdSourceStatusListenerImpl())
    }

    private fun loadAd() {
        printLogOnUI(getString(R.string.anythink_ad_status_loading))
        if (mSplashAd != null) {
            mSplashAd!!.loadAd()
        }
    }

    private fun showAd() {
        if (mSplashAd == null) {
            return
        }
        val placementId = mCurrentPlacementId!!
        ATSplashAd.entryAdScenario(placementId, AdConst.SCENARIO_ID.SPLASH_AD_SCENARIO)
        if (mSplashAd!!.isAdReady) {
            val intent = Intent(this@SplashAdActivity, SplashAdShowActivity::class.java)
            intent.putExtra("placementId", placementId)
            //                intent.putExtra("custom_skip_view", isCustomSkipViewCheckBox.isChecked());
            startActivity(intent)
        }
    }

    private fun isAdReady() {
        val atAdStatusInfo = mSplashAd!!.checkAdStatus()
        if (atAdStatusInfo.isReady) {
            Log.i(TAG, "SplashAd is ready to show.")
            printLogOnUI("SplashAd is ready to show.")
        } else {
            Log.i(TAG, "SplashAd isn't ready to show.")
            printLogOnUI("SplashAd isn't ready to show.")
        }
        val atAdInfoList = mSplashAd!!.checkValidAdCaches()
        Log.i(TAG, "Valid Cahce size:" + (atAdInfoList?.size ?: 0))
        if (atAdInfoList != null) {
            for (adInfo in atAdInfoList) {
                Log.i(TAG, "\nCahce detail:$adInfo")
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.is_ad_ready_btn -> isAdReady()
            R.id.load_ad_btn -> loadAd()
            R.id.show_ad_btn -> showAd()
        }
    }


    override fun onDestroy() {
        if (mSplashAd != null) {
            mSplashAd!!.setAdListener(null)
            mSplashAd!!.setAdDownloadListener(null)
            mSplashAd!!.setAdSourceStatusListener(null)
        }
        super.onDestroy()
    }

    private inner class ATSplashExListenerImpl : ATSplashExListener {
        override fun onAdLoaded(isTimeout: Boolean) {
            Log.i(TAG, "onAdLoaded---------isTimeout:$isTimeout")
            printLogOnUI("onAdLoaded---------isTimeout:$isTimeout")
        }

        override fun onAdLoadTimeout() {
            Log.i(TAG, "onAdLoadTimeout---------")
            printLogOnUI("onAdLoadTimeout---------")
        }

        override fun onNoAdError(adError: AdError) {
            Log.i(TAG, "onNoAdError---------:" + adError.fullErrorInfo)
            printLogOnUI("onNoAdError---------:" + adError.fullErrorInfo)
        }

        override fun onAdShow(entity: ATAdInfo) {
            Log.i(TAG, "onAdShow---------:$entity")
            printLogOnUI("onAdShow---------")
        }

        override fun onAdClick(entity: ATAdInfo) {
            Log.i(TAG, "onAdClick---------:$entity")
            printLogOnUI("onAdClick---------")
        }

        override fun onAdDismiss(entity: ATAdInfo, splashAdExtraInfo: ATSplashAdExtraInfo) {
            Log.i(TAG, "onAdDismiss---------:$entity")
            printLogOnUI("onAdDismiss---------")
        }

        override fun onDeeplinkCallback(entity: ATAdInfo, isSuccess: Boolean) {
            Log.i(TAG, "onDeeplinkCallback---------：$entity isSuccess = $isSuccess")
            printLogOnUI("onDeeplinkCallback---------")
        }

        override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
            Log.i(TAG, "onDownloadConfirm--------- entity = $adInfo")
            printLogOnUI("onDownloadConfirm---------")
        }
    }

}