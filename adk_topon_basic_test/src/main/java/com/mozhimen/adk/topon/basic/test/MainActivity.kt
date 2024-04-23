package com.mozhimen.adk.topon.basic.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import android.view.View
import com.anythink.core.api.ATSDK
import com.mozhimen.adk.topon.basic.test.databinding.ActivityMainBinding
import com.mozhimen.adk.topon.basic.test.uis.BannerAdActivity
import com.mozhimen.adk.topon.basic.test.uis.InterstitialAdActivity
import com.mozhimen.adk.topon.basic.test.uis.NativeMainActivity
import com.mozhimen.adk.topon.basic.test.uis.RewardVideoAdActivity
import com.mozhimen.adk.topon.basic.test.uis.SplashAdActivity
import com.mozhimen.adk.topon.basic.test.utils.PlacementIdUtil
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.content.UtilKClipboardManagerWrapper
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.basick.utilk.android.widget.applyTextStyleBold
import com.mozhimen.basick.utilk.android.widget.showToast
import org.json.JSONObject

class MainActivity : BaseActivityVDB<ActivityMainBinding>(), View.OnClickListener {
    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        vdb.tvVersion.text = getResources().getString(R.string.anythink_sdk_version, ATSDK.getSDKVersionName()) + PlacementIdUtil.MODE
        vdb.tvSdkDemo.applyTextStyleBold()

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
            R.id.interstitialBtn -> adPageClass = InterstitialAdActivity::class.java
            R.id.bannerBtn -> adPageClass = BannerAdActivity::class.java
            R.id.rewardedVideoBtn -> adPageClass = RewardVideoAdActivity::class.java
        }
        adPageClass?.let { startContext(it) }
    }
}