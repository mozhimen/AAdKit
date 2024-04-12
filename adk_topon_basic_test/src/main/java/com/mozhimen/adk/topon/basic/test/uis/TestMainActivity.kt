package com.mozhimen.adk.topon.basic.test.uis

import android.R
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.anythink.splashad.api.ATSplashEyeAdListener
import com.mozhimen.adk.topon.basic.helpers.SplashZoomOutManager
import com.mozhimen.adk.topon.basic.test.helpers.SplashEyeAdHolder
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName TestMainActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

class TestMainActivity : Activity(), IUtilK {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootLayout = getRootLayout()
        setContentView(rootLayout)
    }

    private fun getRootLayout(): FrameLayout {
        val rootLayout = FrameLayout(this)
        rootLayout.setBackgroundColor(Color.WHITE)
        val textView = TextView(this)
        textView.text = "Test Main Activity"
        textView.setTextColor(Color.BLACK)
        textView.textSize = 25f
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        rootLayout.addView(textView, layoutParams)
        return rootLayout
    }

    private fun showSplashEyeAd() {
        if (SplashEyeAdHolder.splashEyeAd == null) {
            return
        }
        SplashEyeAdHolder.splashEyeAd!!.show(this@TestMainActivity, null, object : ATSplashEyeAdListener {
            override fun onAnimationStart(splashView: View) {
                val zoomOutManager: SplashZoomOutManager = SplashZoomOutManager.getInstance(applicationContext)
                val suggestedSize: IntArray = SplashEyeAdHolder.splashEyeAd!!.getSuggestedSize(applicationContext)
                if (suggestedSize != null) {
                    zoomOutManager.setSplashEyeAdViewSize(suggestedSize[0], suggestedSize[1])
                }
                zoomOutManager.startZoomOut(
                    window.decorView as ViewGroup,
                    findViewById(R.id.content), object : SplashZoomOutManager.AnimationCallBack {
                        override fun animationStart(animationTime: Int) {}
                        override fun animationEnd() {
                            Log.i(TAG, "animationEnd---------: eye")
                            SplashEyeAdHolder.splashEyeAd?.onFinished()
                        }
                    })
            }

            override fun onAdDismiss(isSupportEyeSplash: Boolean, errorMsg: String) {
                Log.i(TAG, "onAdDismiss---------: close eye ad")
                val zoomOutManager: SplashZoomOutManager = SplashZoomOutManager.getInstance(applicationContext)
                zoomOutManager.clearStaticData()
                SplashEyeAdHolder.splashEyeAd?.destroy()
                SplashEyeAdHolder.splashEyeAd = null
            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        showSplashEyeAd()
    }
}

