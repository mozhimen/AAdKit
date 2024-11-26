package com.mozhimen.adk.topon.test.impls

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.nativead.api.ATNativeAdView
import com.anythink.nativead.api.ATNativeMaterial
import com.anythink.nativead.api.ATNativePrepareInfo
import com.anythink.nativead.api.NativeAd
import com.chad.library.adapter3.provider.BaseItemProvider
import com.chad.library.adapter3.viewholder.BaseViewHolder
import com.mozhimen.adk.topon.basic.bases.BaseATAdSourceStatusCallback
import com.mozhimen.adk.topon.basic.bases.BaseATNativeNetworkCallback
import com.mozhimen.adk.topon.basic.commons.INativeAdLoadedListener
import com.mozhimen.adk.topon.basic.impls.AdKTopOnNativeProxy
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.view.applyMargin
import com.ty.lelejoy.fun_data.utils.loadImage
import com.ty.lelejoy.fun_widget.R
import com.ty.lelejoy.common.mos.ListBundle
import com.ty.lelejoy.fun_widget.cons.CItemExtras

/**
 * @ClassName ItemDetailPanel
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/11/29 21:57
 * @Version 1.0
 */
class ItemListAdNativeTopOn : BaseItemProvider<ListBundle>() {
    override val itemViewType: Int
        get() = CItemExtras.ITEM_LIST_AD_NATIVE_TOPON
    override val layoutId: Int
        get() = R.layout.item_recycler_vertical_container

    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    override fun onBindViewHolder(holder: BaseViewHolder, item: ListBundle, position: Int?) {
        super.onBindViewHolder(holder, item, position)
        holder.findViewById<FrameLayout>(R.id.item_other_container).applyMargin(11f.dp2px.toInt(), 23f.dp2px.toInt())

        val adKGoogleNativeProxy = AdKTopOnNativeProxy()
        adKGoogleNativeProxy.apply {
            initNativeAdParams(com.ty.lelejoy.common.BuildConfig.topon_placement_id_native, "")
            initNativeAdListener(
                object : BaseATNativeNetworkCallback() {
                    override fun onNativeAdLoadFail(p0: AdError?) {
                        holder.findViewById<FrameLayout>(R.id.item_other_container).applyMargin(0, 23f.dp2px.toInt())
                    }
                },
                object : INativeAdLoadedListener {
                    override fun onNativeAdViewLoad(nativeAd: NativeAd?, adMaterial: ATNativeMaterial?, atNativePrepareInfo: ATNativePrepareInfo): Pair<ATNativeAdView?, View?>? {
                        val nativeAdView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_recycler_vertical_ad_native_topon, null) as? ATNativeAdView?
                        val selfRenderView = nativeAdView?.findViewById<View>(R.id.item_ad_native_view_include)
                        adMaterial?.iconImageUrl?.let {
                            selfRenderView?.findViewById<ImageView>(R.id.item_ad_native_img_thumbail)?.apply {
                                atNativePrepareInfo.iconView = this
                                loadImage(it)
                            }
                        }
                        adMaterial?.title?.let {
                            selfRenderView?.findViewById<TextView>(R.id.item_ad_native_txt_name)?.apply {
                                atNativePrepareInfo.titleView = this
                                text = it
                                setBackgroundColor(Color.TRANSPARENT)
                            }
                        }
                        adMaterial?.descriptionText?.let {
                            selfRenderView?.findViewById<TextView>(R.id.item_ad_native_txt_description)?.apply {
                                atNativePrepareInfo.descView = this
                                text = it
                                setBackgroundColor(Color.TRANSPARENT)
                            }
                        }
                        adMaterial?.callToActionText?.let {
                            selfRenderView?.findViewById<Button>(R.id.item_ad_native_btn_open)?.apply {
                                atNativePrepareInfo.ctaView = this
                                text = it
                            }
                        }
                        return nativeAdView to selfRenderView
                    }

                    override fun onNativeAdViewLoaded(nativeAd: NativeAd?, videoFuns: List<String>?) {
                        adKGoogleNativeProxy.addNativeViewToContainer(holder.findViewById<FrameLayout>(R.id.item_other_container))
                    }
                },
                object : BaseATAdSourceStatusCallback() {
                    override fun onAdSourceBiddingFail(adInfo: ATAdInfo, adError: AdError?) {

                    }

                    override fun onAdSourceLoadFail(adInfo: ATAdInfo, adError: AdError) {

                    }
                },
                null, null
            )
            bindLifecycle(holder)
        }
    }

    override fun onViewRecycled(holder: BaseViewHolder, item: ListBundle?, position: Int?) {
        holder.findViewById<FrameLayout>(R.id.item_other_container).removeAllViews()
        super.onViewRecycled(holder, item, position)
    }
}