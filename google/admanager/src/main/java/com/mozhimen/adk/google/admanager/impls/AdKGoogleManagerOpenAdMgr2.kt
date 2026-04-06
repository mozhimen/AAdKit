package com.mozhimen.adk.google.admanager.impls

import com.mozhimen.adk.google.impls.AdKGoogleOpenAdMgr2
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.api.OApiUse_BaseApplication
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM

@OApiCall_BindViewLifecycle
@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKGoogleManagerOpenAdMgr2(keyWord: String, adUnitId: String) : AdKGoogleOpenAdMgr2(keyWord, adUnitId) {
    override val _adkGoogleOpenProxy by UtilKLazyJVM.lazy_ofNone { AdKGoogleManagerOpenProxy() }
}
