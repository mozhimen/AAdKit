package com.mozhimen.adk.topon.basic.impls

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.anythink.core.api.ATAdInfo
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr2
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.adk.topon.basic.bases.BaseATAdSourceStatusCallback
import com.mozhimen.adk.topon.basic.bases.BaseATSplashExCallback
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.lintk.optins.OApiInit_InApplication
import com.mozhimen.basick.lintk.optins.OApiUse_BaseApplication

/**
 * @ClassName AdKTopOnOpenMgr2
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/11
 * @Version 1.0
 */
@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKTopOnOpenAdMgr2(application: Application, keyWord: String, adUnitId: String) : BaseAdKOpenAdMgr2(application, keyWord) {

    @OptIn(OApiCall_BindLifecycle::class)
    private val _adkTopOnOpenProxy by lazy { AdKTopOnOpenProxy() }

    init {
        initOpenAdProxy(adUnitId)
    }

    override fun initOpenAdProxy(adUnitId: String) {
        _adkTopOnOpenProxy.apply {
            initOpenAdListener(
                null,
                object : BaseATAdSourceStatusCallback() {
                    override fun onAdSourceLoadFilled(adInfo: ATAdInfo) {
                        _isAdLoad = true
                    }
                })
            initOpenAdParams(adUnitId, "")
            bindLifecycle(ProcessLifecycleOwner.get())
        }
    }

    override fun getAdkOpenProxy(): IAdKOpenProxy {
        return _adkTopOnOpenProxy
    }
}