package com.mozhimen.adk.topon.impls

import android.app.Activity
import androidx.annotation.MainThread
import androidx.lifecycle.ProcessLifecycleOwner
import com.anythink.interstitial.api.ATInterstitialAutoEventListener
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @ClassName AdKTopOnInterstitalAdMgr2
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/8/5
 * @Version 1.0
 */
object AdKTopOnInterstitialAdMgr {
    private val _isInit = AtomicBoolean(false)

    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    private val _adkTopOnInterstitialAutoLoadProxy by lazy_ofNone { AdKTopOnInterstitialAutoLoadProxy() }

    //////////////////////////////////////////////////////////////////

    @JvmStatic
    fun isInit(): Boolean {
        return _isInit.get()
    }

    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    @JvmStatic
    @MainThread
    fun init(placementId: String) {
        if (_isInit.compareAndSet(false, true)) {
            _adkTopOnInterstitialAutoLoadProxy.apply {
                initInterstitialAdParams(placementId)
                bindLifecycle(ProcessLifecycleOwner.get())
            }
        }
    }

    @JvmStatic
    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    fun isInterstitialAdReady(placementId: String): Boolean =
        isInit()&& _adkTopOnInterstitialAutoLoadProxy.isInterstitialAdReady(placementId)

    @JvmStatic
    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    fun showInterstitialAd(activity: Activity, placementId: String, atInterstitialAutoEventListener: ATInterstitialAutoEventListener) {
        _adkTopOnInterstitialAutoLoadProxy.showInterstitialAd(activity, placementId, atInterstitialAutoEventListener)
    }
}