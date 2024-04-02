package com.mozhimen.adk.topon.basic.test.uis

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.mozhimen.adk.topon.basic.bases.BaseATAdSourceStatusCallback
import com.mozhimen.adk.topon.basic.bases.BaseATInterstitialAutoEventCallback
import com.mozhimen.adk.topon.basic.bases.BaseATInterstitialAutoLoadCallback
import com.mozhimen.adk.topon.basic.bases.BaseATInterstitialExCallback
import com.mozhimen.adk.topon.basic.impls.AdKTopOnInterstitialProxy
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.basic.test.bases.BaseActivityVDB
import com.mozhimen.adk.topon.basic.test.mos.CommonViewBean
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_ViewReady
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.view.applyGone
import com.mozhimen.basick.utilk.android.view.applyVisible
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName InterstitialAdActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/23
 * @Version 1.0
 */
@OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
class InterstitialAdActivity : BaseActivityVDB<com.mozhimen.adk.topon.basic.test.databinding.ActivityInterstitialBinding>(), IUtilK {

    override val contentViewId: Int
        get() = R.layout.activity_interstitial
    override val adType: Int
        get() = ATAdConst.ATMixedFormatAdType.INTERSTITIAL

    private val atInterstitialAutoLoadCallback = object : BaseATInterstitialAutoLoadCallback() {
        override fun onInterstitialAutoLoaded(placementId: String) {
            super.onInterstitialAutoLoaded(placementId)
            printLogOnUI("onInterstitialAutoLoaded PlacementId:$placementId: onInterstitialAutoLoaded")
        }

        override fun onInterstitialAutoLoadFail(placementId: String, adError: AdError) {
            super.onInterstitialAutoLoadFail(placementId, adError)
            printLogOnUI("onInterstitialAutoLoadFail PlacementId:$placementId: onInterstitialAutoLoadFail:${adError.fullErrorInfo}")
        }
    }
    private val atInterstitialAutoEventCallback = object : BaseATInterstitialAutoEventCallback() {
        override fun onInterstitialAdClicked(adInfo: ATAdInfo) {
            super.onInterstitialAdClicked(adInfo)
            printLogOnUI("onInterstitialAdClicked:")
        }

        override fun onInterstitialAdShow(adInfo: ATAdInfo) {
            super.onInterstitialAdShow(adInfo)
            printLogOnUI("onInterstitialAdShow")
        }

        override fun onInterstitialAdClose(adInfo: ATAdInfo) {
            super.onInterstitialAdClose(adInfo)
            printLogOnUI("onInterstitialAdClose")
        }

        override fun onInterstitialAdVideoStart(adInfo: ATAdInfo) {
            super.onInterstitialAdVideoStart(adInfo)
            printLogOnUI("onInterstitialAdVideoStart")
        }

        override fun onInterstitialAdVideoEnd(adInfo: ATAdInfo) {
            super.onInterstitialAdVideoEnd(adInfo)
            printLogOnUI("onInterstitialAdVideoEnd")
        }

        override fun onInterstitialAdVideoError(adError: AdError) {
            super.onInterstitialAdVideoError(adError)
            printLogOnUI("onInterstitialAdVideoError:" + adError.fullErrorInfo)
        }

        override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
            super.onDeeplinkCallback(adInfo, isSuccess)
            printLogOnUI("onDeeplinkCallback: isSuccess=$isSuccess")
        }

        override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
            super.onDownloadConfirm(context, adInfo, networkConfirmInfo)
            printLogOnUI("onDownloadConfirm")
        }
    }
    private val atInterstitialExCallback = object : BaseATInterstitialExCallback() {
        override fun onDeeplinkCallback(adInfo: ATAdInfo, isSuccess: Boolean) {
            super.onDeeplinkCallback(adInfo, isSuccess)
            printLogOnUI("onDeeplinkCallback")
        }

        override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
            super.onDownloadConfirm(context, adInfo, networkConfirmInfo)
            printLogOnUI("onDownloadConfirm")
        }

        override fun onInterstitialAdLoaded() {
            super.onInterstitialAdLoaded()
            printLogOnUI("onInterstitialAdLoaded")
        }

        override fun onInterstitialAdLoadFail(adError: AdError) {
            super.onInterstitialAdLoadFail(adError)
            printLogOnUI("onInterstitialAdLoadFail:" + adError.fullErrorInfo)
        }

        override fun onInterstitialAdClicked(entity: ATAdInfo) {
            super.onInterstitialAdClicked(entity)
            printLogOnUI("onInterstitialAdClicked")
        }

        override fun onInterstitialAdShow(entity: ATAdInfo) {
            super.onInterstitialAdShow(entity)
            printLogOnUI("onInterstitialAdShow")
        }

        override fun onInterstitialAdClose(entity: ATAdInfo) {
            super.onInterstitialAdClose(entity)
            printLogOnUI("onInterstitialAdClose")
        }

        override fun onInterstitialAdVideoStart(entity: ATAdInfo) {
            super.onInterstitialAdVideoStart(entity)
            printLogOnUI("onInterstitialAdVideoStart")
        }

        override fun onInterstitialAdVideoEnd(entity: ATAdInfo) {
            super.onInterstitialAdVideoEnd(entity)
            printLogOnUI("onInterstitialAdVideoEnd")
        }

        override fun onInterstitialAdVideoError(adError: AdError) {
            super.onInterstitialAdVideoError(adError)
            printLogOnUI("onInterstitialAdVideoError")
        }
    }
    private val atAdSourceStatusListener = object : BaseATAdSourceStatusCallback() {}
    private val _adkTopOnInterstitialProxy: AdKTopOnInterstitialProxy by lazy { AdKTopOnInterstitialProxy() }

    @OptIn(OApiCall_ViewReady::class)
    override fun onSelectPlacementId(placementId: String?) {
        val isAutoLoad = placementId?.let { _adkTopOnInterstitialProxy.isAutoLoad(it) } ?: false
        vdb.includeSelectNet?.ckAutoLoad?.setChecked(isAutoLoad)
        if (placementId != null) {
            _adkTopOnInterstitialProxy.apply {
                initInterstitialAd(this@InterstitialAdActivity, placementId, "", atInterstitialExCallback, atAdSourceStatusListener, atInterstitialAutoEventCallback)
                loadAd()
                showAd(this@InterstitialAdActivity)
            }
        }
    }

    override fun getCommonViewBean(): CommonViewBean {
        val commonViewBean = CommonViewBean()
        commonViewBean.setTitleBar(findViewById(R.id.title_bar))
        commonViewBean.setSpinnerSelectPlacement(findViewById(R.id.spinner_1))
        commonViewBean.setTvLogView(findViewById(R.id.tv_show_log))
        commonViewBean.setTitleResId(R.string.anythink_title_interstitial)
        return commonViewBean
    }

    ///////////////////////////////////////////////////////////////////////

    @OptIn(OApiCall_ViewReady::class)
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        _adkTopOnInterstitialProxy.apply {
            bindLifecycle(this@InterstitialAdActivity)
            initAutoLoad(this@InterstitialAdActivity, atInterstitialAutoLoadCallback)
        }

        vdb.includeSelectNet?.ckAutoLoad?.apply {
            applyVisible()
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    val curPlacementId: String = mCurrentPlacementId!!
                    _adkTopOnInterstitialProxy.isAutoLoad(curPlacementId, true)
                    vdb.includeBtn?.loadAdBtn?.applyGone()
                } else {
                    val curPlacementId: String = mCurrentPlacementId!!
                    _adkTopOnInterstitialProxy.isAutoLoad(curPlacementId, false)
                    vdb.includeBtn?.loadAdBtn?.applyVisible()
                }
            }
        }

        vdb.includeBtn?.loadAdBtn?.setOnClickListener {
            if (!_adkTopOnInterstitialProxy.isInit()) {
                printLogOnUI("ATInterstitial is not init.")
                return@setOnClickListener
            }
            printLogOnUI(getString(R.string.anythink_ad_status_loading))
            _adkTopOnInterstitialProxy.loadAd()
        }

        vdb.includeBtn?.isAdReadyBtn?.setOnClickListener {
            mCurrentPlacementId?.let { placementId ->
                if (_adkTopOnInterstitialProxy.isAutoLoad(placementId)) {
                    printLogOnUI("interstitial auto load ad ready status:" + _adkTopOnInterstitialProxy.isAutoLoadReady(placementId))
                } else {
//         boolean isReady = mInterstitialAd.isAdReady();
                    val atAdStatusInfo = _adkTopOnInterstitialProxy.atInterstitial?.checkAdStatus()
                    printLogOnUI("interstitial ad ready status:" + atAdStatusInfo?.isReady)
                    val atAdInfoList = _adkTopOnInterstitialProxy.atInterstitial?.checkValidAdCaches()
                    Log.i(TAG, "Valid Cahce size:" + (atAdInfoList?.size ?: 0))
                    if (atAdInfoList != null) {
                        for (adInfo in atAdInfoList) {
                            Log.i(TAG, " Cahce detail:$adInfo")
                        }
                    }
                }
            }
        }

        vdb.includeBtn?.showAdBtn?.setOnClickListener {
            mCurrentPlacementId?.let { placementId ->
                _adkTopOnInterstitialProxy.showAd(this)
            }
        }
    }
}


