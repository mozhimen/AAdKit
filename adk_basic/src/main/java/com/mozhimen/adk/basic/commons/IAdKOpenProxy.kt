package com.mozhimen.adk.basic.commons

import android.app.Activity

/**
 * @ClassName IAdKOpenProxy
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/2
 * @Version 1.0
 */
interface IAdKOpenProxy {
    fun initOpenAd()
    fun loadOpenAd()
    fun showOpenAd(activity: Activity?)
    fun destroyOpenAd()
}