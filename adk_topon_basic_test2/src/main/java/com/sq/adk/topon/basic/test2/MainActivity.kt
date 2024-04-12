package com.sq.adk.topon.basic.test2

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.content.startContext
import com.sq.adk.topon.basic.test2.databinding.ActivityMainBinding

class MainActivity : BaseActivityVDB<ActivityMainBinding>() {
    fun goNativeAd(view: View) {
        startContext<NativeAdActivity>()
    }
}