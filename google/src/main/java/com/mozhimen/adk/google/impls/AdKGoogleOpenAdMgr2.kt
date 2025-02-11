package com.mozhimen.adk.google.impls

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr2
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.lintk.optins.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM

@OApiCall_BindViewLifecycle
@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKGoogleOpenAdMgr2(keyWord: String, private val _adUnitId: String) : BaseAdKOpenAdMgr2(keyWord) {

    private val _adkGoogleOpenProxy by UtilKLazyJVM.lazy_ofNone { AdKGoogleOpenProxy() }

    /////////////////////////////////////////////////////////////////////

    override fun init(application: Application) {
        super.init(application)
        initOpenAdProxy(_adUnitId)
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
