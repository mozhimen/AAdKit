package com.mozhimen.adk.google.mediation.anymanager.sample.impls.rewarded

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.ads.mediation.MediationAdLoadCallback
import com.google.android.gms.ads.mediation.MediationRewardedAd
import com.google.android.gms.ads.mediation.MediationRewardedAdCallback
import com.google.android.gms.ads.mediation.MediationRewardedAdConfiguration
import com.google.android.gms.ads.rewarded.RewardItem
import com.mozhimen.adk.google.mediation.SampleAdRequest
import com.mozhimen.adk.google.mediation.commons.ISampleCustomEventLoader
import com.mozhimen.adk.google.mediation.commons.ISampleRewardedAdListener
import com.mozhimen.adk.google.mediation.cons.ESampleError
import com.mozhimen.adk.google.mediation.helpers.SampleCustomEventError

/**
 * @ClassName SampleRewardedCustomEventLoader
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/** Rewarded custom event loader for the SampleSDK.  */
class AnyMindRewardedCustomEventLoader constructor(
    /** Configuration for requesting the rewarded ad from the third party network.  */
    private val mediationRewardedAdConfiguration: MediationRewardedAdConfiguration,
    /**
     * A [MediationAdLoadCallback] that handles any callback when a Sample rewarded ad finishes
     * loading.
     */
    private val mediationAdLoadCallback: MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback>,
) : ISampleRewardedAdListener, MediationRewardedAd, ISampleCustomEventLoader {
    /**
     * Represents a [AnyMindRewardedAd].
     */
    private var sampleRewardedAd: AnyMindRewardedAd? = null


    /**
     * Used to forward rewarded video ad events to the Google Mobile Ads SDK.
     */
    private var rewardedAdCallback: MediationRewardedAdCallback? = null

    /** Loads the rewarded ad from the third party ad network.  */
    override fun loadAd() {
        // All custom events have a server parameter named "parameter" that returns back the parameter
        // entered into the AdMob UI when defining the custom event.
        Log.i(TAG, "Begin loading rewarded ad.")
        val serverParameter =
            mediationRewardedAdConfiguration.serverParameters.getString("parameter")
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(SampleCustomEventError.createCustomEventNoAdIdError())
            return
        }
        Log.d(TAG, "Received server parameter.")

        val request: SampleAdRequest = SampleAdRequest()
        sampleRewardedAd = AnyMindRewardedAd(serverParameter!!)
        sampleRewardedAd!!.setListener(this)
        Log.i(TAG, "Start fetching rewarded ad.")
        sampleRewardedAd!!.loadAd(request)
    }

    override fun onRewardedAdLoaded() {
        Log.d(TAG, "Received the rewarded ad.")
        rewardedAdCallback = mediationAdLoadCallback.onSuccess(this)
    }

    override fun onRewardedAdFailedToLoad(error: ESampleError) {
        Log.e(TAG, "Failed to fetch the rewarded ad.")
        mediationAdLoadCallback.onFailure(SampleCustomEventError.createSampleSdkError(error))
    }

    override fun showAd(context: Context) {
        if (context !is Activity) {
            rewardedAdCallback?.onAdFailedToShow(
                SampleCustomEventError.createCustomEventNoActivityContextError()
            )
            Log.d(TAG, "The rewarded ad failed to show.")
            return
        }

        if (sampleRewardedAd?.isAdAvailable != true) {
            rewardedAdCallback?.onAdFailedToShow(SampleCustomEventError.createCustomEventAdNotAvailableError())
            return
        }
        sampleRewardedAd?.showAd(context)
        Log.d(TAG, "The rewarded ad was shown.")
    }

    override fun onAdRewarded(rewardType: String, amount: Int) {
        val rewardItem: RewardItem =
            object : RewardItem {
                override fun getType(): String {
                    return rewardType
                }

                override fun getAmount(): Int {
                    return amount
                }
            }
        Log.d(TAG, "The user earned a reward.")
        rewardedAdCallback!!.onUserEarnedReward(rewardItem)
    }

    override fun onAdClicked() {
        Log.d(TAG, "The rewarded ad was clicked.")
        rewardedAdCallback!!.reportAdClicked()
    }

    override fun onAdFullScreen() {
        Log.d(TAG, "The rewarded ad was shown fullscreen.")
        rewardedAdCallback!!.onAdOpened()
        rewardedAdCallback!!.onVideoStart()
        rewardedAdCallback!!.reportAdImpression()
    }

    override fun onAdClosed() {
        Log.d(TAG, "The rewarded ad was closed.")
        rewardedAdCallback!!.onAdClosed()
    }

    override fun onAdCompleted() {
        Log.d(TAG, "The rewarded ad video was completed.")
        rewardedAdCallback!!.onVideoComplete()
    }

    companion object {
        /** Tag used for log statements  */
        private const val TAG = "RewardedCustomEvent"
    }
}
