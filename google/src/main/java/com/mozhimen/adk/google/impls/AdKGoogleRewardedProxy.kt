package com.mozhimen.adk.google.impls

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.mozhimen.adk.basic.commons.IAdKRewardedProxy
import com.mozhimen.adk.google.AdKGoogleMgr
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
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
open class AdKGoogleRewardedProxy(
    private var _activity: Activity?,
) : BaseWakeBefDestroyLifecycleObserver(), IAdKRewardedProxy {
    private var _rewardedAd: RewardedAd? = null
    private var _adUnitId: String = ""

    private var _rewardedAdAdLoadCallback: RewardedAdLoadCallback? = null
    private var _fullScreenContentCallback: FullScreenContentCallback? = null
    private var _onUserEarnedRewardCallback: OnUserEarnedRewardCallback? = null

    //////////////////////////////////////////////////////////////////////////////

    fun initRewardedAdListener(
        rewardedAdAdLoadCallback: RewardedAdLoadCallback,
        fullScreenContentCallback: FullScreenContentCallback,
        onUserEarnedRewardCallback: OnUserEarnedRewardCallback,
    ) {
        _rewardedAdAdLoadCallback = rewardedAdAdLoadCallback
        _fullScreenContentCallback = fullScreenContentCallback
        _onUserEarnedRewardCallback = onUserEarnedRewardCallback
    }

    fun initRewardedAdParams(adUnitId: String) {
        _adUnitId = adUnitId
    }

    //////////////////////////////////////////////////////////////////////////////

    override fun initRewardedAd() {

    }

    override fun loadRewardedAd() {
        if (AdKGoogleMgr.isInitSuccess()) {
            // adUnitId为Admob后台创建的激励视频广告的id
            RewardedAd.load(_context, _adUnitId/*"ca-app-pub-3940256099942544/5224354917"*/, getAdRequest(), RewardedAdLoadCallbackImpl())
        }
    }

    open fun getAdRequest():AdRequest=
        AdRequest.Builder().build()

    override fun showRewardedAd() {
        if (_activity != null && _rewardedAd != null) {
            _rewardedAd!!.apply {
                fullScreenContentCallback = FullScreenContentCallbackImpl()// 设置广告事件回调
                show(_activity!!, OnUserEarnedRewardCallback())
            }
        }
    }

    override fun destroyRewardedAd() {
        _rewardedAd?.fullScreenContentCallback = null
        _rewardedAd = null
    }

    //////////////////////////////////////////////////////////////////////////////

    override fun onCreate(owner: LifecycleOwner) {
        initRewardedAd()
        loadRewardedAd()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        destroyRewardedAd()
        _rewardedAdAdLoadCallback = null
        _fullScreenContentCallback = null
        _activity = null
        super.onDestroy(owner)
    }

    //////////////////////////////////////////////////////////////////////////////

    private inner class RewardedAdLoadCallbackImpl : RewardedAdLoadCallback() {
        override fun onAdLoaded(rewardedAd: RewardedAd) {
            UtilKLogWrapper.i(TAG, "rewarded onAdLoaded adapter class name: ${rewardedAd.responseInfo.mediationAdapterClassName}")
            _rewardedAdAdLoadCallback?.onAdLoaded(rewardedAd)

            // 加载成功
            _rewardedAd = rewardedAd
// 展示广告
//            this@AdsRewardedActivity.rewardedAd?.show(this@AdsRewardedActivity, rewardedVideoAdEarnedCallback)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            UtilKLogWrapper.e(TAG, "rewarded onAdFailedToLoad error:${loadAdError.message}")
            _rewardedAdAdLoadCallback?.onAdFailedToLoad(loadAdError)
        }
    }

    //////////////////////////////////////////////////////////////////////////////////

    private inner class FullScreenContentCallbackImpl() : FullScreenContentCallback() {
        override fun onAdImpression() {
            // 被记录为展示成功时调用
            UtilKLogWrapper.i(TAG, "rewarded onAdImpression")
            _fullScreenContentCallback?.onAdImpression()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            // 展示失败时调用，此时销毁当前的激励视频广告对象，重新加载激励视频广告
            UtilKLogWrapper.e(TAG, "rewarded onAdFailedToShowFullScreenContent error:${adError.message}")
            _fullScreenContentCallback?.onAdFailedToShowFullScreenContent(adError)

//            _rewardedAd = null
//            vdb.btnShowRewardedAd.applyInVisible()
//            loadRewardedAd()
        }

        override fun onAdDismissedFullScreenContent() {
            // 隐藏时调用，此时销毁当前的激励视频广告对象，重新加载激励视频广告
            UtilKLogWrapper.i(TAG, "rewarded onAdShowedFullScreenContent")
            _fullScreenContentCallback?.onAdDismissedFullScreenContent()

            destroyRewardedAd()
//            _rewardedAd = null
//            vdb.btnShowRewardedAd.applyInVisible()
//            loadRewardedAd()
        }

        override fun onAdClicked() {
            UtilKLogWrapper.i(TAG, "rewarded onAdClicked")
            _fullScreenContentCallback?.onAdClicked()
        }

        override fun onAdShowedFullScreenContent() {
            UtilKLogWrapper.i(TAG, "rewarded onAdShowedFullScreenContent")
            _fullScreenContentCallback?.onAdShowedFullScreenContent()
        }
    }

    inner class OnUserEarnedRewardCallback : OnUserEarnedRewardListener {
        override fun onUserEarnedReward(p0: RewardItem) {
            // 用户获得奖励回调// 奖励的类型 // 奖励的金额
            UtilKLogWrapper.i(TAG, "rewardedVideo onUserEarnedReward type:${p0.type}, amount:${p0.amount}")
            _onUserEarnedRewardCallback?.onUserEarnedReward(p0)
        }
    }
}