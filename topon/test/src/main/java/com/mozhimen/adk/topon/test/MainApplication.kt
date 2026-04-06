package com.mozhimen.adk.topon.test

import com.mozhimen.adk.topon.AdKTopOnMgr
import com.mozhimen.adk.topon.test.utils.PlacementIdUtil
import com.mozhimen.stackk.bases.BaseApplication
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.api.OApiMultiDex_InApplication

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

        AdKTopOnMgr.init(this, PlacementIdUtil.getAppId(), PlacementIdUtil.getAppKey())

        //test
//        ATDebuggerUITest.showDebuggerUI(this)
    }
}