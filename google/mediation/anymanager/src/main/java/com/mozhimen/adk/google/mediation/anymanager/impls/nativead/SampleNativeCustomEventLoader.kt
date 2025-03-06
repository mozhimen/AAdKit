package com.mozhimen.adk.google.mediation.anymanager.impls.nativead

import android.text.TextUtils
import android.util.Log
import com.google.android.gms.ads.mediation.MediationAdLoadCallback
import com.google.android.gms.ads.mediation.MediationNativeAdCallback
import com.google.android.gms.ads.mediation.MediationNativeAdConfiguration
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.mozhimen.adk.google.mediation.commons.ISampleNativeAdListener
import com.mozhimen.adk.google.mediation.cons.ESampleError
import com.mozhimen.adk.google.mediation.helpers.SampleCustomEventError
import com.mozhimen.adk.google.mediation.mos.SampleNativeAd

/**
 * @ClassName SampleNativeCustomEventLoader
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/** Native custom event loader for the SampleSDK.  */
class SampleNativeCustomEventLoader constructor(
    /** Configuration for requesting the native ad from the third party network.  */
    private val mediationNativeAdConfiguration: MediationNativeAdConfiguration,
    mediationAdLoadCallback: MediationAdLoadCallback<UnifiedNativeAdMapper, MediationNativeAdCallback>,
) : ISampleNativeAdListener {
    /** Callback that fires on loading success or failure.  */
    private val mediationAdLoadCallback: MediationAdLoadCallback<UnifiedNativeAdMapper, MediationNativeAdCallback> = mediationAdLoadCallback


    /**
     * Callback for native ad events. The usual link/click tracking handled through callback methods
     * are handled through the GMA SDK, described here:
     * https://developers.google.com/admob/android/custom-events/native#impression_and_click_events
     */
    @Suppress("unused")
    private var mediationNativeAdCallback: MediationNativeAdCallback? = null

    /** Loads the native ad from the third party ad network.  */
    fun loadAd() {
        // Create one of the Sample SDK's ad loaders to request ads.
        Log.i(TAG, "Begin loading native ad.")
        val loader: SampleNativeAdLoader =
            SampleNativeAdLoader(mediationNativeAdConfiguration.context)

        // All custom events have a server parameter named "parameter" that returns back the parameter
        // entered into the AdMob UI when defining the custom event.
        val serverParameter =
            mediationNativeAdConfiguration.serverParameters.getString("parameter")
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(SampleCustomEventError.createCustomEventNoAdIdError())
            return
        }
        Log.d(TAG, "Received server parameter.")

        loader.setAdUnit(serverParameter)

        // Create a native request to give to the SampleNativeAdLoader.
        val request: SampleNativeAdRequest = SampleNativeAdRequest()
        val options = mediationNativeAdConfiguration.nativeAdOptions
        if (options != null) {
            // If the NativeAdOptions' shouldReturnUrlsForImageAssets is true, the adapter should
            // send just the URLs for the images.
            request.shouldDownloadImages=(!options.shouldReturnUrlsForImageAssets())

            // If your network does not support any of the following options, please make sure
            // that it is documented in your adapter's documentation.
            request.setShouldDownloadMultipleImages(options.shouldRequestMultipleImages())
            when (options.mediaAspectRatio) {
                NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE -> request.setPreferredImageOrientation(SampleNativeAdRequest.IMAGE_ORIENTATION_LANDSCAPE)
                NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_PORTRAIT -> request.setPreferredImageOrientation(SampleNativeAdRequest.IMAGE_ORIENTATION_PORTRAIT)
                NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_SQUARE, NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_ANY, NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_UNKNOWN -> request.setPreferredImageOrientation(SampleNativeAdRequest.IMAGE_ORIENTATION_ANY)
                else -> request.setPreferredImageOrientation(SampleNativeAdRequest.IMAGE_ORIENTATION_ANY)
            }
        }

        loader.setNativeAdListener(this)

        // Begin a request.
        Log.i(TAG, "Start fetching native ad.")
        loader.fetchAd(request)
    }


    /** Called when a native ad is successfully fetched.  */
    override fun onNativeAdFetched(ad: SampleNativeAd) {
        // If the mediated network only ever returns URLs for images, this is an appropriate place
        // to automatically download the image files if the publisher has indicated via the
        // NativeAdOptions object that the custom event should do so.
        //
        // For example, if the publisher set the NativeAdOption's shouldReturnUrlsForImageAssets
        // property to false, and the mediated network returns images only as URLs rather than
        // downloading them itself, the forwarder should:
        //
        // 1. Initiate HTTP downloads of the image assets from the returned URLs using Volley or
        //    another, similar mechanism.
        // 2. Wait for all the requests to complete.
        // 3. Give the mediated network's native ad object and the image assets to your mapper class
        //    (each custom event defines its own mapper classes, so you can add a parameter for this
        //    to the constructor.
        // 4. Call the MediationNativeListener's onAdLoaded method and give it a reference to your
        //    custom event and the mapped native ad, as seen below.
        //
        // The important thing is to make sure that the publisher's wishes in regard to automatic
        // image downloading are respected, and that any additional downloads take place *before*
        // the mapped native ad object is returned to the Google Mobile Ads SDK via the
        // onAdLoaded method.
        Log.d(TAG, "Received the native ad.")
        val mapper: SampleNativeAdMapper = SampleNativeAdMapper(ad)
        mediationNativeAdCallback = mediationAdLoadCallback.onSuccess(mapper)
    }

    override fun onAdFetchFailed(error: ESampleError) {
        Log.e(TAG, "Failed to fetch the native ad.")
        mediationAdLoadCallback.onFailure(SampleCustomEventError.createSampleSdkError(error))
    }

    companion object {
        /** Tag used for log statements  */
        private const val TAG = "NativeCustomEvent"
    }
}
