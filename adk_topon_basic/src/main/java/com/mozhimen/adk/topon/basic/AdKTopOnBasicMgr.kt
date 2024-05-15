package com.mozhimen.adk.topon.basic

import android.app.Activity
import android.content.Context
import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.anythink.core.api.ATDebuggerConfig
import com.anythink.core.api.ATGDPRAuthCallback
import com.anythink.core.api.ATGDPRConsentDismissListener
import com.anythink.core.api.ATSDK
import com.anythink.core.api.NetTrafficeCallback
import com.mozhimen.abilityk.google.android.ump.optins.OMetaData_GMS_ADS_APPLICATION_ID
import com.mozhimen.basick.elemk.commons.I_Listener
import com.mozhimen.basick.lintk.optins.OApiInit_InApplication
import com.mozhimen.basick.utilk.android.app.UtilKRunningAppProcessInfo
import com.mozhimen.basick.utilk.android.content.UtilKContextWrapper
import com.mozhimen.basick.utilk.commons.IUtilK
import com.mozhimen.webk.basic.WebKMgr
import java.util.concurrent.atomic.AtomicBoolean


/**
 * @ClassName AdKTopOnBasic
 * @Description 双端都未上架的可以接：mtg, unity, topon adx
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/21
 * @Version 1.0
 */

object AdKTopOnBasicMgr : IUtilK {
    private val _isInit = AtomicBoolean(false)
    private val _isNeedApplyGDPR = AtomicBoolean(false)

    ////////////////////////////////////////////////////////////////////////////////

    @JvmStatic
    @OApiInit_InApplication
    @OMetaData_GMS_ADS_APPLICATION_ID
    fun init_ofGDPR_ofUmp(activity: Activity, topOnAppId: String, topOnAppKey: String, isDebug: Boolean = true, onInitSuccess: I_Listener? = null) {
        if (_isNeedApplyGDPR.get() && ATSDK.getGDPRDataLevel(activity) == ATSDK.UNKNOWN) {
            UtilKLogWrapper.d(TAG, "init_ofGDPR_ofUmp: showGDPRConsentDialog")
            ATSDK.showGDPRConsentDialog(activity, object : ATGDPRConsentDismissListener {
                override fun onDismiss(p0: ATGDPRConsentDismissListener.ConsentDismissInfo?) {
                    init(activity, topOnAppId, topOnAppKey, isDebug, onInitSuccess)
                }
            })
        } else if (!_isInit.get()) {
            UtilKLogWrapper.d(TAG, "init_ofGDPR_ofUmp: !_isInit.get()")
            init(activity, topOnAppId, topOnAppKey, isDebug, onInitSuccess)
        } else {
            UtilKLogWrapper.d(TAG, "init_ofGDPR_ofUmp: ")
            onInitSuccess?.invoke()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////

    @JvmStatic
    @OApiInit_InApplication
    fun init_ofGDPR(context: Context, topOnAppId: String, topOnAppKey: String, isDebug: Boolean = true, onInitSuccess: I_Listener? = null) {
        ATSDK.checkIsEuTraffic(context, object : NetTrafficeCallback {
            override fun onResultCallback(isEU: Boolean) {
                if (isEU && ATSDK.getGDPRDataLevel(context) == ATSDK.UNKNOWN) {
//                    ATSDK.showGdprAuth(this@DemoApplicaion)
                    _isNeedApplyGDPR.compareAndSet(false, true)
                    return
                } else {
                    init(context, topOnAppId, topOnAppKey, isDebug, onInitSuccess)
                }
            }

            override fun onErrorCallback(errorMsg: String) {
                UtilKLogWrapper.i(TAG, "init_ofGDPR onErrorCallback:$errorMsg")
                init(context, topOnAppId, topOnAppKey, isDebug, onInitSuccess)
            }
        })
    }

    ////////////////////////////////////////////////////////////////////////////////

    @JvmStatic
    @OApiInit_InApplication
    fun init(context: Context, topOnAppId: String, topOnAppKey: String, isDebug: Boolean = true, onInitSuccess: I_Listener? = null) {
        if (topOnAppId.isEmpty() || topOnAppKey.isEmpty()) {
            UtilKLogWrapper.e(TAG, "init: fail")
            return
        }
        UtilKLogWrapper.w(TAG, "init: start")

        if (_isInit.compareAndSet(false, true)) {
            UtilKLogWrapper.d(TAG, "init: topOnAppId $topOnAppId topOnAppKey $topOnAppKey")

            WebKMgr.init(context)

            if (UtilKContextWrapper.isMainProcess(context)) {
                if (isDebug) {
                    ATSDK.setNetworkLogDebug(true)
                    ATSDK.integrationChecking(context)

                    /**
                     * 上线前需要移除此测试代码
                     * deviceId可以通过调用ATSDK.showGDPRConsentDialog后在logcat中过滤指定关键字“addTestDeviceHashedId”获取
                     *
                     * 需要先调用ATSDK.setNetworkLogDebug(true);//应用上线前须关闭此Api，再调用ATSDK.showGDPRConsentDialog才会打印此日志
                     */
                    ATSDK.setDebuggerConfig(context, "", ATDebuggerConfig.Builder().setUMPTestDeviceId("deviceid").build())
                }

                /*//        国内隐私政策
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
                //        );*/
//        ATSDK.setPersonalizedAdStatus(ATAdConst.PRIVACY.PERSIONALIZED_ALLOW_STATUS)
//        ATSDK.setUseHTTP(true);

                ATSDK.init(context, topOnAppId, topOnAppKey);//初始化SDK
//        or
//        ATNetworkConfig atNetworkConfig = getAtNetworkConfig();
//        ATSDK.init(this, appid, appKey, atNetworkConfig);

                onInitSuccess?.invoke()
            }
        }
    }

    @JvmStatic
    @OApiInit_InApplication
    @Deprecated("use init_ofGDPR_ofUmp")
    fun init_ofGDPR(activity: Activity, topOnAppId: String, topOnAppKey: String, isDebug: Boolean = true, onInitSuccess: I_Listener? = null) {
        if (_isNeedApplyGDPR.get() && ATSDK.getGDPRDataLevel(activity) == ATSDK.UNKNOWN) {
            ATSDK.showGdprAuth(activity, object : ATGDPRAuthCallback {
                override fun onAuthResult(p0: Int) {
                    ATSDK.setGDPRUploadDataLevel(activity, p0)
                    init(activity, topOnAppId, topOnAppKey, isDebug, onInitSuccess)
                }

                override fun onPageLoadFail() {
                    init(activity, topOnAppId, topOnAppKey, isDebug, onInitSuccess)
                }
            })
        } else
            init(activity, topOnAppId, topOnAppKey, isDebug, onInitSuccess)
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