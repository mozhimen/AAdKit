package com.mozhimen.adk.google.mediation.annors

import androidx.annotation.IntDef

/**
 * @ClassName ASampleCustomEventErrorCode
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
@IntDef(
    value = [
        ASampleCustomEventErrorCode.ERROR_NO_AD_UNIT_ID,
        ASampleCustomEventErrorCode.ERROR_AD_NOT_AVAILABLE,
        ASampleCustomEventErrorCode.ERROR_NO_ACTIVITY_CONTEXT]
)
annotation class ASampleCustomEventErrorCode {
    companion object {
        /** Error raised when the custom event adapter cannot obtain the ad unit id.  */
        const val ERROR_NO_AD_UNIT_ID: Int = 101

        /**
         * Error raised when the custom event adapter does not have an ad available when trying to show
         * the ad.
         */
        const val ERROR_AD_NOT_AVAILABLE: Int = 102

        /** Error raised when the custom event adapter cannot obtain the activity context.  */
        const val ERROR_NO_ACTIVITY_CONTEXT: Int = 103

    }
}
