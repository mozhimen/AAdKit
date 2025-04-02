package com.mozhimen.adk.google.impls

import android.content.Context
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MuteThisAdListener
import com.google.android.gms.ads.MuteThisAdReason
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.mozhimen.adk.basic.commons.IAdKNativeProxy
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.adk.google.commons.INativeAdLoadedListener
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.view.addViewSafe_MATCH_MATCH

/**
 * @ClassName AdKGoogleNativeSimpleProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
open class AdKGoogleNativeProxy :
    BaseWakeBefDestroyLifecycleObserver(), IAdKNativeProxy {

    private var _adLoader: AdLoader? = null
    private var _nativeAd: NativeAd? = null
    private var _nativeAdView: NativeAdView? = null
    val nativeAdView get() = _nativeAdView
    private var _nativeAdOptions: NativeAdOptions? = NativeAdOptions.Builder()
        .setVideoOptions(VideoOptions.Builder().setStartMuted(true).build())// 设置视频是否静音播放
        .setRequestCustomMuteThisAd(true)// 设置自定义不再显示广告
        .build()
    private var _adUnitId: String = ""
    private val _muteThisAdReasons = ArrayList<MuteThisAdReason>()

    private var _nativeAdListener: AdListener? = null
    private var _nativeAdLoadedListener: INativeAdLoadedListener? = null
    private var _nativeAdMuteThisListener: MuteThisAdListener? = null

    ///////////////////////////////////////////////////////////////////////

    //region # dialog
    fun showChoseMuteNativeAdDialog(context: Context, title: String) {
        val muteThisAdReasonString = arrayOfNulls<CharSequence>(_muteThisAdReasons.size)
        for ((index, item) in _muteThisAdReasons.withIndex()) {
            muteThisAdReasonString[index] = item.description
        }
        AlertDialog.Builder(context)
            .setTitle(title/*"关闭此原生广告的原因是？"*/)
            .setItems(muteThisAdReasonString) { _, which ->
                if (_muteThisAdReasons.size > which) {
                    // 可以上报用户关闭广告的原因，便于优化广告
                    _nativeAd?.muteThisAd(_muteThisAdReasons[which])
                    destroyNativeAdView()
                    destroyNativeAd()
                    destroyNativeAdLoader()
                    _nativeAdLoadedListener?.onNativeAdViewMuted()
//        vdb.flNativeAdContainer.removeAllViews()
                }
            }
            .create()
            .show()
    }
    //endregion

    ///////////////////////////////////////////////////////////////////////

    fun initNativeAdListener(nativeAdListener: AdListener?, nativeAdLoadedListener: INativeAdLoadedListener?, nativeAdMuteThisListener: MuteThisAdListener?) {
        UtilKLogWrapper.d(TAG, "initNativeAdListener: ")
        _nativeAdListener = nativeAdListener
        _nativeAdLoadedListener = nativeAdLoadedListener
        _nativeAdMuteThisListener = nativeAdMuteThisListener
    }

    fun initNativeAdParams(adUnitId: String, opts: NativeAdOptions? = null) {
        _adUnitId = adUnitId
        opts?.let {
            _nativeAdOptions = it
        }
    }

    ///////////////////////////////////////////////////////////////////////

    override fun initNativeAd() {
        if (AdKGoogleMgr.isInitSuccess()) {
            _adLoader = AdLoader.Builder(_context, _adUnitId/*"ca-app-pub-3940256099942544/2247696110"*/)
                .forNativeAd(NativeAdLoadedCallback())
                .apply {
                    if (_nativeAdOptions != null)
                        withNativeAdOptions(_nativeAdOptions!!)
                }
                .withAdListener(NativeAdCallback())
                .build()
            _adLoader?.loadAd(getAdRequest())
        }
    }

    open fun getAdRequest(): AdRequest =
        AdRequest.Builder().build()

    override fun loadNativeAd() {

    }

    fun loadNativeAdView() {
        // 如果在页面销毁后触发此回调，需要销毁NativeAd避免内存泄漏
//            if (_activity.isDestroyed || _activity.isFinishing || _activity.isChangingConfigurations) {
//                nativeAd.destroy()
//                return@forNativeAd
//            }
//            _nativeAd?.destroy()

        _nativeAd?.let { nativeAd ->
            nativeAd.setMuteThisAdListener(NativeAdMuteThisCallback())
            if (nativeAd.isCustomMuteThisAdEnabled) {// 判断是否支持自定义不再显示广告
                _muteThisAdReasons.addAll(nativeAd.muteThisAdReasons)// 获取不再显示广告的原因
            }
            /*//                vdb.btnStopNativeAd.visibility = if (nativeAd.isCustomMuteThisAdEnabled) View.VISIBLE else View.GONE
                        //            nativeAdView?.let { vdb.flNativeAdContainer.removeView(it) }


            //            (LayoutInflater.from(_activity).inflate(R.layout.layout_admob_native_ad, null) as? NativeAdView)?.run {
            //                iconView = findViewById<AppCompatImageView>(R.id.iv_ad_app_icon).apply {
            //                    nativeAd.icon?.let { setImageDrawable(it.drawable) }
            //                    visibility = if (nativeAd.icon != null) View.VISIBLE else View.GONE
            //                }
            //                headlineView = findViewById<AppCompatTextView>(R.id.tv_ad_headline).apply {
            //                    text = nativeAd.headline
            //                }
            //                advertiserView = findViewById<AppCompatTextView>(R.id.tv_advertiser).apply {
            //                    text = nativeAd.advertiser
            //                    visibility = if (nativeAd.advertiser != null) View.VISIBLE else View.INVISIBLE
            //                }
            //                starRatingView = findViewById<AppCompatRatingBar>(R.id.rb_ad_stars).apply {
            //                    nativeAd.starRating?.let { rating = it.toFloat() }
            //                    visibility = if (nativeAd.starRating != null) View.VISIBLE else View.INVISIBLE
            //                }
            //                bodyView = findViewById<AppCompatTextView>(R.id.tv_ad_body).apply {
            //                    text = nativeAd.body
            //                    visibility = if (nativeAd.body != null) View.VISIBLE else View.INVISIBLE
            //                }
            //                mediaView = findViewById<MediaView>(R.id.mv_ad_media).apply {
            //                    nativeAd.mediaContent?.let {
            //                        mediaContent = it
            //                        it.videoController.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
            //                            override fun onVideoStart() {
            //                                super.onVideoStart()
            //                                // 视频开始
            //                                UtilKLogWrapper.i(TAG, "onVideoStart")
            //                            }
            //
            //                            override fun onVideoEnd() {
            //                                super.onVideoEnd()
            //                                // 视频结束，结束后可以刷新广告
            //                                UtilKLogWrapper.i(TAG, "onVideoEnd")
            //                            }
            //
            //                            override fun onVideoPlay() {
            //                                super.onVideoPlay()
            //                                // 视频播放
            //                                UtilKLogWrapper.i(TAG, "onVideoPlay")
            //                            }
            //
            //                            override fun onVideoPause() {
            //                                super.onVideoPause()
            //                                // 视频暂停
            //                                UtilKLogWrapper.i(TAG, "onVideoPause")
            //                            }
            //
            //                            override fun onVideoMute(mute: Boolean) {
            //                                super.onVideoMute(mute)
            //                                // 视频是否静音
            //                                // mute true 静音 false 非静音
            //                                UtilKLogWrapper.i(TAG, "onVideoMute mute:$mute")
            //                            }
            //                        }
            //                    }
            //                }
            //                callToActionView = findViewById<AppCompatButton>(R.id.btn_ad_call_to_action).apply {
            //                    text = nativeAd.callToAction
            //                    visibility = if (nativeAd.callToAction != null) View.VISIBLE else View.INVISIBLE
            //                }
            //                priceView = findViewById<AppCompatTextView>(R.id.tv_ad_price).apply {
            //                    text = nativeAd.price
            //                    visibility = if (nativeAd.price != null) View.VISIBLE else View.INVISIBLE
            //                }
            //                storeView = findViewById<AppCompatTextView>(R.id.tv_ad_store).apply {
            //                    text = nativeAd.store
            //                    visibility = if (nativeAd.store != null) View.VISIBLE else View.INVISIBLE
            //                }
            //                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            //                    gravity = Gravity.BOTTOM
            //                }
            //            }*/

            UtilKLogWrapper.d(
                TAG,
                "loadNativeAd: headline(${nativeAd.headline}) advertiser(${nativeAd.advertiser}) starRating(${nativeAd.starRating}) body(${nativeAd.body}) starRating(${nativeAd.starRating}) callToAction(${nativeAd.callToAction}) price(${nativeAd.price}) store(${nativeAd.store})"
            )
            _nativeAdLoadedListener?.onNativeAdViewLoad(
                nativeAd,
                nativeAd.icon,
                nativeAd.headline,
                nativeAd.advertiser,
                nativeAd.starRating,
                nativeAd.body,
                nativeAd.mediaContent,
                nativeAd.callToAction,
                nativeAd.price,
                nativeAd.store
            )?.apply {
                setNativeAd(nativeAd)
                _nativeAdView = this
            }
        }
    }

    override fun addNativeViewToContainer(container: ViewGroup): Boolean {
        if (_nativeAdView != null) {
            container.addViewSafe_MATCH_MATCH(_nativeAdView!!)// 把 Banner Ad 添加到根布局
            return true
        } else {
            UtilKLogWrapper.d(TAG, "addNativeViewToContainer _nativeAdView null")
            return false
        }
    }

    //////////////////////////////////////////////////////////////////////////

    fun destroyNativeAdLoader() {
        _adLoader = null
    }

    override fun destroyNativeAd() {
        _nativeAd?.destroy()
        _nativeAd = null
    }

    fun destroyNativeAdView() {
        _nativeAdView?.destroy()
        _nativeAdView = null
    }

    //////////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initNativeAd()
        loadNativeAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyNativeAdLoader()
        destroyNativeAd()
        destroyNativeAdView()
        _nativeAdListener = null
        _nativeAdLoadedListener = null
        _nativeAdMuteThisListener = null
        super.onDestroy(owner)
    }

    //////////////////////////////////////////////////////////////////////////

    private inner class NativeAdMuteThisCallback : MuteThisAdListener {
        override fun onAdMuted() {
            // 广告关闭回调
            UtilKLogWrapper.i(TAG, "onAdMuted this native ad been muted")

            _nativeAdMuteThisListener?.onAdMuted()
        }
    }

    private inner class NativeAdLoadedCallback : NativeAd.OnNativeAdLoadedListener {
        override fun onNativeAdLoaded(p0: NativeAd) {
            UtilKLogWrapper.d(TAG, "onNativeAdLoaded: adapter class name: ${p0.responseInfo?.mediationAdapterClassName}")

            _nativeAd = p0

            loadNativeAdView()

            _nativeAdLoadedListener?.onNativeAdViewLoaded(p0)
        }
    }

    private inner class NativeAdCallback : AdListener() {
        override fun onAdLoaded() {
            // 广告加载成功
            UtilKLogWrapper.i(TAG, "nativeAd onAdLoaded")
//            nativeAdView?.let { vdb.flNativeAdContainer.addView(it) }
            _nativeAdListener?.onAdLoaded()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            // 广告加载失败
            UtilKLogWrapper.e(TAG, "nativeAd onAdFailedToLoad error:${loadAdError}")
            _nativeAdListener?.onAdFailedToLoad(loadAdError)
        }

        override fun onAdOpened() {
            // 广告页打开
            UtilKLogWrapper.i(TAG, "nativeAd onAdOpened")
            _nativeAdListener?.onAdOpened()
        }

        override fun onAdClicked() {
            // 广告被点击
            UtilKLogWrapper.i(TAG, "nativeAd onAdClicked")
            _nativeAdListener?.onAdClicked()
        }

        override fun onAdClosed() {
            // 广告页关闭
            UtilKLogWrapper.i(TAG, "nativeAd onAdClosed")
            _nativeAdListener?.onAdClosed()
        }
    }
}