package com.mozhimen.adk.google.mediation.anymanager.impls.banner

import android.content.res.Resources
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.android.gms.ads.mediation.MediationAdLoadCallback
import com.google.android.gms.ads.mediation.MediationBannerAd
import com.google.android.gms.ads.mediation.MediationBannerAdCallback
import com.google.android.gms.ads.mediation.MediationBannerAdConfiguration
import com.mozhimen.adk.google.mediation.commons.ISampleAdListener
import com.mozhimen.adk.google.mediation.SampleAdRequest
import com.mozhimen.adk.google.mediation.anymanager.AnyManagerAdapter
import com.mozhimen.adk.google.mediation.commons.ISampleCustomEventLoader
import com.mozhimen.adk.google.mediation.cons.ESampleError
import com.mozhimen.adk.google.mediation.helpers.SampleCustomEventError
import com.mozhimen.adk.google.mediation.mos.SampleAdSize

/**
 * @ClassName SampleBannerCustomEventLoader
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/** Banner custom event loader for the SampleSDK.  */
class SampleBannerCustomEventLoader constructor(
    /** Configuration for requesting the banner ad from the third party network.  */
    private val mediationBannerAdConfiguration: MediationBannerAdConfiguration,
    /** Callback that fires on loading success or failure.  */
    private val mediationAdLoadCallback: MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback>,
) : ISampleAdListener, MediationBannerAd, ISampleCustomEventLoader {
    /** View to contain the sample banner ad.  */
    private var sampleAdView: SampleAdView? = null


    /** Callback for banner ad events.  */
    private var bannerAdCallback: MediationBannerAdCallback? = null

    /** Loads a banner ad from the third party ad network.  */
    override fun loadAd() {
        // All custom events have a server parameter named "parameter" that returns back the parameter
        // entered into the AdMob UI when defining the custom event.
        Log.i(TAG, "Begin loading banner ad.")
        val serverParameter =
            mediationBannerAdConfiguration.serverParameters.getString("parameter")
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(SampleCustomEventError.createCustomEventNoAdIdError())
            return
        }
        Log.d(TAG, "Received server parameter.")

        val context = mediationBannerAdConfiguration.context
        sampleAdView = SampleAdView(context)

        // Assumes that the serverParameter is the AdUnit for the Sample Network.
        sampleAdView!!.setAdUnit(serverParameter)
        val size = mediationBannerAdConfiguration.adSize

        // Internally, smart banners use constants to represent their ad size, which means a call to
        // AdSize.getHeight could return a negative value. You can accommodate this by using
        // AdSize.getHeightInPixels and AdSize.getWidthInPixels instead, and then adjusting to match
        // the device's display metrics.
        val widthInPixels = size.getWidthInPixels(context)
        val heightInPixels = size.getHeightInPixels(context)
        val displayMetrics = Resources.getSystem().displayMetrics
        val widthInDp = Math.round(widthInPixels / displayMetrics.density)
        val heightInDp = Math.round(heightInPixels / displayMetrics.density)

        sampleAdView!!.setSize(SampleAdSize(widthInDp, heightInDp))
        sampleAdView!!.setAdListener(this)

        val request: SampleAdRequest = AnyManagerAdapter.createSampleRequest(mediationBannerAdConfiguration)
        Log.i(TAG, "Start fetching banner ad.")
        sampleAdView!!.fetchAd(request)
    }

    override fun onAdFetchSucceeded() {
        Log.d(TAG, "Received the banner ad.")
        bannerAdCallback = mediationAdLoadCallback.onSuccess(this)
        bannerAdCallback!!.reportAdImpression()
    }

    override fun onAdFetchFailed(error: ESampleError) {
        Log.e(TAG, "Failed to fetch the banner ad.")
        mediationAdLoadCallback.onFailure(SampleCustomEventError.createSampleSdkError(error))
    }

    override fun getView(): View {
        return sampleAdView!!
    }

    override fun onAdFullScreen() {
        Log.d(TAG, "The banner ad was clicked.")
        bannerAdCallback!!.onAdOpened()
        bannerAdCallback!!.onAdLeftApplication()
        bannerAdCallback!!.reportAdClicked()
    }

    override fun onAdClosed() {
        Log.d(TAG, "The banner ad was closed.")
        bannerAdCallback!!.onAdClosed()
    }

    companion object {
        /** Tag used for log statements  */
        private const val TAG = "BannerCustomEvent"
    }
}
