package com.mozhimen.adk.google.test

import android.os.Bundle
import android.util.Log
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.View
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.adk.google.optins.OMetaData_GMS_ADS_APPLICATION_ID
import com.mozhimen.adk.google.test.databinding.ActivityMainBinding
import com.mozhimen.basick.bases.databinding.BaseActivityVDB
import com.mozhimen.kotlin.elemk.commons.I_Listener
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.kotlin.utilk.android.widget.showToast

class MainActivity : BaseActivityVDB<ActivityMainBinding>() {
    private var _isInitSuccess = false

    @OptIn(OMetaData_GMS_ADS_APPLICATION_ID::class)
    override fun initView(savedInstanceState: Bundle?) {
        AdKGoogleMgr.init(this) { res ->
            UtilKLogWrapper.d(TAG, "initView: isInitSuccess $res")
            _isInitSuccess = res
        }
    }

    private fun checkAdsInit(block: I_Listener) {
        if (_isInitSuccess) {
            block.invoke()
        } else {
            "广告SDK初始化失败,请排查原因".showToast()
        }
    }

    fun goAdsBanner(view: View) {
        checkAdsInit {
            startContext<AdsBannerActivity>()
        }
    }

    fun goAdsInterstitial(view: View) {
        checkAdsInit {
            startContext<AdsInterstitialActivity>()
        }
    }

    fun goAdsNative(view: View) {
        checkAdsInit {
            startContext<AdsNativeActivity>()
        }
    }

    fun goAdsRewardedActivity(view: View) {
        checkAdsInit {
            startContext<AdsRewardedActivity>()
        }
    }
}