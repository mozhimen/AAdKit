package com.mozhimen.adk.yandex.test.impls

import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.widget.FrameLayout
import com.mozhimen.adk.yandex.bases.BaseBannerAdEventCallback
import com.mozhimen.adk.yandex.impls.AdKBannerProxy
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.view.applyGone
import com.mozhimen.kotlin.utilk.android.view.applyVisible
import com.mozhimen.kotlin.utilk.wrapper.UtilKScreen

/**
 * @ClassName ItemRecyclerVerticalAdBanner
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/2/28 0:22
 * @Version 1.0
 */
//class ItemListAdBannerYandex : BaseItemProvider<ListBundle>() {
//    override val itemViewType: Int
//        get() = CItemExtras.ITEM_LIST_AD_BANNER_YANDEX
//    override val layoutId: Int
//        get() = R.layout.item_recycler_vertical_ad_banner_change
//
//    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
//    override fun onBindViewHolder(holder: BaseViewHolder, item: ListBundle, position: Int?) {
//        super.onBindViewHolder(holder, item, position)
//        if (item is ListBundle_AdBannerYandex) {
//            val adKYandexInlineBannerProxy = AdKBannerProxy()
//            adKYandexInlineBannerProxy.apply {
//                initBannerAdListener(object : BaseBannerAdEventCallback() {
//                    override fun onAdLoaded() {
//                        item.bannerAdView = adKYandexInlineBannerProxy.bannerAdView
//                        adKYandexInlineBannerProxy.addBannerViewToContainer(holder.findViewById<FrameLayout>(R.id.item_detail_container))
//                        holder.findViewById<FrameLayout>(R.id.item_detail_container).applyVisible()
//                    }
//                })
//                initBannerAdSize(UtilKScreen.getWidth_ofDisplayMetrics_ofSys() - 46f.dp2px.toInt(), 50f.dp2px.toInt())
//                initBannerAdParams(com.ty.lelejoy.common.BuildConfig.yandex_placement_id_banner)
//                bindLifecycle(holder)
//            }
//        }
//
//    }
//
//    override fun onViewAttachedToWindow(holder: BaseViewHolder, item: ListBundle?, position: Int?) {
//        super.onViewAttachedToWindow(holder, item, position)
//        if (item != null && item is ListBundle_AdBannerYandex) {
//            if (item.bannerAdView != null) {
//                holder.findViewById<FrameLayout>(R.id.item_detail_container).addView_ofMatchParent(item.bannerAdView!!)
//            }
//        }
//    }
//
//
//    override fun onViewDetachedFromWindow(holder: BaseViewHolder, item: ListBundle?, position: Int?) {
//        holder.findViewById<FrameLayout>(R.id.item_detail_container).removeAllViews()
//        super.onViewDetachedFromWindow(holder, item, position)
//    }
//
//    override fun onViewRecycled(holder: BaseViewHolder, item: ListBundle?, position: Int?) {
//        if (item != null && item is ListBundle_AdBannerYandex) {
//            if (item.bannerAdView != null) {
//                UtilKLogWrapper.d(TAG, "onViewRecycled: item $item")
//                item.bannerAdView = null
//            }
//        }
//        holder.findViewById<FrameLayout>(R.id.item_detail_container).applyGone()
//        super.onViewRecycled(holder, item, position)
//    }
//}