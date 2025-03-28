package com.mozhimen.adk.google.mediation.anymind.impls.interstitial

import android.R
import android.app.AlertDialog
import android.content.Context
import com.mozhimen.adk.google.mediation.SampleAdRequest
import com.mozhimen.adk.google.mediation.commons.ISampleAdListener
import com.mozhimen.adk.google.mediation.cons.ESampleError
import java.util.Random

/**
 * @ClassName SampleInterstitial
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * A sample interstitial ad. This is an example of an interstitial class that most ad networks SDKs
 * have.
 */
class AnyMindInterstitial
/**
 * Create a new [AnyMindInterstitial].
 *
 * @param context An Android [Context].
 */(private val context: Context) {
    private var adUnit: String? = null
    private var listener: ISampleAdListener? = null

    /**
     * Sets the sample ad unit.
     *
     * @param sampleAdUnit The sample ad unit.
     */
    fun setAdUnit(sampleAdUnit: String?) {
        this.adUnit = sampleAdUnit
    }

    /**
     * Sets a [SampleAdListener] to listen for ad events.
     *
     * @param listener The ad listener.
     */
    fun setAdListener(listener: ISampleAdListener?) {
        this.listener = listener
    }

    /**
     * Fetch an ad. Instead of doing an actual ad fetch, we will randomly decide to succeed, or fail
     * with different error codes.
     *
     * @param request The ad request with targeting information.
     */
    fun fetchAd(request: SampleAdRequest?) {
        if (listener == null) {
            return
        }

        // If the publisher didn't set an ad unit, return a bad request.
        if (adUnit == null) {
            listener!!.onAdFetchFailed(ESampleError.BAD_REQUEST)
        }

        val random = Random()
        val nextInt = random.nextInt(100)
        if (listener != null) {
            if (nextInt < 80) {
                listener!!.onAdFetchSucceeded()
            } else if (nextInt < 85) {
                listener!!.onAdFetchFailed(ESampleError.UNKNOWN)
            } else if (nextInt < 90) {
                listener!!.onAdFetchFailed(ESampleError.BAD_REQUEST)
            } else if (nextInt < 95) {
                listener!!.onAdFetchFailed(ESampleError.NETWORK_ERROR)
            } else if (nextInt < 100) {
                listener!!.onAdFetchFailed(ESampleError.NO_INVENTORY)
            }
        }
    }

    /**
     * Shows the interstitial.
     */
    fun show() {
        // Notify the developer that a full screen view will be presented.
        listener?.onAdFullScreen()
        AlertDialog.Builder(context)
            .setTitle("Sample Interstitial")
            .setMessage("You are viewing a sample interstitial ad.")
            .setNeutralButton(R.string.ok) { dialog, which -> // Notify the developer that the interstitial was closed.
                listener?.onAdClosed()
            }
            .show()
    }

    /**
     * Destroy the interstitial.
     */
    fun destroy() {
        listener = null
    }
}

