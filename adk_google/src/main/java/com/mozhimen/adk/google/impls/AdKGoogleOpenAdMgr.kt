package com.mozhimen.adk.google.impls

import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.lintk.optins.OApiInit_InApplication
import com.mozhimen.basick.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.basick.stackk.cb.StackKCb

@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKGoogleOpenAdMgr(adUnitId: String) : BaseAdKOpenAdMgr(adUnitId) {
    @OptIn(OApiCall_BindLifecycle::class)
    private val _adkGoogleOpenProxy by lazy { AdKGoogleOpenProxy() }

    init {
        _adkGoogleOpenProxy.apply {
            initOpenAdListener(object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    if (_showOpenAd.compareAndSet(false, true)) {
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
