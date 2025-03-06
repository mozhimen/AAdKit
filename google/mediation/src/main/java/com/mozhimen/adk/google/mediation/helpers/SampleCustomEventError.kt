package com.mozhimen.adk.google.mediation.helpers

import com.google.android.gms.ads.AdError
import com.mozhimen.adk.google.mediation.annors.ASampleCustomEventErrorCode
import com.mozhimen.adk.google.mediation.cons.ESampleError
import com.mozhimen.adk.google.mediation.cons.toMediationErrorCode

/**
 * @ClassName SampleCustomEventError
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/** Convenience factory class to create AdError objects for custom events.  */
object SampleCustomEventError {
    const val SAMPLE_SDK_DOMAIN: String = "com.google.ads.mediation.sample.sdk"
    const val CUSTOM_EVENT_ERROR_DOMAIN: String = "com.google.ads.mediation.sample.customevent"

    fun createCustomEventNoAdIdError(): AdError =
        AdError(
            ASampleCustomEventErrorCode.ERROR_NO_AD_UNIT_ID,
            "Ad unit id is empty",
            CUSTOM_EVENT_ERROR_DOMAIN
        )

    fun createCustomEventAdNotAvailableError(): AdError =
        AdError(
            ASampleCustomEventErrorCode.ERROR_AD_NOT_AVAILABLE,
            "No ads to show",
            CUSTOM_EVENT_ERROR_DOMAIN
        )

    fun createCustomEventNoActivityContextError(): AdError =
        AdError(
            ASampleCustomEventErrorCode.ERROR_NO_ACTIVITY_CONTEXT,
            "An activity context is required to show the sample ad",
            CUSTOM_EVENT_ERROR_DOMAIN
        )

    /**
     * Creates a custom event `AdError`. This error wraps the underlying error thrown by the
     * sample SDK.
     * @param errorCode A `SampleErrorCode` to be reported.
     */
    fun createSampleSdkError(errorCode: ESampleError): AdError =
        AdError(
            errorCode.toMediationErrorCode(),
            errorCode.toString(),
            SAMPLE_SDK_DOMAIN
        )
}
