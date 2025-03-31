package com.mozhimen.adk.google.mediation.anymind.impls.rewarded

import android.app.Activity
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.Log
import com.mozhimen.adk.google.mediation.SampleAdRequest
import com.mozhimen.adk.google.mediation.commons.ISampleRewardedAdListener
import com.mozhimen.adk.google.mediation.cons.ESampleError
import java.util.Random

/**
 * @ClassName SampleRewardedAd
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * The [SampleRewardedAd] class is used to load and show rewarded ad for the Sample SDK.
 */
/**
 * Construct a rewarded ad.
 */
class AnyMindRewardedAd
constructor(
    /**
     * Ad Unit ID to initialize a Sample Rewarded Ad.
     */
    private val adUnitId: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()?:"")

    ///////////////////////////////////////////////////////////////////////

    /**
     * Returns if the rewarded ad is available to show.
     */
    /**
     * A flag that indicates whether a rewarded ad is ready to show.
     */
    var isAdAvailable: Boolean = false
        private set

    /**
     * A listener to forward any rewarded ad events.
     */
    private var listener: ISampleRewardedAdListener? = null

    /**
     * Gets the reward for this rewarded ad. Returns 0 until an ad is available.
     */
    /**
     * The reward amount associated with the ad.
     */
    var reward: Int = 0
        private set

    /**
     * Sets the rewarded ad listener to which the rewarded ad events will be forwarded.
     *
     * @param listener a `ISampleRewardedAdListener` to which to forward the rewarded video ad
     * events.
     */
    fun setListener(listener: ISampleRewardedAdListener?) {
        this.listener = listener
    }

    /**
     * Returns a rewarded ad listener to forward the rewarded events.
     *
     * @return a `ISampleRewardedAdListener` that is currently registered to the Sample SDK.
     */
    fun getListener(): ISampleRewardedAdListener? {
        return listener
    }

    /**
     * Loads a rewarded ad.
     */
    fun loadAd(request: SampleAdRequest?) {
        val random = Random()
        val nextInt = random.nextInt(100)
        var errorCode: ESampleError? = null
        if (nextInt < 80) {
            reward = 5
            isAdAvailable = true
            if (listener != null) {
                listener!!.onRewardedAdLoaded()
            }
        } else if (nextInt < 85) {
            errorCode = ESampleError.UNKNOWN
        } else if (nextInt < 90) {
            errorCode = ESampleError.BAD_REQUEST
        } else if (nextInt < 95) {
            errorCode = ESampleError.NETWORK_ERROR
        } else if (nextInt < 100) {
            errorCode = ESampleError.NO_INVENTORY
        }
        if (errorCode != null && listener != null && !isAdAvailable) {
            listener!!.onRewardedAdFailedToLoad(errorCode)
        }
    }

    /**
     * Shows a rewarded ad if one is available. The publisher should check if [.isAdAvailable]
     * is `true` before calling this method.
     */
    fun showAd(activity: Activity?) {
        if (!(isAdAvailable)) {
            Log.w(
                "SampleSDK",
                "No ads to show. Call SampleRewardedAd.isAdAvailable() before " + "calling showAd()."
            )
            return
        }

        if (activity == null) {
            Log.d("SampleSDK", "Current activity is null. Make sure to pass in a valid activity.")
            return
        }

        val intent = Intent(activity, AnyMindRewardedActivity::class.java)
        intent.putExtra(AnyMindRewardedActivity.KEY_REWARDED_AD_EXTRA, this)
        activity.startActivity(intent)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(adUnitId)
    }

    ////////////////////////////////////////////////////////////////////////////


    companion object CREATOR : Creator<AnyMindRewardedAd> {
        override fun createFromParcel(parcel: Parcel): AnyMindRewardedAd {
            return AnyMindRewardedAd(parcel)
        }

        override fun newArray(size: Int): Array<AnyMindRewardedAd?> {
            return arrayOfNulls(size)
        }
    }
}

