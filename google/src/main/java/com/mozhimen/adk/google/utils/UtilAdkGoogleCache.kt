package com.mozhimen.adk.google.utils

import com.mozhimen.cachek.datastore.CacheKDS
import com.mozhimen.cachek.datastore.temps.CacheKDSVarPropertyBoolean

/**
 * @ClassName CacheUtil
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
internal object UtilAdkGoogleCache {
    private val _cacheKDSProvider by lazy { CacheKDS.instance.with("adk_google_ds") }
    var is_init_success by CacheKDSVarPropertyBoolean(_cacheKDSProvider, false)
}