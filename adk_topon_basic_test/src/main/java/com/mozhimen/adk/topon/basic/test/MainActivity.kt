package com.mozhimen.adk.topon.basic.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.anythink.core.api.ATSDK
import com.mozhimen.adk.topon.basic.test.databinding.ActivityMainBinding
import com.mozhimen.adk.topon.basic.test.utils.PlacementIdUtil
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.utilk.android.content.UtilKClipboardManager
import com.mozhimen.basick.utilk.android.content.UtilKClipboardManagerWrapper
import com.mozhimen.basick.utilk.android.widget.applyTextStyleBold
import org.json.JSONObject

class MainActivity : BaseActivityVB<ActivityMainBinding>(), View.OnClickListener {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        vb.tvVersion.text = getResources().getString(R.string.anythink_sdk_version, ATSDK.getSDKVersionName()) + PlacementIdUtil.MODE
        vb.tvSdkDemo.applyTextStyleBold()

        vb.nativeBtn.setOnClickListener(this)
        vb.splashBtn.setOnClickListener(this)
        vb.bannerBtn.setOnClickListener(this)
        vb.interstitialBtn.setOnClickListener(this)
        vb.rewardedVideoBtn.setOnClickListener(this)

        ATSDK.testModeDeviceInfo(this) { deviceInfo ->
            Log.d(TAG, "initView: deviceInfo $deviceInfo")
            if (!TextUtils.isEmpty(deviceInfo)) {
                try {
                    val jsonObject = JSONObject(deviceInfo)
                    val gaid = jsonObject.optString("GAID")
                    runOnUiThread {
                        vb.tvDeviceId.text = getResources().getString(R.string.anythink_click_to_copy_device_id, gaid)
                        vb.tvDeviceId.setOnClickListener {
                            UtilKClipboardManagerWrapper.
                            copyContentToClipboard(this@MainActivity, gaid)
                            Toast.makeText(this@MainActivity, "Gaid：$gaid", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onClick(v: View?) {

    }
}