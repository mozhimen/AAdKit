package com.mozhimen.adk.inmobi.utils

import com.mozhimen.basick.cachek.datastore.CacheKDS
import com.mozhimen.basick.cachek.datastore.temps.CacheKDSVarPropertyBoolean

/**
 * @ClassName CacheUtil
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
internal object CacheUtil {
    private val _cacheKDSProvider by lazy { CacheKDS.instance.with("adk_inmobi_ds") }
    var is_init_success by CacheKDSVarPropertyBoolean(_cacheKDSProvider, "is_init_success", false)
}