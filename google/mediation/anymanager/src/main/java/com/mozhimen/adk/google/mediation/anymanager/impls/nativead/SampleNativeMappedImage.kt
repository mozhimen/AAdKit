package com.mozhimen.adk.google.mediation.anymanager.impls.nativead

import android.graphics.drawable.Drawable
import android.net.Uri

/**
 * @ClassName SampleNativeMappedImage
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * A simple class that fits the the [NativeAd.Image] interface and can be filled with assets
 * returned by the Sample SDK.
 */
class SampleNativeMappedImage(private val drawable: Drawable, private val imageUri: Uri, private val scale: Double) : com.google.android.gms.ads.formats.NativeAd.Image() {
    override fun getDrawable(): Drawable {
        return drawable
    }

    override fun getUri(): Uri {
        return imageUri
    }

    override fun getScale(): Double {
        return scale
    }
}
