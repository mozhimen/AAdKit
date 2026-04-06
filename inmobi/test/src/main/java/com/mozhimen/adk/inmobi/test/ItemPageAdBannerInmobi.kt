//
//import android.content.Context
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import com.inmobi.ads.AdMetaInfo
//import com.inmobi.ads.InMobiBanner
//import com.inmobi.ads.listeners.BannerAdEventListener
//import com.mozhimen.adk.inmobi.impls.AdKInmobiBannerProxy
//import com.mozhimen.basick.animk.builder.utils.AnimKTypeUtil
//import com.mozhimen.kotlin.elemk.android.animation.BaseViewHolderAnimatorListenerAdapter
//import com.mozhimen.kotlin.elemk.androidx.recyclerview.OnScrollListenerIdleImpl
//import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
//import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
//import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
//import com.mozhimen.kotlin.utilk.android.util.dp2pxI
//import com.mozhimen.kotlin.utilk.android.view.addViewSafe_MATCH_MATCH
//import com.mozhimen.kotlin.utilk.android.view.applyGone
//import com.mozhimen.kotlin.utilk.android.view.applyMargin
//import com.mozhimen.kotlin.utilk.android.view.applyVisible
//import com.mozhimen.pagingk.paging3.data.bases.uis.BasePagingKVHKProvider
//import com.mozhimen.xmlk.vhk.VHKLifecycle2
//import com.ty.lelejoy.fun_widget.R
//import com.ty.lelejoy.fun_widget.cons.PageBundle
//import com.ty.lelejoy.fun_widget.cons.CItemExtras
//import com.ty.lelejoy.fun_widget.cons.PageBundle_AdBannerInmobi
//import java.lang.ref.WeakReference
//
///**
// * @ClassName ItemRecyclerVerticalAdBanner
// * @Description TODO
// * @Author Mozhimen / Kolin Zhao
// * @Date 2024/2/28 0:22
// * @Version 1.0
// */
//class ItemPageAdBannerInmobi : BasePagingKVHKProvider<PageBundle, VHKLifecycle2>() {
//    override val itemViewType: Int
//        get() = CItemExtras.ITEM_PAGE_AD_BANNER_INMOBI_CODE
//
//    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VHKLifecycle2 {
//        return VHKLifecycle2(parent, R.layout.item_recycler_vertical_ad_banner)
//    }
//
//    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
//    override fun onBindViewHolder(holder: VHKLifecycle2, item: PageBundle?, position: Int) {
//        super.onBindViewHolder(holder, item, position)
//        if (item is PageBundle_AdBannerInmobi) {
//            val container = holder.findViewById<FrameLayout>(R.id.item_ad_container)
//            container.applyMargin(0, 0)
//
//            val adKInmobiBannerProxy = AdKInmobiBannerProxy()
//            adKInmobiBannerProxy.apply {
//                initBannerAdListener(object : BannerAdEventListener() {
//                    override fun onAdLoadSucceeded(p0: InMobiBanner, p1: AdMetaInfo) {
//                        val runBlock = {
//                            item.bannerAdView = adKInmobiBannerProxy.bannerAdView
//                            adKInmobiBannerProxy.addBannerViewToContainer(container)
//                            container.apply {
//                                applyVisible()
//                                AnimKTypeUtil.get_ofHeight(this, 0, 50f.dp2pxI()).addAnimatorListener(BaseViewHolderAnimatorListenerAdapter(holder)).build().start()
//                                AnimKTypeUtil.get_ofMarginVertical(this, 0, 0, 5f.dp2pxI()).addAnimatorListener(BaseViewHolderAnimatorListenerAdapter(holder)).build().start()
//                            }
//                        }
//                        adapter?.recyclerView?.apply {
//                            addOnScrollListener(OnScrollListenerIdleImpl(WeakReference(this)) { runBlock.invoke() }
//                                .also { container.setTag("listener".hashCode(), it) })
//                        } ?: kotlin.run {
//                            runBlock.invoke()
//                        }
//                    }
//                })
//                initBannerAdSize(320f.dp2pxI(), 50f.dp2pxI())
//                initBannerAdParams(com.ty.lelejoy.common.BuildConfig.inmobi_placement_id_banner)
//                bindLifecycle(holder)
//            }
//        }
//    }
//
//    override fun onViewAttachedToWindow(holder: VHKLifecycle2, item: PageBundle?, position: Int?) {
//        super.onViewAttachedToWindow(holder, item, position)
//        if (item != null && item is PageBundle_AdBannerInmobi && item.bannerAdView != null) {
//            holder.findViewById<FrameLayout>(R.id.item_ad_container).addViewSafe_MATCH_MATCH(item.bannerAdView!!)
//        }
//    }
//
//
//    override fun onViewDetachedFromWindow(holder: VHKLifecycle2, item: PageBundle?, position: Int?) {
//        holder.findViewById<FrameLayout>(R.id.item_ad_container).removeAllViews()
//        super.onViewDetachedFromWindow(holder, item, position)
//    }
//
//    override fun onViewRecycled(holder: VHKLifecycle2, item: PageBundle?, position: Int?) {
//        if (item != null && item is PageBundle_AdBannerInmobi && item.bannerAdView != null) {
//            item.bannerAdView = null
//        }
//        holder.findViewById<FrameLayout>(R.id.item_ad_container).apply {
//            applyMargin(0, 0)
//            applyGone()
//        }
//        super.onViewRecycled(holder, item, position)
//    }
//}