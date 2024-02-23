package com.mozhimen.adk.topon.basic

import android.content.Context
import android.util.Log
import com.anythink.core.api.ATSDK
import com.mozhimen.basick.lintk.optins.OApiInit_InApplication
import com.mozhimen.basick.utilk.android.app.UtilKRunningAppProcessInfo
import com.mozhimen.basick.utilk.bases.IUtilK
import com.mozhimen.webk.basic.WebKMgr

/**
 * @ClassName AdKTopOnBasic
 * @Description 双端都未上架的可以接：mtg, unity, topon adx
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/21
 * @Version 1.0
 */

object AdKTopOnBasicMgr : IUtilK {
    @JvmStatic
    @OApiInit_InApplication
    fun init(context: Context, topOnAppId: String, topOnAppKey: String, isDebug: Boolean = true) {
        if (topOnAppId.isEmpty() || topOnAppKey.isEmpty()) {
            Log.e(TAG, "init: fail")
            return
        }
        Log.d(TAG, "init: topOnAppId $topOnAppId topOnAppKey $topOnAppKey")

        WebKMgr.init(context)

        if (UtilKRunningAppProcessInfo.isMainProcess(context)) {
            if (isDebug) {
                ATSDK.setNetworkLogDebug(true)
                ATSDK.integrationChecking(context)
            }

//        国内隐私政策
//        ATSDK.deniedUploadDeviceInfo(
//                DeviceDataInfo.DEVICE_SCREEN_SIZE
//                , DeviceDataInfo.ANDROID_ID
//                , DeviceDataInfo.APP_PACKAGE_NAME
//                , DeviceDataInfo.APP_VERSION_CODE
//                , DeviceDataInfo.APP_VERSION_NAME
//                , DeviceDataInfo.BRAND
//                , DeviceDataInfo.GAID
//                , DeviceDataInfo.LANGUAGE
//                , DeviceDataInfo.MCC
//                , DeviceDataInfo.MNC
//                , DeviceDataInfo.MODEL
//                , DeviceDataInfo.ORIENTATION
//                , DeviceDataInfo.OS_VERSION_CODE
//                , DeviceDataInfo.OS_VERSION_NAME
//                , DeviceDataInfo.TIMEZONE
//                , DeviceDataInfo.USER_AGENT
//                , DeviceDataInfo.NETWORK_TYPE
//                , ChinaDeviceDataInfo.IMEI
//                , ChinaDeviceDataInfo.MAC
//                , ChinaDeviceDataInfo.OAID
//                , DeviceDataInfo.INSTALLER
//
//        );
//            ATSDK.setPersonalizedAdStatus(ATAdConst.PRIVACY.PERSIONALIZED_ALLOW_STATUS)
//        ATSDK.setUseHTTP(true);

            ATSDK.init(context, topOnAppId, topOnAppKey);//初始化SDK
//        or
//        ATNetworkConfig atNetworkConfig = getAtNetworkConfig();
//        ATSDK.init(this, appid, appKey, atNetworkConfig);
        }
    }

//    private fun getAtNetworkConfig(): ATNetworkConfig {
//        val atInitConfigs: List<ATInitConfig> = ArrayList()
//        ATInitConfig pangleATInitConfig = new PangleATInitConfig("8025677");
//        ATInitConfig mintegralATInitConfig = new MintegralATInitConfig("100947", "ef13ef712aeb0f6eb3d698c4c08add96");
//        ATInitConfig facebookATInitConfig = new FacebookATInitConfig();
//        ATInitConfig vungleAtInitConfig = new VungleATInitConfig("5ad59a853d927044ac75263a");
//        ATInitConfig adColonyATInitConfig = new AdColonyATInitConfig("app251236acbb494d48a8", "vz6ddfc996216e4c2b99", null);
//        ATInitConfig myTargetATInitConfig = new MyTargetATInitConfig();
//
//        atInitConfigs.add(pangleATInitConfig);
//        atInitConfigs.add(mintegralATInitConfig);
//        atInitConfigs.add(facebookATInitConfig);
//        atInitConfigs.add(vungleAtInitConfig);
//        atInitConfigs.add(adColonyATInitConfig);
//        atInitConfigs.add(myTargetATInitConfig);
//        val builder = ATNetworkConfig.Builder()
//        builder.withInitConfigList(atInitConfigs)
//        return builder.build()
//    }
}