package com.mozhimen.adk.google.impls

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MuteThisAdListener
import com.google.android.gms.ads.MuteThisAdReason
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.adk.google.R
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefPauseLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.androidx.lifecycle.runOnMainThread

/**
 * @ClassName AdKGoogleNativeSimpleProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
interface IAdKGoogleNativeListener {
    fun onAdLoaded(nativeAdView: NativeAdView?)
    fun onMuteNativeAd()
    fun onLoadNativeAd(nativeAd: NativeAd?)
    fun onPopulateNativeAdView(nativeAdView: NativeAdView?)
}

@OApiInit_ByLazy
@OApiCall_BindLifecycle
class AdKGoogleNativeSimpleProxy<A>(private val _activity: A, private val _adUnitId: String) :
    BaseWakeBefPauseLifecycleObserver() where A : Activity, A : LifecycleOwner {

    private var currentNativeAd: NativeAd? = null

    private val muteThisAdReason = ArrayList<MuteThisAdReason>()

    private var nativeAdView: NativeAdView? = null

    private val _muteThisAdListener = MuteThisAdListener {
        // 广告关闭回调
        Log.i(TAG, "this native ad been muted")
    }

    private val adListener = object : AdListener() {
        override fun onAdLoaded() {
            super.onAdLoaded()
            // 广告加载成功
            Log.i(TAG, "nativeAd onAdLoaded")
//            nativeAdView?.let { vdb.flNativeAdContainer.addView(it) }
            _adKGoogleNativeListener?.onAdLoaded(nativeAdView)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            super.onAdFailedToLoad(loadAdError)
            // 广告加载失败
            Log.e(TAG, "nativeAd onAdFailedToLoad error:${loadAdError}")
        }

        override fun onAdOpened() {
            super.onAdOpened()
            // 广告页打开
            Log.i(TAG, "nativeAd onAdOpened")
        }

        override fun onAdClicked() {
            super.onAdClicked()
            // 广告被点击
            Log.i(TAG, "nativeAd onAdClicked")
        }

        override fun onAdClosed() {
            super.onAdClosed()
            // 广告页关闭
            Log.i(TAG, "nativeAd onAdClosed")
        }
    }

    private var _adKGoogleNativeListener: IAdKGoogleNativeListener? = null

    ///////////////////////////////////////////////////////////////////////

    init {
        _activity.runOnMainThread(::initAdsNative)
    }

    ///////////////////////////////////////////////////////////////////////

    fun showChoseMuteNativeAdDialog() {
        val muteThisAdReasonString = arrayOfNulls<CharSequence>(muteThisAdReason.size)
        for ((index, item) in muteThisAdReason.withIndex()) {
            muteThisAdReasonString[index] = item.description
        }
        AlertDialog.Builder(_activity)
            .setTitle("关闭此原生广告的原因是？")
            .setItems(muteThisAdReasonString) { _, which ->
                if (muteThisAdReason.size > which) {
                    muteNativeAd(muteThisAdReason[which])
                }
            }
            .create()
            .show()
    }

    fun setIAdKGoogleNativeListener(listener: IAdKGoogleNativeListener) {
        _adKGoogleNativeListener = listener
    }

    ///////////////////////////////////////////////////////////////////////

    private fun muteNativeAd(muteThisAdReason: MuteThisAdReason) {
        // 可以上报用户关闭广告的原因，便于优化广告
        currentNativeAd?.muteThisAd(muteThisAdReason)
        nativeAdView?.destroy()
        currentNativeAd?.destroy()
        _adKGoogleNativeListener?.onMuteNativeAd()
//        vdb.flNativeAdContainer.removeAllViews()
    }

    private fun initAdsNative() {
        if (AdKGoogleMgr.isInitSuccess()) {
            loadNativeAd()
        }
    }

    private fun loadNativeAd() {
        val adLoader = AdLoader.Builder(_activity, _adUnitId/*"ca-app-pub-3940256099942544/2247696110"*/)
            .forNativeAd { nativeAd ->
                // 如果在页面销毁后触发此回调，需要销毁NativeAd避免内存泄漏
                if (_activity.isDestroyed || _activity.isFinishing || _activity.isChangingConfigurations) {
                    nativeAd.destroy()
                    return@forNativeAd
                }
                currentNativeAd?.destroy()
                nativeAd.setMuteThisAdListener(_muteThisAdListener)
                currentNativeAd = nativeAd
                // 判断是否支持自定义不再显示广告
                if (nativeAd.isCustomMuteThisAdEnabled) {
                    // 获取不再显示广告的原因
                    muteThisAdReason.addAll(nativeAd.muteThisAdReasons)
                }
                _adKGoogleNativeListener?.onLoadNativeAd(nativeAd)
//                vdb.btnStopNativeAd.visibility = if (nativeAd.isCustomMuteThisAdEnabled) View.VISIBLE else View.GONE
                populateNativeAdView()
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    // 设置视频是否静音播放
                    .setVideoOptions(VideoOptions.Builder().setStartMuted(false).build())
                    // 设置自定义不再显示广告
                    .setRequestCustomMuteThisAd(true)
                    .build()
            )
            .withAdListener(adListener)
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    @SuppressLint("InflateParams")
    private fun populateNativeAdView() {
        currentNativeAd?.let { nativeAd ->
//            nativeAdView?.let { vdb.flNativeAdContainer.removeView(it) }
            _adKGoogleNativeListener?.onPopulateNativeAdView(nativeAdView)
            (LayoutInflater.from(_activity).inflate(R.layout.layout_admob_native_ad, null) as? NativeAdView)?.run {
                iconView = findViewById<AppCompatImageView>(R.id.iv_ad_app_icon).apply {
                    nativeAd.icon?.let { setImageDrawable(it.drawable) }
                    visibility = if (nativeAd.icon != null) View.VISIBLE else View.GONE
                }
                headlineView = findViewById<AppCompatTextView>(R.id.tv_ad_headline).apply {
                    text = nativeAd.headline
                }
                advertiserView = findViewById<AppCompatTextView>(R.id.tv_advertiser).apply {
                    text = nativeAd.advertiser
                    visibility = if (nativeAd.advertiser != null) View.VISIBLE else View.INVISIBLE
                }
                starRatingView = findViewById<AppCompatRatingBar>(R.id.rb_ad_stars).apply {
                    nativeAd.starRating?.let { rating = it.toFloat() }
                    visibility = if (nativeAd.starRating != null) View.VISIBLE else View.INVISIBLE
                }
                bodyView = findViewById<AppCompatTextView>(R.id.tv_ad_body).apply {
                    text = nativeAd.body
                    visibility = if (nativeAd.body != null) View.VISIBLE else View.INVISIBLE
                }
                mediaView = findViewById<MediaView>(R.id.mv_ad_media).apply {
                    nativeAd.mediaContent?.let {
                        mediaContent = it
                        it.videoController.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                            override fun onVideoStart() {
                                super.onVideoStart()
                                // 视频开始
                                Log.i(TAG, "onVideoStart")
                            }

                            override fun onVideoEnd() {
                                super.onVideoEnd()
                                // 视频结束，结束后可以刷新广告
                                Log.i(TAG, "onVideoEnd")
                            }

                            override fun onVideoPlay() {
                                super.onVideoPlay()
                                // 视频播放
                                Log.i(TAG, "onVideoPlay")
                            }

                            override fun onVideoPause() {
                                super.onVideoPause()
                                // 视频暂停
                                Log.i(TAG, "onVideoPause")
                            }

                            override fun onVideoMute(mute: Boolean) {
                                super.onVideoMute(mute)
                                // 视频是否静音
                                // mute true 静音 false 非静音
                                Log.i(TAG, "onVideoMute mute:$mute")
                            }
                        }
                    }
                }
                callToActionView = findViewById<AppCompatButton>(R.id.btn_ad_call_to_action).apply {
                    text = nativeAd.callToAction
                    visibility = if (nativeAd.callToAction != null) View.VISIBLE else View.INVISIBLE
                }
                priceView = findViewById<AppCompatTextView>(R.id.tv_ad_price).apply {
                    text = nativeAd.price
                    visibility = if (nativeAd.price != null) View.VISIBLE else View.INVISIBLE
                }
                storeView = findViewById<AppCompatTextView>(R.id.tv_ad_store).apply {
                    text = nativeAd.store
                    visibility = if (nativeAd.store != null) View.VISIBLE else View.INVISIBLE
                }
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.BOTTOM
                }
                setNativeAd(nativeAd)
                nativeAdView = this
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        currentNativeAd?.destroy()
        super.onDestroy(owner)
    }
}