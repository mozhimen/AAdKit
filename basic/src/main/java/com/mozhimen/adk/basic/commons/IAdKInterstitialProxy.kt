package com.mozhimen.adk.basic.commons

/**
 * @ClassName IAdKInterstitialProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/2
 * @Version 1.0
 */
interface IAdKInterstitialProxy {
    fun initInterstitialAd()
    fun loadInterstitialAd()
    fun showInterstitialAd()
    fun destroyInterstitialAd()
}