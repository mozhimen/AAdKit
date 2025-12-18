package com.mozhimen.adk.yandex.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mozhimen.adk.yandex.test.databinding.ActivityMainBinding
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.uik.databinding.bases.viewbinding.activity.BaseActivityVB

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