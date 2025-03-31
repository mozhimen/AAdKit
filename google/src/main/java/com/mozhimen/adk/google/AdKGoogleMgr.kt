package com.mozhimen.adk.google

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus
import com.mozhimen.adk.google.utils.UtilAdkGoogleCache
import com.mozhimen.kotlin.elemk.commons.IA_Listener
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.libk.google.android.gms.play.ads.optins.OMetaData_ANDROID_GMS_ADS_APPLICATION_ID

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
    @OMetaData_ANDROID_GMS_ADS_APPLICATION_ID
    fun init(context: Context, listener: IA_Listener<Boolean>) {
        MobileAds.initialize(context) { initializationStatus ->
            val adapterStatusMap = initializationStatus.adapterStatusMap
            for (adapterStatusName in adapterStatusMap.keys) {
                val status = adapterStatusMap[adapterStatusName]
                UtilKLogWrapper.d(TAG, "Adapter name: $adapterStatusName, Description: ${status?.description}, Latency: ${ status?.latency}"))
            }
            val readyAdapter: MutableMap.MutableEntry<String, AdapterStatus>? = initializationStatus.adapterStatusMap.entries.find {
                // 判断适配器初始化的状态
                // 准备就绪 AdapterStatus.State.READY
                // 没准备好 AdapterStatus.State.NOT_READY
                it.value.initializationState == AdapterStatus.State.READY
            }
            // 有任意一种适配器初始化成功就可以开始加载广告
            if (readyAdapter != null) {
                // 适配器的名称
                val adapterName = readyAdapter.key
                UtilKLogWrapper.d(TAG, "初始化: success adapterName $adapterName")
                UtilAdkGoogleCache.is_init_success = true
                listener.invoke(true)
                return@initialize
            }
            UtilKLogWrapper.d(TAG, "初始化: fail")
            UtilAdkGoogleCache.is_init_success = false
            listener.invoke(false)
        }
    }

    @JvmStatic
    fun isInitSuccess(): Boolean =
        UtilAdkGoogleCache.is_init_success
}