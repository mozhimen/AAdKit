package com.mozhimen.adk.topon.basic.test

import com.mozhimen.adk.topon.basic.AdKTopOnBasicMgr
import com.mozhimen.adk.topon.basic.test.utils.PlacementIdUtil
import com.mozhimen.basick.elemk.android.app.bases.BaseApplication
import com.mozhimen.basick.lintk.optins.OApiInit_InApplication
import com.mozhimen.basick.lintk.optins.OApiMultiDex_InApplication

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

        AdKTopOnBasicMgr.init(this, PlacementIdUtil.getAppId(), PlacementIdUtil.getAppKey())
    }
}