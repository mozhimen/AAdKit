package com.mozhimen.adk.google.mediation.commons

import com.mozhimen.adk.google.mediation.cons.ESampleError

/**
 * @ClassName SampleAdListener
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * A sample ad listener to listen for ad events. These ad events more or less represent the events
 * that a typical ad network would provide.
 */
interface ISampleAdListener {
    /**
     * Called when an ad is successfully fetched.
     */
    fun onAdFetchSucceeded() {
        // Default is to do nothing.
    }

    /**
     * Called when an ad fetch fails.
     *
     * @param error The reason the fetch failed.
     */
    fun onAdFetchFailed(error: ESampleError) {
        // Default is to do nothing.
    }

    /**
     * Called when an ad goes full screen.
     */
    fun onAdFullScreen() {
        // Default is to do nothing.
    }

    /**
     * Called when an ad is closed.
     */
    fun onAdClosed() {
        // Default is to do nothing.
    }
}
