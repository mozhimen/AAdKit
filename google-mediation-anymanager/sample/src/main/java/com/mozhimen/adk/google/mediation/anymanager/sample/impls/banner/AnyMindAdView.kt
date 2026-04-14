package com.mozhimen.adk.google.mediation.anymanager.sample.impls.banner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.mozhimen.adk.google.mediation.SampleAdRequest
import com.mozhimen.adk.google.mediation.commons.ISampleAdListener
import com.mozhimen.adk.google.mediation.cons.ESampleError
import com.mozhimen.adk.google.mediation.mos.SampleAdSize
import java.util.Random

/**
 * @ClassName SampleAdView
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * An ad view for the sample ad network. This is an example of an ad view that most ad network SDKs
 * have.
 */
/**
 * Create a new [AnyMindAdView].
 *
 * @param context An Android [Context].
 */
@SuppressLint("SetTextI18n")
class AnyMindAdView constructor(context: Context) : AppCompatTextView(context) {
    private var adSize: SampleAdSize? = null
    private var adUnit: String? = null
    private var listener: ISampleAdListener? = null

    /**
     * Sets the size of the banner.
     *
     * @param size The banner size.
     */
    fun setSize(size: SampleAdSize?) {
        this.adSize = size
    }

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

        // If the publisher didn't set a size or ad unit, return a bad request.
        if (adSize == null || adUnit == null) {
            listener!!.onAdFetchFailed(ESampleError.BAD_REQUEST)
        }

        // Randomly decide whether to succeed or fail.
        val random = Random()
        val nextInt = random.nextInt(100)
        if (listener != null) {
            if (nextInt < 85) {
                this.setText("Sample Text Ad")
                this.setOnClickListener(View.OnClickListener { // Notify the developer that a full screen view will be presented.
                    listener?.onAdFullScreen()
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
                    this@AnyMindAdView.getContext().startActivity(intent)
                })
                listener?.onAdFetchSucceeded()
            } else if (nextInt < 90) {
                listener?.onAdFetchFailed(ESampleError.UNKNOWN)
            } else if (nextInt < 95) {
                listener?.onAdFetchFailed(ESampleError.NETWORK_ERROR)
            } else if (nextInt < 100) {
                listener?.onAdFetchFailed(ESampleError.NO_INVENTORY)
            }
        }
    }

    /**
     * Destroy the banner.
     */
    fun destroy() {
        listener = null
    }
}
