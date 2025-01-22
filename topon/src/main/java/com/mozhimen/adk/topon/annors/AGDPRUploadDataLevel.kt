package com.mozhimen.adk.topon.annors

import androidx.annotation.IntDef
import com.anythink.core.api.ATSDK

/**
 * @ClassName AGDPRUploadDataLevel
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/12
 * @Version 1.0
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(
    ATSDK.PERSONALIZED,//设备数据允许上报
    ATSDK.NONPERSONALIZED,//设备数据不允许上报
    ATSDK.UNKNOWN //未知等级，只能通过getGDPRDataLevel方法获取，不能用setGDPRUploadDataLevel方法设置
)
annotation class AGDPRUploadDataLevel
