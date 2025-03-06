package com.mozhimen.adk.google.mediation.anymanager.impls.nativead

import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.formats.NativeAd
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper
import com.mozhimen.adk.google.mediation.mos.SampleNativeAd
import java.text.NumberFormat

/**
 * @ClassName SampleNativeAdMapper
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * A [NativeAdMapper] extension to map [SampleNativeAd] instances to the Mobile
 * Ads SDK's [com.google.android.gms.ads.nativead.NativeAd] interface.
 */
class SampleNativeAdMapper(ad: SampleNativeAd) : UnifiedNativeAdMapper() {
    companion object {
        /**
         * The pixel-to-dpi scale for images downloaded from the sample SDK's URL values. Scale value is
         * set in [SampleNativeMappedImage].
         */
        const val SAMPLE_SDK_IMAGE_SCALE: Double = 1.0

        /**
         * Example of an extra field that publishers can use for a Native ad. In this example, the String
         * is added to a [Bundle] in [SampleNativeAdMapper].
         */
        const val DEGREE_OF_AWESOMENESS: String = "DegreeOfAwesomeness"
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    private val sampleAd: SampleNativeAd = ad

    ////////////////////////////////////////////////////////////////////////////////////////
    init {
        setHeadline(sampleAd.getHeadline())
        setBody(sampleAd.getBody())
        setCallToAction(sampleAd.getCallToAction())
        setStarRating(sampleAd.getStarRating())
        setStore(sampleAd.getStoreName())
        if (ad.getIcon() != null && ad.getIconUri() != null) {
            setIcon(SampleNativeMappedImage(ad.getIcon()!!, ad.getIconUri()!!, SAMPLE_SDK_IMAGE_SCALE))
        }
        setAdvertiser(ad.getAdvertiser())

        if (ad.getImage() != null && ad.getIconUri() != null) {
            val imagesList: MutableList<NativeAd.Image> = ArrayList()
            imagesList.add(SampleNativeMappedImage(ad.getImage()!!, ad.getImageUri()!!, SAMPLE_SDK_IMAGE_SCALE))
            setImages(imagesList)
        }

        if (sampleAd.getPrice() != null) {
            val formatter = NumberFormat.getCurrencyInstance()
            val priceString: String = formatter.format(sampleAd.getPrice())
            setPrice(priceString)
        }

        val extras = Bundle()
        extras.putString(DEGREE_OF_AWESOMENESS, ad.getDegreeOfAwesomeness())
        this.setExtras(extras)

        setOverrideClickHandling(false)
        setOverrideImpressionRecording(false)

        if (sampleAd.getInformationIcon() != null) {
            setAdChoicesContent(sampleAd.getInformationIcon()!!)
        }
    }

    override fun recordImpression() {
        sampleAd.recordImpression()
    }

    override fun handleClick(view: View) {
        sampleAd.handleClick(view)
    }

    // The Sample SDK doesn't do its own impression/click tracking, instead relies on its
    // publishers calling the recordImpression and handleClick methods on its native ad object. So
    // there's no need to pass it a reference to the View being used to display the native ad. If
    // your mediated network does need a reference to the view, the following method can be used
    // to provide one.
    override fun trackViews(
        containerView: View,
        clickableAssetViews: Map<String?, View?>,
        nonClickableAssetViews: Map<String?, View?>,
    ) {
        super.trackViews(containerView, clickableAssetViews, nonClickableAssetViews)
        // If your ad network SDK does its own impression tracking, here is where you can track the
        // top level native ad view and its individual asset views.
    }

    override fun untrackView(view: View) {
        super.untrackView(view)
        // Here you would remove any trackers from the View added in trackView.
    }
}
