package com.mozhimen.adk.topon.commons

import android.view.View

/**
 * @ClassName IListAdProvider
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/8/6
 * @Version 1.0
 */
interface IAdViewProvider {
    fun applyForAdView(): View?
    fun giveBackAdView(view: View?)
}