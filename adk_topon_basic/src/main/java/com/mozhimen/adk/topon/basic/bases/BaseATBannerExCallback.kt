package com.mozhimen.adk.topon.basic.bases

import android.content.Context
import android.util.Log
import android.widget.ScrollView
import com.anythink.banner.api.ATBannerExListener
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATNetworkConfirmInfo
import com.anythink.core.api.AdError
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName BaseATBannerExCallback
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/23
 * @Version 1.0
 */
open class BaseATBannerExCallback : ATBannerExListener, IUtilK {
    override fun onBannerLoaded() {
    }

    override fun onBannerFailed(adError: AdError) {
    }

    override fun onBannerAutoRefreshed(entity: ATAdInfo) {
    }

    override fun onBannerAutoRefreshFail(adError: AdError) {
    }

    override fun onDeeplinkCallback(isRefresh: Boolean, adInfo: ATAdInfo, isSuccess: Boolean) {
    }

    override fun onDownloadConfirm(context: Context, adInfo: ATAdInfo, networkConfirmInfo: ATNetworkConfirmInfo) {
    }

    override fun onBannerClicked(entity: ATAdInfo) {
    }

    override fun onBannerShow(entity: ATAdInfo) {
    }

    override fun onBannerClose(entity: ATAdInfo) {
    }
}