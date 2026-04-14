package com.mozhimen.adk.google.mediation.mos

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.mozhimen.adk.google.mediation.commons.ISampleMediaView

/**
 * @ClassName SampleNativeAd
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
class SampleNativeAd {
    private var headline: String=""
    private var image: Drawable? = null
    private var imageUri: Uri? = null
    private var body: String=""
    private var icon: Drawable? = null
    private var iconUri: Uri? = null
    private var callToAction: String=""
    private var advertiser: String=""
    private var starRating = 0.0
    private var storeName: String=""
    private var price: Double? = null
    private var degreeOfAwesomeness: String=""
    private var informationIcon: ImageView? = null
    private var mediaView: ISampleMediaView? = null

    fun getHeadline():String {
        return headline
    }

    fun setHeadline(headline:String) {
        this.headline = headline
    }

    fun getImage(): Drawable? {
        return image
    }

    fun setImage(image: Drawable?) {
        this.image = image
    }

    fun getBody():String {
        return body
    }

    fun setBody(body:String) {
        this.body = body
    }

    fun getIcon(): Drawable? {
        return icon
    }

    fun setIcon(mAppIcon: Drawable?) {
        this.icon = mAppIcon
    }

    fun getCallToAction():String {
        return callToAction
    }

    fun setCallToAction(mCallToAction:String) {
        this.callToAction = mCallToAction
    }

    fun getAdvertiser():String {
        return advertiser
    }

    fun setAdvertiser(advertiser:String) {
        this.advertiser = advertiser
    }

    fun getStarRating(): Double {
        return starRating
    }

    fun setStarRating(starRating: Double) {
        this.starRating = starRating
    }

    fun getStoreName():String {
        return storeName
    }

    fun setStoreName(storeName:String) {
        this.storeName = storeName
    }

    fun getPrice(): Double? {
        return price
    }

    fun setPrice(mPrice: Double?) {
        this.price = mPrice
    }

    fun getDegreeOfAwesomeness():String {
        return degreeOfAwesomeness
    }

    fun setDegreeOfAwesomeness(degreeOfAwesomeness:String) {
        this.degreeOfAwesomeness = degreeOfAwesomeness
    }

    fun getImageUri(): Uri? {
        return imageUri
    }

    fun setImageUri(imageUri: Uri?) {
        this.imageUri = imageUri
    }

    fun getIconUri(): Uri? {
        return iconUri
    }

    fun setIconUri(mAppIconUri: Uri?) {
        this.iconUri = mAppIconUri
    }

    fun getInformationIcon(): ImageView? {
        return informationIcon
    }

    fun setInformationIcon(informationIcon: ImageView?) {
        this.informationIcon = informationIcon
    }

    fun getMediaView(): ISampleMediaView? {
        return mediaView
    }

    fun setMediaView(mediaView: ISampleMediaView?) {
        this.mediaView = mediaView
    }

    fun handleClick(view: View) {
        // Normally this would result in some type of click response, like a browser opening
        // or a ping to the servers. This isn't a real sdk, though, so we'll just log it.
        Log.i("SampleAdSdk", "A click has been reported for View #" + view.getId())
    }

    fun recordImpression() {
        // Here again, we'll just log that an impression took place.
        Log.i("SampleAdSdk", "An impression has been reported.")
    }

    fun registerNativeAdView(view: View?) {
        // Starts playing video if there is any video asset. Here, passing view is not mandatory
        // since we are just calling the playback to play the video.
        if (mediaView != null) {
            mediaView!!.beginPlaying()
        }
    }
}