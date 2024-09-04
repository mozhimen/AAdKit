package com.mozhimen.adk.yandex.basic.optins

import com.mozhimen.adk.yandex.basic.cons.CMetaData
import com.mozhimen.kotlin.lintk.annors.AManifestRequire

/**
 * @ClassName OMetaData_YANDEX_ADS_APPLICATION_ID
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/8
 * @Version 1.0
 */
@AManifestRequire(CMetaData.YANDEX_ADS_APPLICATION_ID)
@RequiresOptIn("The api is must add this metadata to your AndroidManifest.xml. 需要声明此Metadata到你的AndroidManifest.xml", RequiresOptIn.Level.WARNING)
annotation class OMetaData_YANDEX_ADS_APPLICATION_ID