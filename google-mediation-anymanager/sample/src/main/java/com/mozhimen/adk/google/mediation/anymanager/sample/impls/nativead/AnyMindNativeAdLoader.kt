package com.mozhimen.adk.google.mediation.anymanager.sample.impls.nativead

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.mozhimen.adk.google.mediation.commons.ISampleNativeAdListener
import com.mozhimen.adk.google.mediation.cons.ESampleError
import com.mozhimen.adk.google.mediation.mos.SampleNativeAd
import java.util.Random
import com.mozhimen.adk.google.mediation.anymanager.sample.R

/**
 * @ClassName SampleNativeAdLoader
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * An example AdLoader that pretends to load native ads. It has methods that will be used by the
 * `SampleCustomEvent` to request native ads.
 */
/**
 * Create a new [SampleInterstitial].
 *
 * @param context An Android [Context].
 */
class AnyMindNativeAdLoader constructor(private val context: Context) {
    private var adUnit: String? = null
    private var listener: ISampleNativeAdListener? = null

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
     * @param listener The native ad listener.
     */
    fun setNativeAdListener(listener: ISampleNativeAdListener?) {
        this.listener = listener
    }

    /**
     * Fetch an ad. Instead of doing an actual ad fetch, we will randomly decide to succeed, or fail
     * with different error codes.
     *
     * @param request The ad request with targeting information.
     */
    fun fetchAd(request: AnyMindNativeAdRequest) {
        // Check for conditions that constitute a bad request.
        if ((listener == null) || (adUnit == null)) {
            listener!!.onAdFetchFailed(ESampleError.BAD_REQUEST)
            return
        }

        val random = Random()
        val nextInt = random.nextInt(100)
        if (listener != null) {
            if (nextInt < 92) {
                // Act as if the request was successful and create a sample native ad
                // of the request type filled with dummy data.
                if (random.nextBoolean()) {
                    listener!!.onNativeAdFetched(createSampleAppInstallAd(request))
                } else {
                    listener!!.onNativeAdFetched(createSampleContentAd(request))
                }
            } else if (nextInt < 94) {
                listener!!.onAdFetchFailed(ESampleError.UNKNOWN)
            } else if (nextInt < 96) {
                listener!!.onAdFetchFailed(ESampleError.BAD_REQUEST)
            } else if (nextInt < 98) {
                listener!!.onAdFetchFailed(ESampleError.NETWORK_ERROR)
            } else {
                listener!!.onAdFetchFailed(ESampleError.NO_INVENTORY)
            }
        }
    }

    private fun createSampleAppInstallAd(request: AnyMindNativeAdRequest): SampleNativeAd {
        val nativeAd: SampleNativeAd = SampleNativeAd()

        nativeAd.setHeadline("Sample App!")
        nativeAd.setBody("This app doesn't actually exist.")
        nativeAd.setCallToAction("Take Action!")
        nativeAd.setDegreeOfAwesomeness("Quite Awesome")
        nativeAd.setPrice(1.99)
        nativeAd.setStarRating(4.5)
        nativeAd.setStoreName("Sample Store")
        nativeAd.setImageUri(Uri.parse("http://www.example.com/"))
        nativeAd.setIconUri(Uri.parse("http://www.example.com/"))

        // We pretend 80% of network's inventory has video assets and 20% doesn't.
        if ((Random()).nextInt(100) < 80) {
            nativeAd.setMediaView(AnyMindMediaView(context))
        } else {
            nativeAd.setMediaView(null)
        }

        // There are other options offered in the SampleNativeAdRequest,
        // but for simplicity's sake, this is the only one we'll put to use.
        if (request.shouldDownloadImages) {
            nativeAd.setIcon(
                ResourcesCompat
                    .getDrawable(context.resources, R.drawable.sample_app_icon, null)
            )
            nativeAd.setImage(
                ResourcesCompat
                    .getDrawable(context.resources, R.drawable.sample_app_image, null)
            )
        }

        nativeAd.setInformationIcon(createInformationIconImageView())
        return nativeAd
    }

    private fun createSampleContentAd(request: AnyMindNativeAdRequest): SampleNativeAd {
        val nativeAd: SampleNativeAd = SampleNativeAd()

        nativeAd.setHeadline("Sample Content!")
        nativeAd.setBody(
            "This is a sample ad, so there's no real content. In the event of a real "
                    + "ad, though, some persuasive text would appear here."
        )
        nativeAd.setAdvertiser("The very best advertiser!")
        nativeAd.setCallToAction("Take Action!")
        nativeAd.setDegreeOfAwesomeness("Fairly Awesome")

        // We pretend 80% of network's inventory has video assets and 20% doesn't.
        if ((Random()).nextInt(100) < 80) {
            nativeAd.setMediaView(AnyMindMediaView(context))
        } else {
            nativeAd.setMediaView(null)
        }

        // There are other options offered in the SampleNativeAdRequest,
        // but for simplicity's sake, this is the only one we'll put to use.
        if (request.shouldDownloadImages) {
            nativeAd.setIcon(
                ResourcesCompat
                    .getDrawable(context.resources, R.drawable.sample_content_logo, null)
            )
            nativeAd.setImage(
                ResourcesCompat
                    .getDrawable(context.resources, R.drawable.sample_content_ad_image, null)
            )
        }

        nativeAd.setInformationIcon(createInformationIconImageView())
        return nativeAd
    }

    /**
     * Creates and returns an information icon image view.
     *
     * @return information icon image view.
     */
    private fun createInformationIconImageView(): ImageView {
        val informationIconImageView = ImageView(context)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        informationIconImageView.layoutParams = params
        informationIconImageView.setImageResource(R.drawable.ic_info_outline_black_24px)

        informationIconImageView.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Sample SDK")
                .setMessage("This is a sample ad from the Sample SDK.")
                .setNeutralButton(
                    android.R.string.ok
                ) { dialog, which -> dialog.dismiss() }
                .show()
        }

        return informationIconImageView
    }
}
