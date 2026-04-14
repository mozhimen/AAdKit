package com.mozhimen.adk.google.mediation.commons

import com.mozhimen.adk.google.mediation.cons.ESampleError

/**
 * @ClassName ISampleRewardedAdListener
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * The [ISampleRewardedAdListener] class listens for rewarded video ad events. These ad events
 * more or less represent the events that a typical ad network would provide.
 */
interface ISampleRewardedAdListener {
    /**
     * Called when the rewarded ad is loaded.
     */
    fun onRewardedAdLoaded() {
        // Default is do nothing.
    }

    /**
     * Called when the rewarded ad fails to load.
     *
     * @param error reason for failure.
     */
    fun onRewardedAdFailedToLoad(error: ESampleError) {
        // Default is do nothing.
    }

    /**
     * Called when the user is eligible for a reward.
     *
     * @param rewardType the reward type.
     * @param amount the reward amount to be rewarded.
     */
    fun onAdRewarded(rewardType: String, amount: Int) {
        // Default is do nothing.
    }

    /**
     * Called when the ad is clicked.
     */
    fun onAdClicked() {
        // Default is do nothing.
    }

    /**
     * Called when an ad goes full screen.
     */
    fun onAdFullScreen() {
        // Default is do nothing.
    }

    /**
     * Called when an ad is closed.
     */
    fun onAdClosed() {
        // Default is do nothing.
    }

    /**
     * Called when an ad is finished.
     */
    fun onAdCompleted() {
        // Default is do nothing.
    }
}
