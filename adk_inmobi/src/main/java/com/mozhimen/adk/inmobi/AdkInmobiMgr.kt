package com.mozhimen.adk.inmobi

import android.content.Context
import androidx.annotation.Size
import com.inmobi.sdk.InMobiSdk
import com.inmobi.sdk.SdkInitializationListener
import com.mozhimen.adk.inmobi.mos.ConsentObject
import com.mozhimen.adk.inmobi.utils.CacheUtil
import com.mozhimen.basick.elemk.commons.IAB_Listener
import com.mozhimen.basick.utilk.android.content.isMainProcess
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.basick.utilk.commons.IUtilK
import org.json.JSONObject

/**
 * @ClassName AdkInmobiMgr
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/6/5
 * @Version 1.0
 */
object AdkInmobiMgr : IUtilK {
    /**
     * gdpr_consent	字符串
     * 意见征求字符串是一串数字，用于标识广告技术供应商的同意状态。
     * 字符串必须遵循此处提到的 IAB 合同。
     * 键 gdpr_consent 可以通过字符串常量 IM_GDPR_CONSENT_IAB 访问。
     *
     * //////////////////////////////////////////////////////////////////////
     *
     * gdpr_consent_available	布尔
     * 真：发布者已同意收集和使用用户数据。
     * false： - 发布商未同意收集和使用用户数据。
     * 除“true”和“false”以外的任何值都是无效的，将被视为发布者未提供的值，即空值。
     * 密钥gdpr_consent_available可以通过字符串常量IM_GDPR_CONSENT_AVAILABLE访问。
     *
     * //////////////////////////////////////////////////////////////////////
     *
     * GDPR	字符串
     * 无论请求是否受 GDPR 法规的约束，与设定值的偏差（0 = 否，1 = 是）都表示未知实体。
     */
    @JvmStatic
    fun init(context: Context, @Size(min = 32L, max = 36L) accountId: String, consentObject: ConsentObject, listener: IAB_Listener<Boolean, Error?>? = null) {
        UtilKLogWrapper.d(TAG, "init: ")
        if (!context.isMainProcess()) return
        val jsonObject: JSONObject? = consentObject.toConsentJSONObject()
        init(context, accountId, jsonObject, listener)
    }

    @JvmStatic
    fun init(context: Context, @Size(min = 32L, max = 36L) accountId: String, consentObject: JSONObject? = null, listener: IAB_Listener<Boolean, Error?>? = null) {
        UtilKLogWrapper.d(TAG, "init: ")
        if (!context.isMainProcess()) return
        InMobiSdk.init(
            context, accountId, consentObject,
            object : SdkInitializationListener {
                override fun onInitializationComplete(error: java.lang.Error?) {
                    if (null != error) {
                        UtilKLogWrapper.e(TAG, "InMobi Init failed -" + error.message)
                        CacheUtil.is_init_success = false
                        listener?.invoke(false, error)
                    } else {
                        UtilKLogWrapper.d(TAG, "InMobi Init Successful")
                        CacheUtil.is_init_success = true
                        listener?.invoke(true, null)
                    }
                }
            }
        )
    }

    @JvmStatic
    fun updateGDPRConsent(consentObject: ConsentObject) {
        val jsonObject: JSONObject? = consentObject.toConsentJSONObject()
        if (jsonObject != null) {
            updateGDPRConsent(jsonObject)
        } else {
            UtilKLogWrapper.e(TAG, "updateGDPRConsent: consentObject is null")
        }
    }

    @JvmStatic
    fun updateGDPRConsent(consentObject: JSONObject) {
        InMobiSdk.updateGDPRConsent(consentObject);
    }

    @JvmStatic
    fun isInitSuccess(): Boolean =
        CacheUtil.is_init_success
}