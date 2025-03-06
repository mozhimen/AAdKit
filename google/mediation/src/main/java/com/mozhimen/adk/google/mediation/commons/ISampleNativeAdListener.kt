package com.mozhimen.adk.google.mediation.commons

import android.view.View
import com.mozhimen.adk.google.mediation.cons.ESampleError
import com.mozhimen.adk.google.mediation.mos.SampleNativeAd

/**
 * @ClassName ISampleNativeAdListener
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * A sample ad listener to listen for native ad events. These ad events more or less represent the
 * events that a typical ad network would provide.
 */
interface ISampleNativeAdListener {
    /**
     * Called when a native ad is successfully fetched.
     */
    fun onNativeAdFetched(ad: SampleNativeAd) {
        // Default is to do nothing.
    }

    /**
     * Called when an ad fetch fails.
     *
     * @param code The reason the fetch failed.
     */
    fun onAdFetchFailed(error: ESampleError) {
        // Default is to do nothing.
    }
}