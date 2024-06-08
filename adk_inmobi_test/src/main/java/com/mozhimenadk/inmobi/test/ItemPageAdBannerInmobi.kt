//
//import android.content.Context
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import com.inmobi.ads.AdMetaInfo
//import com.inmobi.ads.InMobiBanner
//import com.inmobi.ads.listeners.BannerAdEventListener
//import com.mozhimen.adk.inmobi.impls.AdKInmobiBannerProxy
//import com.mozhimen.basick.animk.builder.utils.AnimKTypeUtil
//import com.mozhimen.basick.elemk.android.animation.BaseViewHolderAnimatorListenerAdapter
//import com.mozhimen.basick.elemk.androidx.recyclerview.OnScrollListenerIdleImpl
//import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
//import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
//import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
//import com.mozhimen.basick.utilk.android.util.dp2pxI
//import com.mozhimen.basick.utilk.android.view.addViewSafe_ofMatchParent
//import com.mozhimen.basick.utilk.android.view.applyGone
//import com.mozhimen.basick.utilk.android.view.applyMargin
//import com.mozhimen.basick.utilk.android.view.applyVisible
//import com.mozhimen.pagingk.paging3.data.bases.BasePagingKVHKProvider
//import com.mozhimen.xmlk.vhk.VHKRecycler
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
//class ItemPageAdBannerInmobi : BasePagingKVHKProvider<PageBundle, VHKRecycler>() {
//    override val itemViewType: Int
//        get() = CItemExtras.ITEM_PAGE_AD_BANNER_INMOBI_CODE
//
//    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VHKRecycler {
//        return VHKRecycler(parent, R.layout.item_recycler_vertical_ad_banner)
//    }
//
//    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
//    override fun onBindViewHolder(holder: VHKRecycler, item: PageBundle?, position: Int) {
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
//                initBannerAdParams(com.ty.lelejoy.module_common.BuildConfig.inmobi_placement_id_banner)
//                bindLifecycle(holder)
//            }
//        }
//    }
//
//    override fun onViewAttachedToWindow(holder: VHKRecycler, item: PageBundle?, position: Int?) {
//        super.onViewAttachedToWindow(holder, item, position)
//        if (item != null && item is PageBundle_AdBannerInmobi && item.bannerAdView != null) {
//            holder.findViewById<FrameLayout>(R.id.item_ad_container).addViewSafe_ofMatchParent(item.bannerAdView!!)
//        }
//    }
//
//
//    override fun onViewDetachedFromWindow(holder: VHKRecycler, item: PageBundle?, position: Int?) {
//        holder.findViewById<FrameLayout>(R.id.item_ad_container).removeAllViews()
//        super.onViewDetachedFromWindow(holder, item, position)
//    }
//
//    override fun onViewRecycled(holder: VHKRecycler, item: PageBundle?, position: Int?) {
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