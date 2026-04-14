package com.mozhimen.adk.google.mediation.anymanager.sample.impls.nativead

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.mozhimen.adk.google.mediation.commons.ISampleMediaView
import com.mozhimen.adk.google.mediation.commons.ISampleMediaViewListener

/**
 * @ClassName SampleMediaView
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * SampleMediaView is a mocked media view returned by the Sample SDK. Normally, a media view would
 * contain an image or video asset. Since this one is just a mock, it displays a series of text
 * values instead.
 */
@SuppressLint("SetTextI18n")
class AnyMindMediaView constructor(context: Context) : AppCompatTextView(context), ISampleMediaView {
    var listener: ISampleMediaViewListener? = null

    init {
        this.setBackgroundColor(-0xff0100)
        this.setGravity(Gravity.CENTER)
        this.setText("I'm a SampleMediaView.")
    }

    /**
     * Sets a [SampleNativeAdListener] to listen for ad events.
     *
     * @param listener The SampleMediaView listener.
     */
    fun setMediaViewListener(listener: ISampleMediaViewListener?) {
        this.listener = listener
    }

    /**
     * Show the text loading progress as if a video is playing.
     */
    override fun beginPlaying() {
        this.setText("Playback has begun.")

        val handler = Handler()
        val runningTimeMillis = 10000

        handler.postDelayed({
            this@AnyMindMediaView.setText(
                """
                Playback is 25% finished.
                Some characters have been introduced.
                """.trimIndent()
            )
        }, (runningTimeMillis / 4).toLong())

        handler.postDelayed({
            this@AnyMindMediaView.setText(
                """
                Playback is 50% finished.
                The characters have encountered a problem.
                """.trimIndent()
            )
        }, (runningTimeMillis / 2).toLong())

        handler.postDelayed({
            this@AnyMindMediaView.setText(
                """
                Playback is 75% finished.
                But wait, a product solves their problem!
                """.trimIndent()
            )
        }, (runningTimeMillis * 3 / 4).toLong())

        handler.postDelayed({
            this@AnyMindMediaView.setText(
                """
                Playback is complete.
                We've all learned about the product.
                """.trimIndent()
            )
            if (listener != null) {
                listener!!.onVideoEnd()
            }
        }, runningTimeMillis.toLong())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, (heightMeasureSpec * 5) / 4)
    }
}
