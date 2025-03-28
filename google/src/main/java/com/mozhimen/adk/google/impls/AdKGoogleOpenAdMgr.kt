package com.mozhimen.adk.google.impls

import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.lintk.optins.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM
import com.mozhimen.stackk.callback.StackKCb

@OptIn(OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKGoogleOpenAdMgr(adUnitId: String) : BaseAdKOpenAdMgr(adUnitId) {
    @OptIn(OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private val _adkGoogleOpenProxy by UtilKLazyJVM.lazy_ofNone { AdKGoogleOpenProxy() }

    init {
        _adkGoogleOpenProxy.apply {
            initOpenAdListener(object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    if (_showOpenAd.compareAndSet(false, true)) {
                        UtilKLogWrapper.d(TAG, "onAdLoaded: mediationAdapterClassName ${p0.responseInfo.mediationAdapterClassName}")
                        StackKCb.instance.getStackTopActivity()?.let {
                            _adkGoogleOpenProxy.showOpenAd(it)
                        }
                    }
                }
            }, null)
            initOpenAdParams(adUnitId)
            bindLifecycle(ProcessLifecycleOwner.get())
        }
    }
}
