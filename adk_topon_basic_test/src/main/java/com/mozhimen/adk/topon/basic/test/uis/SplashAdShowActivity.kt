package com.mozhimen.adk.topon.basic.test.uis

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATAdSourceStatusListener
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdExtraInfo
import com.anythink.splashad.api.ATSplashExListener
import com.anythink.splashad.api.ATSplashSkipAdListener
import com.anythink.splashad.api.ATSplashSkipInfo
import com.mozhimen.adk.topon.basic.helpers.SplashZoomOutManager
import com.mozhimen.adk.topon.basic.test.cons.AdConst
import com.mozhimen.adk.topon.basic.test.databinding.SplashAdShowBinding
import com.mozhimen.adk.topon.basic.test.helpers.SplashEyeAdHolder
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB

/**
 * @ClassName SplashAdShowActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */
class SplashAdShowActivity : BaseActivityVDB<SplashAdShowBinding>(), ATSplashExListener {
    private var splashAd: ATSplashAd? = null
    private val container: FrameLayout? = null
    var inForeBackground = false
    var needShowSplashAd = false
    var hasHandleJump = false
    var needJump = false

    override fun initView(savedInstanceState: Bundle?) {
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
        val placementId = intent.getStringExtra("placementId")
        splashAd = ATSplashAd(this, placementId, this, 5000, defaultConfig)

        splashAd!!.setAdSourceStatusListener(object : ATAdSourceStatusListener {
            override fun onAdSourceBiddingAttempt(adInfo: ATAdInfo) {
                Log.i(TAG, "onAdSourceBiddingAttempt: $adInfo")
            }

            override fun onAdSourceBiddingFilled(adInfo: ATAdInfo) {
                Log.i(TAG, "onAdSourceBiddingFilled: $adInfo")
            }

            override fun onAdSourceBiddingFail(adInfo: ATAdInfo, adError: AdError) {
                Log.i(TAG, "onAdSourceBiddingFail Info: $adInfo")
                Log.i(TAG, "onAdSourceBiddingFail error: " + adError.fullErrorInfo)
            }

            override fun onAdSourceAttempt(adInfo: ATAdInfo) {
                Log.i(TAG, "onAdSourceAttempt: $adInfo")
            }

            override fun onAdSourceLoadFilled(adInfo: ATAdInfo) {
                Log.i(TAG, "onAdSourceLoadFilled: $adInfo")
            }

            override fun onAdSourceLoadFail(adInfo: ATAdInfo, adError: AdError) {
                Log.i(TAG, "onAdSourceLoadFail Info: $adInfo")
                Log.i(TAG, "onAdSourceLoadFail error: " + adError.fullErrorInfo)
            }
        })

        if (splashAd!!.isAdReady()) {
            Log.i(TAG, "SplashAd is ready to show.")
            //splashAd.show(SplashAdShowActivity.this, container);
            //showAdWithCustomSkipView();//show with customSkipView
            splashAd!!.show(this@SplashAdShowActivity, container, AdConst.SCENARIO_ID.SPLASH_AD_SCENARIO)
        } else {
            Log.i(TAG, "SplashAd isn't ready to show, start to request.")
            splashAd!!.loadAd()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAdWithCustomSkipView() {
        val countDownDuration: Long = 5000
        val callbackInterval: Long = 1000
        vdb.splashAdSkip.text = (countDownDuration / 1000).toInt().toString() + "s | Skip"

//        splashAd.show(this, container, new ATSplashSkipInfo(skipView, countDownDuration, callbackInterval, new ATSplashSkipAdListener() {
//            @Override
//            public void onAdTick(long duration, long remainder) {
//                skipView.setText(((int) (remainder / 1000)) + "s | Skip");
//            }
//
//            @Override
//            public void isSupportCustomSkipView(boolean isSupport) {
//                Log.i(TAG, "isSupportCustomSkipView: " + isSupport);
//                if (isSupport) {
//                    skipView.setVisibility(View.VISIBLE);
//                }
//            }
//        }));
        splashAd!!.show(this, container, ATSplashSkipInfo(vdb.splashAdSkip, countDownDuration, callbackInterval, object : ATSplashSkipAdListener {
            override fun onAdTick(duration: Long, remainder: Long) {
                vdb.splashAdSkip.text = (remainder / 1000).toInt().toString() + "s | Skip"
            }

            override fun isSupportCustomSkipView(isSupport: Boolean) {
                Log.i(TAG, "isSupportCustomSkipView: $isSupport")
                if (isSupport) {
                    vdb.splashAdSkip.visibility = View.VISIBLE
                }
            }
        }), AdConst.SCENARIO_ID.SPLASH_AD_SCENARIO)
    }

    override fun onAdLoaded(isTimeout: Boolean) {
        Log.i(TAG, "onAdLoaded---------isTimeout:$isTimeout")
        if (!inForeBackground) {
            needShowSplashAd = true
            return
        }
        if (!splashAd!!.isAdReady) {
            Log.e(TAG, "onAdLoaded: no cache")
            jumpToMainActivity()
            return
        }
        splashAd!!.show(this, container)
//        showAdWithCustomSkipView();//show with customSkipView
//            splashAd.show(this, container, "f628c7999265cd");
    }
    override fun onDeeplinkCallback(adInfo: ATAdInfo?, isSuccess: Boolean) {
        Log.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess)
    }

    fun jumpToMainActivity() {
        if (!needJump) {
            needJump = true
            return
        }
        if (!hasHandleJump) {
            hasHandleJump = true
            if (SplashEyeAdHolder.splashEyeAd != null) {
                try {
                    val zoomOutManager: SplashZoomOutManager = SplashZoomOutManager.getInstance(applicationContext)
                    zoomOutManager.setSplashInfo(
                        container!!.getChildAt(0),
                        window.decorView
                    )
                } catch (e: Throwable) {
                    Log.e(TAG, "jumpToMainActivity: ------------------------------------------ error")
                    e.printStackTrace()
                }
                val intent = Intent(this, TestMainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            Toast.makeText(this.applicationContext, "start your MainActivity.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onAdLoadTimeout() {
        Log.i(TAG, "onAdLoadTimeout---------")
        Toast.makeText(applicationContext, "onAdLoadTimeout", Toast.LENGTH_SHORT).show()
    }

    override fun onNoAdError(adError: AdError) {
        Log.i(TAG, "onNoAdError---------:" + adError.fullErrorInfo)
        jumpToMainActivity()
    }

    override fun onAdShow(entity: ATAdInfo) {
        Log.i(TAG, "onAdShow:\n$entity")
    }

    override fun onAdClick(entity: ATAdInfo) {
        Log.i(TAG, "onAdClick:\n$entity")
    }

    override fun onAdDismiss(entity: ATAdInfo, splashAdExtraInfo: ATSplashAdExtraInfo) {
        Log.i(TAG, """
     onAdDismiss type:${splashAdExtraInfo.dismissType}
     $entity
     """.trimIndent()
        )
        SplashEyeAdHolder.splashEyeAd = splashAdExtraInfo.atSplashEyeAd
        jumpToMainActivity()
    }

    override fun onResume() {
        super.onResume()
        inForeBackground = true
        if (needJump) {
            jumpToMainActivity()
        }
        needJump = true
        if (needShowSplashAd) {
            needShowSplashAd = false
            if (splashAd!!.isAdReady) {
//                splashAd.show(this, container);
                splashAd!!.show(this, container, AdConst.SCENARIO_ID.SPLASH_AD_SCENARIO)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        inForeBackground = false
        needJump = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (splashAd != null) {
            splashAd!!.setAdListener(null)
            splashAd!!.setAdDownloadListener(null)
            splashAd!!.setAdSourceStatusListener(null)
        }
    }

    override fun onDownloadConfirm(context: Context?, adInfo: ATAdInfo?, networkConfirmInfo: ATNetworkConfirmInfo?) {}
}