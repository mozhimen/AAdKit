package com.mozhimen.adk.google.mediation.anymanager.impls.nativead

import com.mozhimen.adk.google.mediation.SampleAdRequest

/**
 * @ClassName SampleNativeAdRequest
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * An example request class for native ads that can be used with [SampleNativeAdLoader].
 */
class SampleNativeAdRequest : SampleAdRequest() {
    var shouldDownloadImages: Boolean = true

    // For the sake of simplicity, the following two values are ignored by the Sample SDK.
    // They're included so that the custom event classes can demonstrate how to take a request
    // from the Google Mobile Ads SDK and translate it into one for the Sample SDK.
    fun setShouldDownloadMultipleImages(shouldDownloadMultipleImages: Boolean) {
    }

    fun setPreferredImageOrientation(preferredImageOrientation: Int) {
    }

    companion object {
        const val IMAGE_ORIENTATION_ANY: Int = 0
        const val IMAGE_ORIENTATION_PORTRAIT: Int = 1
        const val IMAGE_ORIENTATION_LANDSCAPE: Int = 2
    }
}
