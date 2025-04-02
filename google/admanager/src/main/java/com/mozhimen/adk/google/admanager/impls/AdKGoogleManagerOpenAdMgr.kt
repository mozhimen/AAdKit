package com.mozhimen.adk.google.admanager.impls

import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.mozhimen.adk.basic.bases.BaseAdKOpenAdMgr
import com.mozhimen.adk.google.impls.AdKGoogleOpenAdMgr
import com.mozhimen.adk.google.impls.AdKGoogleOpenProxy
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
class AdKGoogleManagerOpenAdMgr(adUnitId: String) : AdKGoogleOpenAdMgr(adUnitId) {
    @OptIn(OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override val _adkGoogleOpenProxy by UtilKLazyJVM.lazy_ofNone { AdKGoogleManagerOpenProxy() }
}
