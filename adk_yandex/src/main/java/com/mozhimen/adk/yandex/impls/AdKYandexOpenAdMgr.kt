package com.mozhimen.adk.yandex.basic.impls

import androidx.lifecycle.ProcessLifecycleOwner
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr
import com.mozhimen.adk.yandex.basic.bases.BaseAppOpenAdLoadCallback
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.lintk.optins.OApiInit_InApplication
import com.mozhimen.basick.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.basick.stackk.cb.StackKCb
import com.yandex.mobile.ads.appopenad.AppOpenAd

@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKYandexOpenAdMgr(adUnitId: String) : BaseAdKOpenAdMgr(adUnitId) {

    @OptIn(OApiCall_BindLifecycle::class)
    private val _adkYandexOpenProxy by lazy { AdKYandexOpenProxy() }

    //////////////////////////////////////////////////////////////////////////////

    init {
        _adkYandexOpenProxy.apply {
            initOpenAdListener(object : BaseAppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    if (_showOpenAd.compareAndSet(false, true)) {
                        StackKCb.instance.getStackTopActivity()?.let {
                            _adkYandexOpenProxy.showOpenAd(it)
                        }
                    }
                }
            }, null)
            initOpenAdParams(adUnitId)
            bindLifecycle(ProcessLifecycleOwner.get())
        }
    }
}
