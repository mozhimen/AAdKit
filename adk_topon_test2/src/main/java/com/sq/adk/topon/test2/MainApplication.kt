package com.sq.adk.topon.test2

import com.mozhimen.adk.topon.basic.AdKTopOnBasicMgr
import com.mozhimen.stackk.bases.BaseApplication
import com.mozhimen.kotlin.lintk.optins.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.OApiMultiDex_InApplication

/**
 * @ClassName MainApplication
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/21
 * @Version 1.0
 */
@OptIn(OApiMultiDex_InApplication::class)
class MainApplication : BaseApplication() {
    @OptIn(OApiInit_InApplication::class)
    override fun onCreate() {
        super.onCreate()

        AdKTopOnBasicMgr.init(this, "a65d81b5cee116", "a432b4ef5762c2dd776fa8779ef0ebe26")

    }
}