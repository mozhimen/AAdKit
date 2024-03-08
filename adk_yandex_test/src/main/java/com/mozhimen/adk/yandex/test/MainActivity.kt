package com.mozhimen.adk.yandex.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mozhimen.adk.yandex.test.databinding.ActivityMainBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.viewbinding.BaseActivityVB
import com.mozhimen.basick.utilk.android.content.startContext

class MainActivity : BaseActivityVB<ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vb.mainBtnInterstitial.setOnClickListener {
            startContext<InterstitialAdActivity>()
        }
    }
}