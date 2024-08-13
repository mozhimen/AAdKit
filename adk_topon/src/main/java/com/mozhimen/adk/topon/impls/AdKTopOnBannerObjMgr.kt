package com.mozhimen.adk.topon.impls

import android.view.View
import com.mozhimen.adk.topon.commons.IAdViewProvider

/**
 * @ClassName AdKTopOnBannerObjMgr
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/8/6
 * @Version 1.0
 */
object AdKTopOnBannerObjMgr : IAdViewProvider {
    private val _adViews by lazy { ArrayList<View>() }

    override fun applyForAdView(): View? {

    }

    override fun giveBackAdView(view: View?) {

    }
}