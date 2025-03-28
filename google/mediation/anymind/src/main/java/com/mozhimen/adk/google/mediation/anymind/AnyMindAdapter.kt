package com.mozhimen.adk.google.mediation.anymind

import android.content.Context
import com.google.android.gms.ads.VersionInfo
import com.google.android.gms.ads.mediation.Adapter
import com.google.android.gms.ads.mediation.InitializationCompleteCallback
import com.google.android.gms.ads.mediation.MediationAdConfiguration
import com.google.android.gms.ads.mediation.MediationAdLoadCallback
import com.google.android.gms.ads.mediation.MediationBannerAd
import com.google.android.gms.ads.mediation.MediationBannerAdCallback
import com.google.android.gms.ads.mediation.MediationBannerAdConfiguration
import com.google.android.gms.ads.mediation.MediationConfiguration
import com.google.android.gms.ads.mediation.MediationInterstitialAd
import com.google.android.gms.ads.mediation.MediationInterstitialAdCallback
import com.google.android.gms.ads.mediation.MediationInterstitialAdConfiguration
import com.google.android.gms.ads.mediation.MediationNativeAdCallback
import com.google.android.gms.ads.mediation.MediationNativeAdConfiguration
import com.google.android.gms.ads.mediation.MediationRewardedAd
import com.google.android.gms.ads.mediation.MediationRewardedAdCallback
import com.google.android.gms.ads.mediation.MediationRewardedAdConfiguration
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper
import com.mozhimen.adk.google.mediation.SampleAdRequest
import com.mozhimen.adk.google.mediation.anymind.impls.banner.AnyMindBannerCustomEventLoader
import com.mozhimen.adk.google.mediation.anymind.impls.interstitial.AnyMindInterstitialCustomEventLoader
import com.mozhimen.adk.google.mediation.anymind.impls.rewarded.AnyMindRewardedCustomEventLoader
import com.mozhimen.adk.google.mediation.anymind.impls.nativead.AnyMindNativeCustomEventLoader

/**
 * @ClassName AnyManagerAdapter
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/4
 * @Version 1.0
 */
class AnyMindAdapter : Adapter() {
    companion object {
        /**
         * Helper method to create a [SampleAdRequest].
         *
         * @param mediationAdConfiguration The mediation request with targeting information.
         * @return The created [SampleAdRequest].
         */
        @JvmStatic
        fun createSampleRequest(mediationAdConfiguration: MediationAdConfiguration): SampleAdRequest {
            val request = SampleAdRequest()
            request.setTestMode(mediationAdConfiguration.isTestRequest)
            request.setKeywords(mediationAdConfiguration.mediationExtras.keySet())
            return request
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    // This method won't be called for custom events.
    override fun initialize(p0: Context, p1: InitializationCompleteCallback, p2: MutableList<MediationConfiguration>) {
        // This is where you will initialize the SDK that this custom
        // event is built for. Upon finishing the SDK initialization,
        // call the completion handler with success.
        p1.onInitializationSucceeded()
    }

    override fun getSDKVersionInfo(): VersionInfo {
        val versionString: String = SampleAdRequest.getSDKVersion()
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (splits.size >= 3) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt()
            return VersionInfo(major, minor, micro)
        }

        return VersionInfo(0, 0, 0)
    }

    override fun getVersionInfo(): VersionInfo {
        val versionString: String = BuildConfig.ADAPTER_VERSION
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (splits.size >= 4) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt() * 100 + splits[3].toInt()
            return VersionInfo(major, minor, micro)
        }

        return VersionInfo(0, 0, 0)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private var _bannerLoader: AnyMindBannerCustomEventLoader? = null
    private var _interstitialLoader: AnyMindInterstitialCustomEventLoader? = null
    private var _nativeLoader: AnyMindNativeCustomEventLoader? = null
    private var _rewardedLoader: AnyMindRewardedCustomEventLoader? = null
//    private var _openLoader: SampleOpenCustomEventLoader? = null
//    private var _interscrollerLoader: SampleInterscrollerCustomEventLoader? = null
//    private var _rewardedInterstitialLoader: SampleRewardedInterstitialCustomEventLoader? = null

    ///////////////////////////////////////////////////////////////////////////////////////////////

    override fun loadBannerAd(
        adConfiguration: MediationBannerAdConfiguration,
        callback: MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback>,
    ) {
        _bannerLoader = AnyMindBannerCustomEventLoader(adConfiguration, callback)
        _bannerLoader!!.loadAd()
    }

    override fun loadInterstitialAd(
        adConfiguration: MediationInterstitialAdConfiguration,
        callback: MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback>,
    ) {
        _interstitialLoader = AnyMindInterstitialCustomEventLoader(adConfiguration, callback)
        _interstitialLoader!!.loadAd()
    }

    override fun loadNativeAd(
        adConfiguration: MediationNativeAdConfiguration,
        callback: MediationAdLoadCallback<UnifiedNativeAdMapper, MediationNativeAdCallback>,
    ) {
        _nativeLoader = AnyMindNativeCustomEventLoader(adConfiguration, callback)
        _nativeLoader!!.loadAd()
    }

    override fun loadRewardedAd(
        adConfiguration: MediationRewardedAdConfiguration,
        callback: MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback>,
    ) {
        _rewardedLoader = AnyMindRewardedCustomEventLoader(adConfiguration, callback)
        _rewardedLoader!!.loadAd()
    }

//    override fun loadAppOpenAd(
//        adConfiguration: MediationAppOpenAdConfiguration,
//        callback: MediationAdLoadCallback<MediationAppOpenAd, MediationAppOpenAdCallback>,
//    ) {
//        _openLoader = SampleOpenCustomEventLoader(adConfiguration, callback)
//        _openLoader!!.loadAd()
//    }

//    override fun loadInterscrollerAd(
//        adConfiguration: MediationBannerAdConfiguration,
//        callback: MediationAdLoadCallback<MediationInterscrollerAd, MediationBannerAdCallback>,
//    ) {
//        _interscrollerLoader = SampleInterscrollerCustomEventLoader(adConfiguration, callback)
//        _interscrollerLoader.loadAd()
//    }

//    override fun loadRewardedInterstitialAd(
//        adConfiguration: MediationRewardedAdConfiguration,
//        callback: MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback>,
//    ) {
//        _rewardedInterstitialLoader = SampleRewardedInterstitialCustomEventLoader(adConfiguration, callback)
//        _rewardedInterstitialLoader.loadAd()
//    }
}