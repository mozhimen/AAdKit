package com.mozhimen.adk.google.admanager.impls

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.mozhimen.adk.google.impls.AdKGoogleNativeProxy
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy

/**
 * @ClassName AdKGoogleNativeSimpleProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class AdKGoogleManagerNativeProxy : AdKGoogleNativeProxy() {
    override fun getAdRequest(): AdRequest {
        return AdManagerAdRequest.Builder().build()
    }
}