package com.mozhimen.adk.topon.impls
import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr2
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.adk.topon.bases.BaseATSplashExCallback
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.api.OApiUse_BaseApplication
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM

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
class AdKTopOnOpenAdMgr2(keyWord: String,private val _adUnitId: String) : BaseAdKOpenAdMgr2(keyWord) {

    @OptIn(OApiCall_BindLifecycle::class)
    private val _adkTopOnOpenProxy by UtilKLazyJVM.lazy_ofNone { AdKTopOnOpenProxy() }

    /////////////////////////////////////////////////////////////////////

    override fun init(application: Application) {
        super.init(application)
        initOpenAdProxy(_adUnitId)
    }

    override fun initOpenAdProxy(adUnitId: String) {
        _adkTopOnOpenProxy.apply {
            initOpenAdListener(
                object : BaseATSplashExCallback() {
                    override fun onAdLoaded(p0: Boolean) {
                        _isAdLoad = true
                    }
                }, null
            )
            initOpenAdParams(adUnitId, "")
            bindLifecycle(ProcessLifecycleOwner.get())
        }
    }

    override fun getAdkOpenProxy(): IAdKOpenProxy {
        return _adkTopOnOpenProxy
    }
}