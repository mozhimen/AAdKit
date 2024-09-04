package com.mozhimen.adk.google.test.impls

import android.util.Log
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.widget.FrameLayout
import com.chad.library.adapter3.provider.BaseItemProvider
import com.chad.library.adapter3.viewholder.BaseViewHolder
import com.google.android.gms.ads.AdListener
import com.mozhimen.adk.google.impls.AdKGoogleBannerProxy
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.util.px2dp
import com.mozhimen.kotlin.utilk.wrapper.UtilKScreen
import com.mozhimen.kotlin.utilk.android.view.addView_ofMatchParent
import com.mozhimen.kotlin.utilk.android.view.applyGone
import com.mozhimen.kotlin.utilk.android.view.applyVisible
import com.ty.lelejoy.fun_widget.R
import com.ty.lelejoy.fun_widget.cons.ListBundle
import com.ty.lelejoy.fun_widget.cons.ListBundle_AdBannerGoogle
import com.ty.lelejoy.fun_widget.cons.CItemExtras

/**
 * @ClassName ItemRecyclerVerticalAdBanner
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/2/28 0:22
 * @Version 1.0
 */
class ItemListAdBannerGoogle : BaseItemProvider<ListBundle>() {
    override val itemViewType: Int
        get() = CItemExtras.ITEM_LIST_AD_BANNER_GOOGLE
    override val layoutId: Int
        get() = R.layout.item_recycler_vertical_ad_banner_change

    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    override fun onBindViewHolder(holder: BaseViewHolder, item: ListBundle, position: Int?) {
        super.onBindViewHolder(holder, item, position)
        if (item is ListBundle_AdBannerGoogle) {
            val adKGoogleInlineBannerProxy = AdKGoogleBannerProxy()
            adKGoogleInlineBannerProxy.apply {
                initBannerAdListener(object : AdListener() {
                    override fun onAdLoaded() {
                        item.bannerAdView = adKGoogleInlineBannerProxy.bannerAdView
                        adKGoogleInlineBannerProxy.addBannerViewToContainer(holder.findViewById<FrameLayout>(R.id.item_detail_container))
                        holder.findViewById<FrameLayout>(R.id.item_detail_container).applyVisible()
                    }
                })
                initBannerAdSize((UtilKScreen.getWidth_ofDisplayMetrics_ofSys().px2dp - 46f).toInt())
                initBannerAdParams(com.ty.lelejoy.module_common.BuildConfig.gms_ad_placement_id_banner)
                bindLifecycle(holder)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder, item: ListBundle?, position: Int?) {
        super.onViewAttachedToWindow(holder, item, position)
        if (item != null && item is ListBundle_AdBannerGoogle) {
            if (item.bannerAdView != null) {
                holder.findViewById<FrameLayout>(R.id.item_detail_container).addView_ofMatchParent(item.bannerAdView!!)
            }
        }
    }


    override fun onViewDetachedFromWindow(holder: BaseViewHolder, item: ListBundle?, position: Int?) {
        holder.findViewById<FrameLayout>(R.id.item_detail_container).removeAllViews()
        super.onViewDetachedFromWindow(holder, item, position)
    }

    override fun onViewRecycled(holder: BaseViewHolder, item: ListBundle?, position: Int?) {
        if (item != null && item is ListBundle_AdBannerGoogle) {
            if (item.bannerAdView != null) {
                UtilKLogWrapper.d(TAG, "onViewRecycled: item $item")
                item.bannerAdView = null
            }
        }
        holder.findViewById<FrameLayout>(R.id.item_detail_container).applyGone()
        super.onViewRecycled(holder, item, position)
    }
}