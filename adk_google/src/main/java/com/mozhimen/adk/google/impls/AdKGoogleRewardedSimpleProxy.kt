//package com.mozhimen.adk.google.impls
//
//import android.app.Activity
//import android.util.Log
//import androidx.lifecycle.LifecycleOwner
//import com.google.android.gms.ads.AdError
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.FullScreenContentCallback
//import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.OnUserEarnedRewardListener
//import com.google.android.gms.ads.rewarded.RewardedAd
//import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
//import com.mozhimen.adk.google.AdKGoogleMgr
//import com.mozhimen.basick.elemk.androidx.lifecycle.bases.BaseWakeBefDestroyLifecycleObserver
//import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
//import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
//import com.mozhimen.basick.utilk.androidx.lifecycle.runOnMainThread
//import java.lang.ref.WeakReference
//
///**
// * @ClassName AdKGoogleRewardedSimpleProxy
// * @Description TODO
// * @Author Mozhimen & Kolin Zhao
// * @Date 2024/2/4
// * @Version 1.0
// */
//interface IAdKGoogleRewardedListener {
//    fun onAdLoaded()
//    fun onAdDismissedFullScreenContent()
//    fun onAdFailedToShowFullScreenContent()
//}
//
//@OApiInit_ByLazy
//@OApiCall_BindLifecycle
//class AdKGoogleRewardedSimpleProxy<A>(private val _activityRef: WeakReference<A>, private val _adUnitId: String) : BaseWakeBefDestroyLifecycleObserver() where A : Activity, A : LifecycleOwner {
//    private var _rewardedAd: RewardedAd? = null
//
//    private val _rewardedAdLoadCallback = object : RewardedAdLoadCallback() {
//        override fun onAdLoaded(rewardedAd: RewardedAd) {
//            super.onAdLoaded(rewardedAd)
//            Log.i(TAG, "rewardedVideo onAdLoaded")
//            // 加载成功
//            this@AdKGoogleRewardedSimpleProxy._rewardedAd = rewardedAd
//            // 设置广告事件回调
//            this@AdKGoogleRewardedSimpleProxy._rewardedAd?.fullScreenContentCallback = _rewardedVideoAdCallback
//            // 展示广告
////            this@AdsRewardedActivity.rewardedAd?.show(this@AdsRewardedActivity, rewardedVideoAdEarnedCallback)
//
////            vdb.btnShowRewardedAd.applyVisible()
//            _adKGoogleRewardedListener?.onAdLoaded()
//        }
//
//        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//            super.onAdFailedToLoad(loadAdError)
//            // 加载失败
//            Log.e(TAG, "rewardedVideo onAdFailedToLoad error:${loadAdError.message}")
//        }
//    }
//
//    private val _rewardedVideoAdCallback = object : FullScreenContentCallback() {
//        override fun onAdImpression() {
//            super.onAdImpression()
//            // 被记录为展示成功时调用
//            Log.i(TAG, "rewardedVideo onAdImpression")
//        }
//
//        override fun onAdShowedFullScreenContent() {
//            super.onAdShowedFullScreenContent()
//            // 显示时调用
//            Log.i(TAG, "rewardedVideo onAdShowedFullScreenContent")
//        }
//
//        override fun onAdClicked() {
//            super.onAdClicked()
//            // 被点击时调用
//            Log.i(TAG, "rewardedVideo onAdClicked")
//        }
//
//        override fun onAdDismissedFullScreenContent() {
//            super.onAdDismissedFullScreenContent()
//            // 隐藏时调用，此时销毁当前的激励视频广告对象，重新加载激励视频广告
//            _rewardedAd = null
////            vdb.btnShowRewardedAd.applyInVisible()
//            _adKGoogleRewardedListener?.onAdDismissedFullScreenContent()
//            loadRewardedVideoAd()
//        }
//
//        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//            super.onAdFailedToShowFullScreenContent(adError)
//            // 展示失败时调用，此时销毁当前的激励视频广告对象，重新加载激励视频广告
//            Log.e(TAG, "rewardedVideo onAdFailedToShowFullScreenContent error:${adError.message}")
//            _rewardedAd = null
////            vdb.btnShowRewardedAd.applyInVisible()
//            _adKGoogleRewardedListener?.onAdFailedToShowFullScreenContent()
//            loadRewardedVideoAd()
//        }
//    }
//
//    private val _rewardedVideoAdEarnedCallback = OnUserEarnedRewardListener {
//        // 用户获得奖励回调
//        // 奖励的类型
//        val type = it.type
//        // 奖励的金额
//        val amount = it.amount
//        Log.i(TAG, "rewardedVideo onUserEarnedReward type:$type, amount:$amount")
//    }
//
//    private var _adKGoogleRewardedListener: IAdKGoogleRewardedListener? = null
//
//    //////////////////////////////////////////////////////////////////////////////
//
//    init {
//        _activityRef.get()?.runOnMainThread(::initAdsRewarded)
//    }
//
//    //////////////////////////////////////////////////////////////////////////////
//
//    fun setAdKGoogleRewardedListener(listener: IAdKGoogleRewardedListener) {
//        _adKGoogleRewardedListener = listener
//    }
//
//    fun showRewardedVideo() {
//        if (_activityRef.get() != null) {
//            _rewardedAd?.show(_activityRef.get()!!, _rewardedVideoAdEarnedCallback)
//        }
//    }
//
//    //////////////////////////////////////////////////////////////////////////////
//
//    private fun initAdsRewarded() {
//        if (AdKGoogleMgr.isInitSuccess()) {
//            loadRewardedVideoAd()
//        }
//    }
//
//    private fun loadRewardedVideoAd() {
//        // adUnitId为Admob后台创建的激励视频广告的id
//        RewardedAd.load(_context, _adUnitId/*"ca-app-pub-3940256099942544/5224354917"*/, AdRequest.Builder().build(), _rewardedAdLoadCallback)
//    }
//
//    //////////////////////////////////////////////////////////////////////////////
//
//    override fun onDestroy(owner: LifecycleOwner) {
//        _adKGoogleRewardedListener = null
//        super.onDestroy(owner)
//    }
//}