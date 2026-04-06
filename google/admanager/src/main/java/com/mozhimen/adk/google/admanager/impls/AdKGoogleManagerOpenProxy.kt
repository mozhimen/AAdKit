package com.mozhimen.adk.google.admanager.impls

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.mozhimen.adk.google.impls.AdKGoogleOpenProxy
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy

/**
 * @ClassName AdKYandexOpenProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/11
 * @Version 1.0
 */
@OApiCall_BindViewLifecycle
@OApiInit_ByLazy
@OApiCall_BindLifecycle
class AdKGoogleManagerOpenProxy : AdKGoogleOpenProxy() {
    override fun getAdRequest(): AdRequest {
        return AdManagerAdRequest.Builder().build()
    }
}