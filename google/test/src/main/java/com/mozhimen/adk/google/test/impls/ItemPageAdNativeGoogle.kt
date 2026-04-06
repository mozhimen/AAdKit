package com.mozhimen.adk.google.test.impls

import android.graphics.Color
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MediaContent
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.mozhimen.adk.google.commons.INativeAdLoadedListener
import com.mozhimen.adk.google.impls.AdKGoogleNativeProxy
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.view.applyMargin
import com.mozhimen.pagingk.paging3.data.bases.BasePagingKVHKProvider
import com.mozhimen.xmlk.vhk.VHKLifecycle2

/**
 * @ClassName ItemDetailPanel
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/11/29 21:57
 * @Version 1.0
 */
//class ItemPageAdNativeGoogle : BasePagingKVHKProvider<PageBundle>() {
//    override val itemViewType: Int
//        get() = CItemExtras.ITEM_PAGE_AD_NATIVE_GOOGLE_CODE
//    override val layoutId: Int
//        get() = R.layout.item_recycler_vertical_container
//
//    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
//    override fun onBindViewHolder(holder: VHK, item: PageBundle?, position: Int) {
//        super.onBindViewHolder(holder, item, position)
//        if (item is PageBundle) {
//            holder.findViewById<FrameLayout>(R.id.item_other_container).applyMargin(11f.dp2px.toInt(), 23f.dp2px.toInt())
//
//            val adKGoogleNativeProxy = AdKGoogleNativeProxy()
//            adKGoogleNativeProxy.apply {
//                initNativeAdParams(com.ty.lelejoy.common.BuildConfig.gms_ad_placement_id_native)
//                initNativeAdListener(
//                    object : AdListener() {
//                        override fun onAdFailedToLoad(p0: LoadAdError) {
//                            holder.findViewById<FrameLayout>(R.id.item_other_container).applyMargin(0, 23f.dp2px.toInt())
//                        }
//                    },
//                    object : INativeAdLoadedListener {
//                        override fun onNativeAdViewLoad(
//                            nativeAd: NativeAd,
//                            icon: NativeAd.Image?,
//                            headline: String?,
//                            advertiser: String?,
//                            starRating: Double?,
//                            body: String?,
//                            mediaContent: MediaContent?,
//                            callToAction: String?,
//                            price: String?,
//                            store: String?
//                        ): NativeAdView? {
//                            val nativeAdView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_recycler_vertical_ad_native_google, null) as? NativeAdView?
//                            icon?.drawable?.let {
//                                nativeAdView?.findViewById<ImageView>(R.id.item_ad_native_img_thumbail)?.apply {
//                                    nativeAdView.iconView = this
//                                    loadImage(it)
//                                }
//                            }
//                            headline?.let {
//                                nativeAdView?.findViewById<TextView>(R.id.item_ad_native_txt_name)?.apply {
//                                    nativeAdView.headlineView = this
//                                    text = it
//                                    setBackgroundColor(Color.TRANSPARENT)
//                                }
//                            }
//                            body?.let {
//                                nativeAdView?.findViewById<TextView>(R.id.item_ad_native_txt_description)?.apply {
//                                    nativeAdView.bodyView = this
//                                    text = it
//                                    setBackgroundColor(Color.TRANSPARENT)
//                                }
//                            }
//                            callToAction?.let {
//                                nativeAdView?.findViewById<Button>(R.id.item_ad_native_btn_open)?.apply {
//                                    nativeAdView.callToActionView = this
////                                    text = it
//                                }
//                            }
//                            return nativeAdView
//                        }
//
//                        override fun onNativeAdViewLoaded(nativeAd: NativeAd) {
//                            adKGoogleNativeProxy.addNativeViewToContainer(holder.findViewById<FrameLayout>(R.id.item_other_container))
//                        }
//                    }, null
//                )
//                bindLifecycle(holder)
//            }
//        }
//    }
//
//    override fun onViewRecycled(holder: VHKLifecycle2, item: PageBundle?, position: Int?) {
//        holder.findViewById<FrameLayout>(R.id.item_other_container).removeAllViews()
//        super.onViewRecycled(holder, item, position)
//    }
//}