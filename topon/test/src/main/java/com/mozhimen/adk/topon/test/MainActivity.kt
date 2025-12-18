package com.mozhimen.adk.topon.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.anythink.core.api.ATSDK
import com.mozhimen.adk.topon.test.databinding.ActivityMainBinding
import com.mozhimen.adk.topon.test.uis.NativeMainActivity
import com.mozhimen.adk.topon.test.uis.RewardVideoAdActivity
import com.mozhimen.adk.topon.test.uis.SplashAdActivity
import com.mozhimen.adk.topon.test.utils.PlacementIdUtil
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.kotlin.utilk.android.widget.applyTypeface_BOLD
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB

class MainActivity : BaseActivityVDB<ActivityMainBinding>(), View.OnClickListener {
    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        vdb.tvVersion.text = getResources().getString(R.string.anythink_sdk_version, ATSDK.getSDKVersionName()) + PlacementIdUtil.MODE
        vdb.tvSdkDemo.applyTypeface_BOLD()

        vdb.nativeBtn.setOnClickListener(this)
        vdb.splashBtn.setOnClickListener(this)
        vdb.bannerBtn.setOnClickListener(this)
        vdb.interstitialBtn.setOnClickListener(this)
        vdb.rewardedVideoBtn.setOnClickListener(this)

//        ATSDK.testModeDeviceInfo(this) { deviceInfo ->
//            UtilKLogWrapper.d(TAG, "initView: deviceInfo $deviceInfo")
//            if (!TextUtils.isEmpty(deviceInfo)) {
//                try {
//                    val jsonObject = JSONObject(deviceInfo)
//                    val gaid = jsonObject.optString("GAID")
//                    runOnUiThread {
//                        vdb.tvDeviceId.text = getResources().getString(R.string.anythink_click_to_copy_device_id, gaid)
//                        vdb.tvDeviceId.setOnClickListener {
//                            UtilKClipboardManagerWrapper.applyCopyText("Label", gaid)
//                            "Gaid：$gaid".showToast()
//                        }
//                    }
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                }
//            }
//        }
    }

    override fun onClick(v: View) {
        var adPageClass: Class<*>? = null
        when (v.id) {
            R.id.nativeBtn -> adPageClass = NativeMainActivity::class.java
            R.id.splashBtn -> adPageClass = SplashAdActivity::class.java
            R.id.interstitialBtn -> adPageClass = com.mozhimen.adk.topon.test.uis.InterstitialAdActivity::class.java
            R.id.bannerBtn -> adPageClass = com.mozhimen.adk.topon.test.uis.BannerAdActivity::class.java
            R.id.rewardedVideoBtn -> adPageClass = RewardVideoAdActivity::class.java
        }
        adPageClass?.let { startContext(it) }
    }
}