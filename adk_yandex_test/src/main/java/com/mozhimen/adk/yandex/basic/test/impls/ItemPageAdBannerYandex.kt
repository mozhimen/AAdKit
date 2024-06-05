package com.mozhimen.adk.yandex.basic.test.impls

import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import android.widget.FrameLayout
import com.mozhimen.adk.yandex.basic.bases.BaseBannerAdEventCallback
import com.mozhimen.adk.yandex.basic.impls.AdKYandexBannerProxy
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.view.addView_ofMatchParent
import com.mozhimen.basick.utilk.android.view.applyGone
import com.mozhimen.basick.utilk.android.view.applyVisible
import com.mozhimen.basick.utilk.wrapper.UtilKScreen
import com.mozhimen.pagingk.bases.BasePagingKVHKProvider
import com.mozhimen.xmlk.vhk.VHKRecycler
import com.ty.lelejoy.fun_widget.R
import com.ty.lelejoy.fun_widget.cons.CItemExtras
import com.ty.lelejoy.fun_widget.cons.PageBundle
import com.ty.lelejoy.fun_widget.cons.PageBundle_AdBannerYandex

/**
 * @ClassName ItemRecyclerVerticalAdBanner
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/2/28 0:22
 * @Version 1.0
 */
class ItemPageAdBannerYandex : BasePagingKVHKProvider<PageBundle>() {
    override val itemViewType: Int
        get() = CItemExtras.ITEM_PAGE_AD_BANNER_YANDEX_CODE
    override val layoutId: Int
        get() = R.layout.item_recycler_vertical_ad_banner_change

    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    override fun onBindViewHolder(holder: VHKRecycler, item: PageBundle?, position: Int) {
        super.onBindViewHolder(holder, item, position)
        if (item is PageBundle_AdBannerYandex) {
            val adKYandexInlineBannerProxy = AdKYandexBannerProxy()
            adKYandexInlineBannerProxy.apply {
                initBannerAdListener(object : BaseBannerAdEventCallback() {
                    override fun onAdLoaded() {
                        item.bannerAdView = adKYandexInlineBannerProxy.bannerAdView
                        adKYandexInlineBannerProxy.addBannerViewToContainer(holder.findViewById<FrameLayout>(R.id.item_detail_container))
                        holder.findViewById<FrameLayout>(R.id.item_detail_container).applyVisible()
                    }
                })
                initBannerAdSize(UtilKScreen.getWidth_ofDisplayMetrics_ofSys() - 46f.dp2px.toInt(), 50f.dp2px.toInt())
                initBannerAdParams(com.ty.lelejoy.module_common.BuildConfig.yandex_placement_id_banner)
                bindLifecycle(holder)
            }
        }

    }

    override fun onViewAttachedToWindow(holder: VHKRecycler, item: PageBundle?, position: Int?) {
        super.onViewAttachedToWindow(holder, item, position)
        if (item != null && item is PageBundle_AdBannerYandex) {
            if (item.bannerAdView != null) {
                holder.findViewById<FrameLayout>(R.id.item_detail_container).addView_ofMatchParent(item.bannerAdView!!)
            }
        }
    }


    override fun onViewDetachedFromWindow(holder: VHKRecycler, item: PageBundle?, position: Int?) {
        holder.findViewById<FrameLayout>(R.id.item_detail_container).removeAllViews()
        super.onViewDetachedFromWindow(holder, item, position)
    }

    override fun onViewRecycled(holder: VHKRecycler, item: PageBundle?, position: Int?) {
        if (item != null && item is PageBundle_AdBannerYandex) {
            if (item.bannerAdView != null) {
                UtilKLogWrapper.d(TAG, "onViewRecycled: item $item")
                item.bannerAdView = null
            }
        }
        holder.findViewById<FrameLayout>(R.id.item_detail_container).applyGone()
        super.onViewRecycled(holder, item, position)
    }
}