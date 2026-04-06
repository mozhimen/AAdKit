package com.mozhimen.adk.yandex

import android.content.Context
import com.mozhimen.kotlin.elemk.commons.I_Listener
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_InApplication
import com.yandex.mobile.ads.common.MobileAds

/**
 * @ClassName AdKYandexMgr
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/3/12 20:20
 * @Version 1.0
 */
object AdKYandexMgr {
    @JvmStatic
    @OApiInit_InApplication
    fun init(context: Context, onInit: I_Listener) {
        MobileAds.initialize(context) {
            // wait until sdk initialized before using Ads
            onInit.invoke()
        }
    }
}