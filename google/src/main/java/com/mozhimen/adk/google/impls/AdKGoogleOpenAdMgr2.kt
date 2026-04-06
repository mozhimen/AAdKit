package com.mozhimen.adk.google.impls

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr2
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.api.OApiUse_BaseApplication
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM

@OApiCall_BindViewLifecycle
@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
open class AdKGoogleOpenAdMgr2(keyWord: String, private val _adUnitId: String) : BaseAdKOpenAdMgr2(keyWord) {

    protected open val _adkGoogleOpenProxy by UtilKLazyJVM.lazy_ofNone { AdKGoogleOpenProxy() }

    /////////////////////////////////////////////////////////////////////

    override fun init(application: Application) {
        super.init(application)
        initOpenAdProxy(_adUnitId)
    }

    override fun initOpenAdProxy(adUnitId: String) {
        _adkGoogleOpenProxy.apply {
            initOpenAdListener(object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    UtilKLogWrapper.d(TAG, "onAdLoaded: mediationAdapterClassName ${p0.responseInfo.mediationAdapterClassName}")
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
