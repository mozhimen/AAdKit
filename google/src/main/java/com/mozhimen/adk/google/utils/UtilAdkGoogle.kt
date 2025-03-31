package com.mozhimen.adk.google.utils

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnAdInspectorClosedListener

/**
 * @ClassName UtilAdkGoogle
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/31
 * @Version 1.0
 */
object UtilAdkGoogle {
    @JvmStatic
    fun openAdInspector(context: Context, listener: OnAdInspectorClosedListener) {
        MobileAds.openAdInspector(context, listener)
    }
}