package com.mozhimen.adk.topon.test.annors

import androidx.annotation.StringDef

/**
 * @ClassName AAdNativeType
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/21
 * @Version 1.0
 */
@StringDef(value = [AAdNativeType.NATIVE_EXPRESS_TYPE, AAdNativeType.NATIVE_SELF_RENDER_TYPE])
@Retention(AnnotationRetention.SOURCE)
annotation class AAdNativeType {
    companion object {
        const val NATIVE_SELF_RENDER_TYPE = "1"
        const val NATIVE_EXPRESS_TYPE = "2"
    }
}
