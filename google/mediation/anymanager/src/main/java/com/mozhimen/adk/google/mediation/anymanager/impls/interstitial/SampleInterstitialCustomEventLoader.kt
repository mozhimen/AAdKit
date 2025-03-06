package com.mozhimen.adk.google.mediation.anymanager.impls.interstitial

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.ads.mediation.MediationAdLoadCallback
import com.google.android.gms.ads.mediation.MediationInterstitialAd
import com.google.android.gms.ads.mediation.MediationInterstitialAdCallback
import com.google.android.gms.ads.mediation.MediationInterstitialAdConfiguration
import com.mozhimen.adk.google.mediation.anymanager.AnyManagerAdapter
import com.mozhimen.adk.google.mediation.commons.ISampleAdListener
import com.mozhimen.adk.google.mediation.commons.ISampleCustomEventLoader
import com.mozhimen.adk.google.mediation.cons.ESampleError
import com.mozhimen.adk.google.mediation.helpers.SampleCustomEventError

/**
 * @ClassName SampleInterstitialCustomEventLoader
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/** Interstitial custom event loader for the SampleSDK.  */
class SampleInterstitialCustomEventLoader(
    /** Configuration for requesting the interstitial ad from the third party network.  */
    private val mediationInterstitialAdConfiguration: MediationInterstitialAdConfiguration,
    /** Callback that fires on loading success or failure.  */
    private val mediationAdLoadCallback: MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback>,
) : ISampleAdListener, MediationInterstitialAd, ISampleCustomEventLoader {
    /** A sample third party SDK interstitial ad.  */
    private var sampleInterstitialAd: SampleInterstitial? = null


    /** Callback for interstitial ad events.  */
    private var interstitialAdCallback: MediationInterstitialAdCallback? = null

    /** Loads the interstitial ad from the third party ad network.  */
    override fun loadAd() {
        // All custom events have a server parameter named "parameter" that returns back the parameter
        // entered into the AdMob UI when defining the custom event.
        Log.i(TAG, "Begin loading interstitial ad.")
        val serverParameter =
            mediationInterstitialAdConfiguration.serverParameters.getString("parameter")
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(SampleCustomEventError.createCustomEventNoAdIdError())
            return
        }
        Log.d(TAG, "Received server parameter.")

        sampleInterstitialAd =
            SampleInterstitial(mediationInterstitialAdConfiguration.context)
        sampleInterstitialAd!!.setAdUnit(serverParameter)

        // Implement a SampleAdListener and forward callbacks to mediation.
        sampleInterstitialAd!!.setAdListener(this)

        // Make an ad request.
        Log.i(TAG, "start fetching interstitial ad.")
        sampleInterstitialAd!!.fetchAd(
            AnyManagerAdapter.createSampleRequest(mediationInterstitialAdConfiguration)
        )
    }

    override fun onAdFetchSucceeded() {
        Log.d(TAG, "Received the interstitial ad.")
        interstitialAdCallback = mediationAdLoadCallback.onSuccess(this)
    }

    override fun onAdFetchFailed(error: ESampleError) {
        Log.e(TAG, "Failed to fetch the interstitial ad.")
        mediationAdLoadCallback.onFailure(SampleCustomEventError.createSampleSdkError(error))
    }

    override fun onAdFullScreen() {
        Log.d(TAG, "The interstitial ad was shown fullscreen.")
        interstitialAdCallback!!.reportAdImpression()
        interstitialAdCallback!!.onAdOpened()
    }

    override fun onAdClosed() {
        Log.d(TAG, "The interstitial ad was closed.")
        interstitialAdCallback!!.onAdClosed()
    }

    override fun showAd(context: Context) {
        Log.d(TAG, "The interstitial ad was shown.")
        sampleInterstitialAd?.show()
    }

    companion object {
        /** Tag used for log statements  */
        private const val TAG = "InterstitialCustomEvent"
    }
}
