package com.mozhimen.adk.inmobi.mos

import com.inmobi.sdk.InMobiSdk
import org.json.JSONException
import org.json.JSONObject

/**
 * @ClassName ConsentObject
 * @Description
 *
 * 什么是 consentObject？
 * consentObject 是发布者向 SDK 提供的所有类型的同意的 JSONObject 表示形式。如果您希望通过来自欧洲经济区的流量获利，则密钥是强制性的;InMobi依靠发布者获得用户同意以遵守法规。您可以在此处进一步阅读有关 GDPR 法规的信息。
 *
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/6/5
 * @Version 1.0
 */
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
data class ConsentObject(
    val gdpr_consent_available: Boolean,
    val gdpr_consent: String? = null,
    val gdpr: Boolean? = null
) {
    fun toConsentJSONObject(): JSONObject? {
        val jsonObject: JSONObject?
        try {
            jsonObject = JSONObject()
            // Provide correct consent value to sdk which is obtained by User
            jsonObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, gdpr_consent_available/*true*/)
            gdpr_consent?.let {
                // Provide user consent in IAB format
                jsonObject.put(InMobiSdk.IM_GDPR_CONSENT_IAB, gdpr_consent/*"<< consent in IAB format >>"*/)
            }
            gdpr?.let {
                // Provide 0 if GDPR is not applicable and 1 if applicable
                jsonObject.put("gdpr", if (gdpr) "0" else "1"/*"0"*/)
            }
            //
            return jsonObject
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }
}
