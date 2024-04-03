package com.mozhimen.adk.yandex.basic.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mozhimen.adk.yandex.basic.test.databinding.ActivityMainBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.viewbinding.BaseActivityVB
import com.mozhimen.basick.utilk.android.content.startContext

class MainActivity : BaseActivityVB<ActivityMainBinding>() {

    fun InterstitialAd(view: View) {
        startContext<InterstitialAdActivity>()
    }

    fun InlineBannerAd(view: View) {
        startContext<InlineBannerAdActivity>()
    }

    fun OpenAd(view: View) {
        startContext<OpenAdActivity>()
    }

    fun NativeAd(view: View) {
        startContext<NativeAdActivity>()
    }
}