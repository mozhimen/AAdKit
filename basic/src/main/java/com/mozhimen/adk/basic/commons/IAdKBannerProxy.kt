package com.mozhimen.adk.basic.commons

import android.view.ViewGroup

/**
 * @ClassName IAdKBannerProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/2
 * @Version 1.0
 */
interface IAdKBannerProxy {
    fun initBannerAd()
    fun loadBannerAd()
    fun initBannerAdSize(width: Int, height: Int)
    fun addBannerViewToContainer(container: ViewGroup)
    fun destroyBannerAd()
}