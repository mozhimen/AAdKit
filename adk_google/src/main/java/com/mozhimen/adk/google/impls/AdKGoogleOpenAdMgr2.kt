package com.mozhimen.adk.google.impls

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr2
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.lintk.optins.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.OApiUse_BaseApplication

@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKGoogleOpenAdMgr2(application: Application, keyWord: String, adUnitId: String) : BaseAdKOpenAdMgr2(application, keyWord, adUnitId) {

    private val _adkGoogleOpenProxy by lazy_ofNone { AdKGoogleOpenProxy() }

    init {
        initOpenAdProxy(adUnitId)
    }

    override fun initOpenAdProxy(adUnitId: String) {
        _adkGoogleOpenProxy.apply {
            initOpenAdListener(object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    _isAdLoad = true
                }
            }, null)
            initOpenAdParams(adUnitId)
            bindLifecycle(ProcessLifecycleOwner.get())
        }
    }

    override fun getAdkOpenProxy(): IAdKOpenProxy {
        return _adkGoogleOpenProxy
    }
}
