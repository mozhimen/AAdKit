package com.mozhimen.adk.google

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus
import com.mozhimen.abilityk.google.android.gms.ads.optins.OMetaData_GMS_ADS_APPLICATION_ID
import com.mozhimen.adk.google.utils.CacheUtil
import com.mozhimen.basick.elemk.commons.IA_Listener
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName AdKGoogle
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
object AdKGoogleMgr : IUtilK {
//    @JvmStatic
//    @OMetaData_GMS_ADS_APPLICATION_ID
//    fun init(context: Context) {
//        MobileAds.initialize(context)
//    }

//    @JvmStatic
//    @OMetaData_GMS_ADS_APPLICATION_ID
//    fun init(context: Context, listener: OnInitializationCompleteListener) {
//        MobileAds.initialize(context, listener)
//    }

    @JvmStatic
    @OMetaData_GMS_ADS_APPLICATION_ID
    fun init(context: Context, listener: IA_Listener<Boolean>) {
        MobileAds.initialize(context) { initializationStatus ->
            val readyAdapter = initializationStatus.adapterStatusMap.entries.find {
                // 判断适配器初始化的状态
                // 准备就绪 AdapterStatus.State.READY
                // 没准备好 AdapterStatus.State.NOT_READY
                it.value.initializationState == AdapterStatus.State.READY
            }
            // 有任意一种适配器初始化成功就可以开始加载广告
            if (readyAdapter != null) {
                // 适配器的名称
                val adapterName = readyAdapter.key
                Log.d(TAG, "初始化: success adapterName $adapterName")
                CacheUtil.is_init_success = true
                listener.invoke(true)
                return@initialize
            }
            Log.d(TAG, "初始化: fail")
            CacheUtil.is_init_success = false
            listener.invoke(false)
        }
    }

    @JvmStatic
    fun isInitSuccess(): Boolean =
        CacheUtil.is_init_success
}