package com.mozhimen.adk.yandex.impls

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr2
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.adk.yandex.bases.BaseAppOpenAdLoadCallback
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.lintk.optins.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.OApiUse_BaseApplication
import com.yandex.mobile.ads.appopenad.AppOpenAd

@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKYandexOpenAdMgr2(application: Application, keyWord: String, adUnitId: String) : BaseAdKOpenAdMgr2(keyWord) {

    @OptIn(OApiCall_BindLifecycle::class)
    private val _adkYandexOpenProxy by lazy { AdKYandexOpenProxy() }

    //////////////////////////////////////////////////////////////////////////////

    init {
        initOpenAdProxy(adUnitId)
    }

    override fun initOpenAdProxy(adUnitId: String) {
        _adkYandexOpenProxy.apply {
            initOpenAdListener(object : BaseAppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    _isAdLoad = true
                }
            }, null)
            initOpenAdParams(adUnitId)
            bindLifecycle(ProcessLifecycleOwner.get())
        }
    }

    override fun getAdkOpenProxy(): IAdKOpenProxy {
        return _adkYandexOpenProxy
    }
}
