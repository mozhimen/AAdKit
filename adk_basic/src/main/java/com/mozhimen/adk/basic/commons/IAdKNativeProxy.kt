package com.mozhimen.adk.basic.commons

import android.view.ViewGroup

/**
 * @ClassName IAdKNativeProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/2
 * @Version 1.0
 */
interface IAdKNativeProxy {
    fun initNativeAd()
    fun loadNativeAd()
    fun addNativeViewToContainer(container: ViewGroup)
    fun destroyNativeAd()
}