package com.mozhimen.adk.topon.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.anythink.nativead.api.ATNativeImageView
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.xmlk.bases.BaseLayoutKLinear

/**
 * @ClassName MutiImageView
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

class LayoutKATImageGroup @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseLayoutKLinear(context, attrs, defStyleAttr) {
    var padding: Int

    init {
        orientation = HORIZONTAL
        padding = 5f.dp2px().toInt()
        //        setPadding(padding, padding, padding, padding);
    }

    fun applyATImageGroup(imageList: List<String?>, imageWidth: Int, imageHeight: Int) {
        removeAllViews()
        val size = imageList.size
        for (url in imageList) {
            val width = resources.displayMetrics.widthPixels
            val atNativeImageView = ATNativeImageView(context)
            atNativeImageView.setImage(url)
            atNativeImageView.setPadding(padding, padding, padding, padding)
            val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width * 600 / size / 1024)
            if (imageWidth > 0 && imageHeight > 0) {
                layoutParams.height = width * imageHeight / size / imageWidth
            }
            layoutParams.weight = 1f
            addView(atNativeImageView, layoutParams)
        }
    }
}
