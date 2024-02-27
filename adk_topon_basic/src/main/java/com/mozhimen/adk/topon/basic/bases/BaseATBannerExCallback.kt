package com.mozhimen.adk.topon.basic.bases

import android.content.Context
import android.util.Log
import android.widget.ScrollView
import com.anythink.banner.api.ATBannerExListener
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.mozhimen.basick.utilk.bases.IUtilK

/**
 * @ClassName BaseATBannerExCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/23
 * @Version 1.0
 */
open class BaseATBannerExCallback : ATBannerExListener, IUtilK {
    override fun onBannerLoaded() {
        Log.i(TAG, "onBannerLoaded")
    }

    override fun onBannerFailed(adError: AdError) {
        Log.i(TAG, "onBannerFailed: 横幅广告加载失败 ${adError.fullErrorInfo}")
    }

    override fun onBannerAutoRefreshed(entity: ATAdInfo) {
        Log.i(TAG, "onBannerAutoRefreshed: $entity")
    }

    override fun onBannerAutoRefreshFail(adError: AdError) {
        Log.i(TAG, "onBannerAutoRefreshFail: 横幅广告自动刷新失败 ${adError.fullErrorInfo}")
    }

    override fun onDeeplinkCallback(isRefresh: Boolean, adInfo: ATAdInfo, isSuccess: Boolean) {
        Log.i(TAG, "onDeeplinkCallback: $adInfo status: $isSuccess")
    }

    override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
        Log.i(TAG, "onDownloadConfirm: $adInfo networkConfirmInfo: $networkConfirmInfo")
    }

    override fun onBannerClicked(entity: ATAdInfo) {
        Log.i(TAG, "onBannerClicked: $entity")
    }

    override fun onBannerShow(entity: ATAdInfo) {
        Log.i(TAG, "onBannerShow: $entity")
    }

    override fun onBannerClose(entity: ATAdInfo) {
        Log.i(TAG, "onBannerClose: $entity")
    }
}