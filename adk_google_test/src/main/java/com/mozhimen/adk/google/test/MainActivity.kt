package com.mozhimen.adk.google.test

import android.os.Bundle
import android.view.View
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.adk.google.optins.OMetaData_GMS_ADS_APPLICATION_ID
import com.mozhimen.adk.google.test.databinding.ActivityMainBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.elemk.commons.I_Listener
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.basick.utilk.android.widget.showToast

class MainActivity : BaseActivityVB<ActivityMainBinding>() {
    private var _isInitSuccess = false

    @OptIn(OMetaData_GMS_ADS_APPLICATION_ID::class)
    override fun initView(savedInstanceState: Bundle?) {
        AdKGoogleMgr.init(this) { res ->
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