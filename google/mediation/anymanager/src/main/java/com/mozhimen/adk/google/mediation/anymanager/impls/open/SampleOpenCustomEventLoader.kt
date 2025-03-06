package com.mozhimen.adk.google.mediation.anymanager.impls.open

import android.content.Context
import com.google.android.gms.ads.mediation.MediationAdLoadCallback
import com.google.android.gms.ads.mediation.MediationAppOpenAd
import com.google.android.gms.ads.mediation.MediationAppOpenAdCallback
import com.google.android.gms.ads.mediation.MediationAppOpenAdConfiguration
import com.google.android.gms.ads.mediation.MediationInterstitialAd
import com.mozhimen.adk.google.mediation.commons.ISampleAdListener
import com.mozhimen.adk.google.mediation.commons.ISampleCustomEventLoader

/**
 * @ClassName SampleOpenCustomEventLoader
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/3/5
 * @Version 1.0
 */
class SampleOpenCustomEventLoader constructor(
    /** Configuration for requesting the interstitial ad from the third party network.  */
    private val mediationAppOpenAdConfiguration: MediationAppOpenAdConfiguration,
    /** Callback that fires on loading success or failure.  */
    private val mediationAdLoadCallback: MediationAdLoadCallback<MediationAppOpenAd, MediationAppOpenAdCallback>,
) : ISampleAdListener, MediationInterstitialAd, ISampleCustomEventLoader {
    override fun showAd(p0: Context) {
    }

    override fun loadAd() {
    }
}