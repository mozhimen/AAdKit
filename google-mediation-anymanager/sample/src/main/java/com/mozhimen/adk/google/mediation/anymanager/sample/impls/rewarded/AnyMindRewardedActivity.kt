package com.mozhimen.adk.google.mediation.anymanager.sample.impls.rewarded

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import com.mozhimen.adk.google.mediation.anymind.R
import com.mozhimen.adk.google.mediation.commons.ISampleRewardedAdListener

/**
 * @ClassName SampleSDKAdsActivity
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
/**
 * The [AnyMindRewardedActivity] is used to show sample rewarded ad by the Sample SDK.
 */
class AnyMindRewardedActivity : AppCompatActivity() {
    /**
     * Displays the amount of time remaining for the ad to complete.
     */
    private var countdownTimerView: TextView? = null

    /**
     * Closes the ad/activity.
     */
    private var closeAdButton: ImageButton? = null

    /**
     * Flag to determine whether or not it is ok to close this activity. The ad can be skipped after 5
     * seconds; no reward is provided if the ad is closed before the countdown is finished.
     */
    private var isSkippable = false

    /**
     * Flag to determine whether not the ad is clickable. The ad is not clickable when showing the
     * countdown (clickable after the video completed playing).
     */
    private var isClickable = false

    /**
     * A simple countdown timer.
     */
    private var countDownTimer: CountDownTimer? = null

    /**
     * The Sample SDK's rewarded ad object that needs to be shown to the user.
     */
    private var sampleRewardedAd: AnyMindRewardedAd? = null

    /**
     * Forwards rewarded ad events.
     */
    private var rewardedAdListener: ISampleRewardedAdListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_sdk_ads)

        // Get the Sample SDK rewarded ad, which was added to the intent as extra.
        val intent = intent
        if (intent != null && intent.hasExtra(KEY_REWARDED_AD_EXTRA)) {
            sampleRewardedAd = intent.getParcelableExtra(KEY_REWARDED_AD_EXTRA)
        } else {
            // Rewarded ad not available, close ad.
            finish()
        }

        rewardedAdListener = sampleRewardedAd!!.getListener()
        if (rewardedAdListener != null) {
            rewardedAdListener!!.onAdFullScreen()
        }

        findViewById<View>(R.id.main_view).setOnClickListener {
            if (rewardedAdListener != null && isClickable) {
                rewardedAdListener!!.onAdClicked()
            }
        }
        closeAdButton = findViewById<View>(R.id.close_button) as ImageButton
        closeAdButton!!.setOnClickListener {
            if (countDownTimer != null) {
                if (rewardedAdListener != null) {
                    rewardedAdListener!!.onAdClosed()
                }
                countDownTimer!!.cancel()
                countDownTimer = null
            }
            finish()
        }
        countdownTimerView = findViewById<View>(R.id.countdown_timer_textView) as TextView

        // Countdown timer for 10 seconds.
        countDownTimer =
            object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (millisUntilFinished > 6000) {
                        isSkippable = false
                        closeAdButton!!.visibility = View.GONE
                    } else {
                        // The ad is skippable after 5 seconds.
                        isSkippable = true
                        closeAdButton!!.visibility = View.VISIBLE
                    }
                    countdownTimerView!!.text = String.format(Locale.getDefault(), "%d", (millisUntilFinished / 1000))
                }

                override fun onFinish() {
                    val rewardAmount: Int = sampleRewardedAd?.reward ?: 0
                    if (rewardedAdListener != null) {
                        rewardedAdListener!!.onAdRewarded("", rewardAmount)
                        rewardedAdListener!!.onAdCompleted()
                    }
                    countdownTimerView!!.text = String.format(Locale.getDefault(), "Rewarded with reward amount %d", rewardAmount)
                    isClickable = true
                }
            }.start()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
    }

    override fun onBackPressed() {
        if (isSkippable) {
            super.onBackPressed()
        }
    }

    companion object {
        /**
         * Key to set and get rewarded ad as an extra for an intent.
         */
        const val KEY_REWARDED_AD_EXTRA: String = "rewarded_ad_extra"
    }
}
