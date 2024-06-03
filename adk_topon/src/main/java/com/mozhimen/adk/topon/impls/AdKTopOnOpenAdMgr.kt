package com.mozhimen.adk.topon.impls

import androidx.lifecycle.ProcessLifecycleOwner
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr
import com.mozhimen.adk.topon.bases.BaseATSplashExCallback
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.lintk.optins.OApiInit_InApplication
import com.mozhimen.basick.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.basick.stackk.cb.StackKCb

/**
 * @ClassName AdKTopOnOpenAdMgr
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/11
 * @Version 1.0
 */
@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKTopOnOpenAdMgr(adUnitId: String) : BaseAdKOpenAdMgr(adUnitId) {

    @OptIn(OApiCall_BindLifecycle::class)
    private val _adkTopOnOpenProxy by lazy { AdKTopOnOpenProxy() }

    //////////////////////////////////////////////////////////////////////////////

    init {
        _adkTopOnOpenProxy.apply {
            initOpenAdListener(object : BaseATSplashExCallback() {
                override fun onAdLoaded(p0: Boolean) {
                    if (_showOpenAd.compareAndSet(false, true)) {
                        StackKCb.instance.getStackTopActivity()?.let {
                            _adkTopOnOpenProxy.showOpenAd(it)
                        }
                    }
                }
            }, null)
            initOpenAdParams(adUnitId, "")
            bindLifecycle(ProcessLifecycleOwner.get())
        }
    }
}