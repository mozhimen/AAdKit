package com.mozhimen.adk.google.mediation.cons

/**
 * @ClassName ESampleErrorCode
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
enum class ESampleError {
    UNKNOWN,
    BAD_REQUEST,
    NETWORK_ERROR,
    NO_INVENTORY;
}

/**
 * Converts the SampleErrorCode to an integer in the range 0-99. This range is distinct from the
 * SampleCustomEventErrorCode's range which is 100-199.
 *
 * @param errorCode the error code returned by the sample SDK
 * @return an integer in the range 0-99
 */
fun ESampleError.toMediationErrorCode(): Int {
    when (this) {
        ESampleError.UNKNOWN -> return 0
        ESampleError.BAD_REQUEST -> return 1
        ESampleError.NO_INVENTORY -> return 2
        ESampleError.NETWORK_ERROR -> return 3
    }

    return 99
}
