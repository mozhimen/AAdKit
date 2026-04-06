package com.mozhimen.adk.google.admanager.impls

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.mozhimen.adk.basic.commons.IAdKRewardedProxy
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.adk.google.impls.AdKGoogleRewardedProxy
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import java.lang.ref.WeakReference

/**
 * @ClassName AdKGoogleRewardedSimpleProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/4
 * @Version 1.0
 */
@OApiCall_BindViewLifecycle
@OApiInit_ByLazy
@OApiCall_BindLifecycle
class AdKGoogleManagerRewardedProxy(
    activity: Activity?,
) : AdKGoogleRewardedProxy(activity) {
    override fun getAdRequest(): AdRequest {
        return AdManagerAdRequest.Builder().build()
    }
}