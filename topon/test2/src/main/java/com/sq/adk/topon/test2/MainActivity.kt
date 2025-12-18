package com.sq.adk.topon.test2

import android.view.View
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.sq.adk.topon.test2.databinding.ActivityMainBinding

class MainActivity : BaseActivityVDB<ActivityMainBinding>() {
    fun goNativeAd(view: View) {
        startContext<NativeAdActivity>()
    }
}