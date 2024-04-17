package com.mozhimen.adk.topon.basic.impls

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATAdSourceStatusListener
import com.anythink.core.api.AdError
import com.anythink.nativead.api.ATNative
import com.anythink.nativead.api.ATNativeAdView
import com.anythink.nativead.api.ATNativeDislikeListener
import com.anythink.nativead.api.ATNativeEventListener
import com.anythink.nativead.api.ATNativeMaterial
import com.anythink.nativead.api.ATNativeNetworkListener
import com.anythink.nativead.api.ATNativePrepareExInfo
import com.anythink.nativead.api.ATNativePrepareInfo
import com.anythink.nativead.api.NativeAd
import com.anythink.nativead.unitgroup.api.CustomNativeAd
import com.mozhimen.adk.basic.commons.IAdKNativeProxy
import com.mozhimen.adk.topon.basic.commons.INativeAdLoadedListener
import com.mozhimen.adk.topon.basic.cons.CVideoAction
import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.basick.utilk.android.view.addView_ofMatchParent

/**
 * @ClassName AdKTopOnNativeProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/11
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class AdKTopOnNativeProxy :
    BaseWakeBefDestroyLifecycleObserver(), IAdKNativeProxy, ATNativeNetworkListener, ATAdSourceStatusListener, ATNativeEventListener {
    private var _aTNative: ATNative? = null
    private var _nativeAd: NativeAd? = null
    private var _aTNativeView: ATNativeAdView? = null
    private var _placementId = ""
    private var _scenarioId: String = ""
    private var _nativeAdSize: MutableMap<String, Any>? = null

    private var _aTNativeNetworkListener: ATNativeNetworkListener? = null
    private var _nativeAdLoadedListener: INativeAdLoadedListener? = null
    private var _aTAdSourceStatusListener: ATAdSourceStatusListener? = null
    private var _aTNativeEventListener: ATNativeEventListener? = null
    private var _aTNativeDislikeListener: ATNativeDislikeListener? = null

    ///////////////////////////////////////////////////////////////////////

    fun setVideoMute(boolean: Boolean) {
        _nativeAd?.setVideoMute(boolean)
    }

    fun resumeVideo() {
        _nativeAd?.resumeVideo()
    }

    fun pauseVideo() {
        _nativeAd?.pauseVideo()
    }

    ///////////////////////////////////////////////////////////////////////

    fun initNativeAdListener(
        aTNativeNetworkListener: ATNativeNetworkListener?,
        nativeAdLoadedListener: INativeAdLoadedListener?,
        aTAdSourceStatusListener: ATAdSourceStatusListener?,
        aTNativeEventListener: ATNativeEventListener?,
        aTNativeDislikeListener: ATNativeDislikeListener?
    ) {
        _aTNativeNetworkListener = aTNativeNetworkListener
        _nativeAdLoadedListener = nativeAdLoadedListener
        _aTAdSourceStatusListener = aTAdSourceStatusListener
        _aTNativeEventListener = aTNativeEventListener
        _aTNativeDislikeListener = aTNativeDislikeListener
    }

    fun initNativeAdParams(placementId: String, scenarioId: String) {
        _placementId = placementId
        _scenarioId = scenarioId
    }

    fun initNativeAdSize(width: Int, height: Int) {
        val localMap: MutableMap<String, Any> = HashMap()
        localMap[ATAdConst.KEY.AD_WIDTH] = width
        localMap[ATAdConst.KEY.AD_HEIGHT] = height//50f.dp2px.toInt()
        _nativeAdSize = localMap
    }

    override fun initNativeAd() {
        _aTNative = ATNative(_context, _placementId, this).apply {
            _nativeAdSize?.let { setLocalExtra(it) }
            setAdSourceStatusListener(this@AdKTopOnNativeProxy)
        }
        ATNative.entryAdScenario(_placementId, _scenarioId)
        _aTNative?.makeAdRequest()
    }

//    fun isNativeAdReady(): Boolean =
//        (_aTNative?.is()?.isReady ?: false).also { UtilKLogWrapper.d(TAG, "isNativeAdReady: $it") }

    override fun loadNativeAd() {
        if (/*isNativeAdReady() &&*/ _nativeAd != null) {
            UtilKLogWrapper.d(TAG, "loadNativeAd: ")
            _nativeAd!!.apply {
                setNativeEventListener(this@AdKTopOnNativeProxy)
                setDislikeCallbackListener(ATNativeDislikeCallback())
            }
            try {
                printNativeAdMaterial(_nativeAd?.adMaterial)
                val atNativePrepareInfo: ATNativePrepareInfo = ATNativePrepareExInfo()
                _nativeAdLoadedListener?.onNativeAdViewLoad(_nativeAd, _nativeAd?.adMaterial, atNativePrepareInfo)?.also {
                    UtilKLogWrapper.d(TAG, "loadNativeAd: ATNativeAdView ${it.first} View ${it.second}")
                    _aTNativeView = it.first
                    if (_nativeAd!!.isNativeExpress) {
                        _nativeAd!!.renderAdContainer(_aTNativeView, null)
                    } else if (it.second != null) {
                        _nativeAd!!.renderAdContainer(it.first, it.second)
                    }
                    _nativeAd!!.prepare(_aTNativeView, atNativePrepareInfo)
                }
            } catch (e: Exception) {
                UtilKLogWrapper.e(TAG, "loadNativeAd: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    override fun addNativeViewToContainer(container: ViewGroup) {
        if (_aTNativeView != null) {
            UtilKLogWrapper.d(TAG, "addNativeViewToContainer")
            container.addView_ofMatchParent(_aTNativeView!!)// 把 Banner Ad 添加到根布局
        } else {
            UtilKLogWrapper.d(TAG, "addNativeViewToContainer _aTNativeView null")
        }
    }

    ///////////////////////////////////////////////////////////////////////

    fun destroyATNative() {
        if (_aTNative != null) {
            _aTNative!!.setAdListener(null)
            _aTNative!!.setAdSourceStatusListener(null)
        }
        _aTNative = null
    }

    override fun destroyNativeAd() {
        if (_nativeAd != null) {
            _nativeAd!!.setNativeEventListener(null)
            _nativeAd!!.destory()
        }
        _nativeAd = null
    }

    fun destroyNativeAdView() {
        _aTNativeView?.destory()
        _aTNativeView = null
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initNativeAd()
    }

    override fun onResume(owner: LifecycleOwner) {
        _nativeAd?.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        _nativeAd?.onPause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyATNative()
        destroyNativeAd()
        destroyNativeAdView()
        _aTNativeNetworkListener = null
        _aTAdSourceStatusListener = null
        super.onDestroy(owner)
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onNativeAdLoaded() {
        UtilKLogWrapper.d(TAG, "_aTNativeNetworkListener_ onNativeAdLoaded: ")

        _aTNativeNetworkListener?.onNativeAdLoaded()

        _nativeAd = if (_scenarioId.isNotEmpty()) {
            _aTNative?.getNativeAd(_scenarioId)
        } else
            _aTNative?.getNativeAd()

        loadNativeAd()

        _nativeAdLoadedListener?.onNativeAdViewLoaded(_nativeAd, getVideoFuns(_nativeAd?.getAdMaterial()?.getAdType()))
    }

    override fun onNativeAdLoadFail(p0: AdError?) {
        UtilKLogWrapper.e(TAG, "_aTNativeNetworkListener_ onNativeAdLoadFail: AdError $p0")

        _aTNativeNetworkListener?.onNativeAdLoadFail(p0)
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onAdSourceBiddingAttempt(p0: ATAdInfo?) {
        UtilKLogWrapper.d(TAG, "_aTAdSourceStatusListener onAdSourceBiddingAttempt: ATAdInfo $p0")

        _aTAdSourceStatusListener?.onAdSourceBiddingAttempt(p0)
    }

    override fun onAdSourceAttempt(p0: ATAdInfo?) {
        UtilKLogWrapper.d(TAG, "_aTAdSourceStatusListener onAdSourceAttempt: ATAdInfo $p0")

        _aTAdSourceStatusListener?.onAdSourceAttempt(p0)
    }

    override fun onAdSourceBiddingFilled(p0: ATAdInfo?) {
        UtilKLogWrapper.w(TAG, "_aTAdSourceStatusListener onAdSourceBiddingFilled: ATAdInfo $p0")

        _aTAdSourceStatusListener?.onAdSourceBiddingFilled(p0)
    }

    override fun onAdSourceLoadFilled(p0: ATAdInfo?) {
        UtilKLogWrapper.w(TAG, "_aTAdSourceStatusListener onAdSourceLoadFilled: ATAdInfo $p0")

        _aTAdSourceStatusListener?.onAdSourceLoadFilled(p0)
    }

    override fun onAdSourceBiddingFail(p0: ATAdInfo?, p1: AdError?) {
        UtilKLogWrapper.e(TAG, "_aTAdSourceStatusListener onAdSourceBiddingFail: AdError $p1 ATAdInfo $p0")

        _aTAdSourceStatusListener?.onAdSourceBiddingFail(p0, p1)
    }

    override fun onAdSourceLoadFail(p0: ATAdInfo?, p1: AdError?) {
        UtilKLogWrapper.e(TAG, "_aTAdSourceStatusListener onAdSourceLoadFail: AdError $p1 ATAdInfo $p0")

        _aTAdSourceStatusListener?.onAdSourceLoadFail(p0, p1)
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onAdImpressed(p0: ATNativeAdView?, p1: ATAdInfo?) {
        UtilKLogWrapper.d(TAG, "_aTNativeEventListener___ onAdImpressed: ATAdInfo $p0")

        _aTNativeEventListener?.onAdImpressed(p0, p1)
    }

    override fun onAdClicked(p0: ATNativeAdView?, p1: ATAdInfo?) {
        UtilKLogWrapper.d(TAG, "_aTNativeEventListener___ onAdClicked: ATAdInfo $p0")

        _aTNativeEventListener?.onAdClicked(p0, p1)
    }

    override fun onAdVideoStart(p0: ATNativeAdView?) {
        UtilKLogWrapper.d(TAG, "_aTNativeEventListener___ onAdVideoStart: ATNativeAdView $p0")

        _aTNativeEventListener?.onAdVideoStart(p0)
    }

    override fun onAdVideoEnd(p0: ATNativeAdView?) {
        UtilKLogWrapper.d(TAG, "_aTNativeEventListener___ onAdVideoEnd: ATNativeAdView $p0")

        _aTNativeEventListener?.onAdVideoEnd(p0)
    }

    override fun onAdVideoProgress(p0: ATNativeAdView?, p1: Int) {
        UtilKLogWrapper.d(TAG, "_aTNativeEventListener___ onAdVideoProgress: ATNativeAdView $p0 Int $p1")

        _aTNativeEventListener?.onAdVideoProgress(p0, p1)
    }

    ///////////////////////////////////////////////////////////////////////

    private inner class ATNativeDislikeCallback : ATNativeDislikeListener() {
        override fun onAdCloseButtonClick(p0: ATNativeAdView?, p1: ATAdInfo?) {
            UtilKLogWrapper.d(TAG, "_aTNativeDislikeListener_ onAdCloseButtonClick: ATNativeAdView $p1 ATAdInfo $p0")

            _aTNativeDislikeListener?.onAdCloseButtonClick(p0, p1)
        }
    }

    ///////////////////////////////////////////////////////////////////////

    private fun getVideoFuns(adType: String?): List<String>? {
        if (adType == CustomNativeAd.NativeAdConst.VIDEO_TYPE) {
            var isNativeExpress = true
            if (_nativeAd != null) {
                isNativeExpress = _nativeAd!!.isNativeExpress()
            }
            if (isNativeExpress) {
                return null
            }
            val atAdInfo: ATAdInfo = _nativeAd!!.getAdInfo()
            val networkId = atAdInfo.networkFirmId
            when (networkId) {
                8 -> {//for GDT
                    return listOf(CVideoAction.VOICE_CHANGE, CVideoAction.VIDEO_RESUME, CVideoAction.VIDEO_PAUSE, CVideoAction.VIDEO_PROGRESS)
                }

                22, 28 -> {//for BaiDu //for KuaiShou
                    return listOf(CVideoAction.VIDEO_PROGRESS)
                }

                66, 67 -> {//for Adx //for Direct
                    return listOf(CVideoAction.VOICE_CHANGE, CVideoAction.VIDEO_RESUME, CVideoAction.VIDEO_PAUSE, CVideoAction.VIDEO_PROGRESS)
                }
            }
        }
        return null
    }

    private fun printNativeAdMaterial(adMaterial: ATNativeMaterial?) {
        adMaterial ?: return
        when (adMaterial.adType) {
            CustomNativeAd.NativeAdConst.VIDEO_TYPE -> UtilKLogWrapper.d(TAG, "Ad source type: Video" + ", video duration: " + adMaterial.videoDuration)
            CustomNativeAd.NativeAdConst.IMAGE_TYPE -> UtilKLogWrapper.d(TAG, "Ad source type: Image")
            else -> UtilKLogWrapper.d(TAG, "Ad source type: Unknown")
        }
        when (adMaterial.nativeType) {
            CustomNativeAd.NativeType.FEED -> UtilKLogWrapper.d(TAG, "Native type: Feed")
            CustomNativeAd.NativeType.PATCH -> UtilKLogWrapper.d(TAG, "Native type: Patch")
        }
        UtilKLogWrapper.d(
            TAG,
            """
     [main]
     show native material:
     adMaterial:$adMaterial
     adType:${adMaterial.adType} nativeType:${adMaterial.nativeType} nativeAdInteractionType:${adMaterial.nativeAdInteractionType}
     adAppInfo:${adMaterial.adAppInfo}
     adLogo:${adMaterial.adLogo}
     networkInfoMap:${adMaterial.networkInfoMap}
     advertiserInfoOperate:${adMaterial.advertiserInfoOperate}
     
     [content]
     title:${adMaterial.title} descriptionText:${adMaterial.descriptionText} callToActionText:${adMaterial.callToActionText}
     appPrice:${adMaterial.appPrice} starRating:${adMaterial.starRating} domain:${adMaterial.domain} warning:${adMaterial.warning}
     adFrom:${adMaterial.adFrom} appCommentNum:${adMaterial.appCommentNum} advertiserName:${adMaterial.advertiserName}
     
     [address]
     mainImageUrl:${adMaterial.mainImageUrl} iconImageUrl:${adMaterial.iconImageUrl} videoUrl:${adMaterial.videoUrl} adChoiceIconUrl:${adMaterial.adChoiceIconUrl}
     imageUrlList:${adMaterial.imageUrlList}
     
     [view]
     adIconView:${adMaterial.adIconView} adLogoView:${adMaterial.adLogoView} getAdMediaView:${adMaterial.getAdMediaView()} appDownloadButton:${adMaterial.appDownloadButton}
     supportSetPrivacyClickViewList:${adMaterial.supportSetPrivacyClickViewList()} supportSetPermissionClickViewList:${adMaterial.supportSetPermissionClickViewList()}
     
     [size]
     mainImageHeight:${adMaterial.mainImageHeight} mainImageWidth:${adMaterial.mainImageWidth} nativeExpressWidth:${adMaterial.nativeExpressWidth} nativeExpressHeight${adMaterial.nativeExpressHeight}
     
     [video]
     videoWidth:${adMaterial.videoWidth} videoHeight:${adMaterial.videoHeight} videoDuration:${adMaterial.videoDuration} videoProgress:${adMaterial.videoProgress}
     nativeCustomVideo:${adMaterial.nativeCustomVideo}

     [download]
     downloadStatus:${adMaterial.downloadStatus} downloadProgress:${adMaterial.downloadProgress}
     
     """.trimIndent(),
        )
    }
}