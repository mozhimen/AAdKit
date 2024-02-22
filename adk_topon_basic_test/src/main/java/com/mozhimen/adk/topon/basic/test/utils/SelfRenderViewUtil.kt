package com.mozhimen.adk.topon.basic.test.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.TextView
import com.anythink.nativead.api.ATNativeImageView
import com.anythink.nativead.api.ATNativeMaterial
import com.anythink.nativead.api.ATNativePrepareExInfo
import com.anythink.nativead.api.ATNativePrepareInfo
import com.anythink.nativead.unitgroup.api.CustomNativeAd
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.basic.test.widgets.MutiImageView

/**
 * @ClassName SelfRenderViewUtil
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

object SelfRenderViewUtil {
    private val TAG = SelfRenderViewUtil::class.java.getSimpleName()
    fun bindSelfRenderView(context: Context, adMaterial: ATNativeMaterial, selfRenderView: View, nativePrepareInfo: ATNativePrepareInfo?) {
        var nativePrepareInfo = nativePrepareInfo
        printNativeAdMaterial(adMaterial)
        val padding = dip2px(context, 5f)
        selfRenderView.setPadding(padding, padding, padding, padding)
        val titleView = selfRenderView.findViewById<TextView>(R.id.native_ad_title)
        val descView = selfRenderView.findViewById<TextView>(R.id.native_ad_desc)
        val ctaView = selfRenderView.findViewById<TextView>(R.id.native_ad_install_btn)
        val adFromView = selfRenderView.findViewById<TextView>(R.id.native_ad_from)
        val iconArea = selfRenderView.findViewById<FrameLayout>(R.id.native_ad_image)
        val contentArea = selfRenderView.findViewById<FrameLayout>(R.id.native_ad_content_image_area)
        val logoView = selfRenderView.findViewById<ATNativeImageView>(R.id.native_ad_logo)
        val closeView = selfRenderView.findViewById<View>(R.id.native_ad_close)
        val domainView = selfRenderView.findViewById<TextView>(R.id.native_ad_domain) //(v6.1.20+) Yandex domain
        val warningView = selfRenderView.findViewById<TextView>(R.id.native_ad_warning) //(v6.1.20+) Yandex warning
        val adLogoContainer = selfRenderView.findViewById<FrameLayout>(R.id.native_ad_logo_container) //v6.1.52+
        val advertiserIcon = selfRenderView.findViewById<TextView>(R.id.native_advertiser_icon) //v6.1.70+

        // bind view
        if (nativePrepareInfo == null) {
            nativePrepareInfo = ATNativePrepareInfo()
        }
        val clickViewList: MutableList<View> = ArrayList() //click views
        val title = adMaterial.title
        // title
        if (!TextUtils.isEmpty(title)) {
            titleView.text = title
            nativePrepareInfo.titleView = titleView //bind title
            clickViewList.add(titleView)
            titleView.visibility = View.VISIBLE
        } else {
            titleView.visibility = View.GONE
        }
        val descriptionText = adMaterial.descriptionText
        if (!TextUtils.isEmpty(descriptionText)) {
            // desc
            descView.text = descriptionText
            nativePrepareInfo.descView = descView //bind desc
            clickViewList.add(descView)
            descView.visibility = View.VISIBLE
        } else {
            descView.visibility = View.GONE
        }

        // icon
        val adIconView = adMaterial.adIconView
        val iconImageUrl = adMaterial.iconImageUrl
        iconArea.removeAllViews()
        val iconView = ATNativeImageView(context)
        if (adIconView != null) {
            iconArea.addView(adIconView)
            nativePrepareInfo.iconView = adIconView //bind icon
            clickViewList.add(adIconView)
            iconArea.visibility = View.VISIBLE
        } else if (!TextUtils.isEmpty(iconImageUrl)) {
            iconArea.addView(iconView)
            iconView.setImage(iconImageUrl)
            nativePrepareInfo.iconView = iconView //bind icon
            clickViewList.add(iconView)
            iconArea.visibility = View.VISIBLE
        } else {
            iconArea.visibility = View.INVISIBLE
        }

        // cta button
        val callToActionText = adMaterial.callToActionText
        if (!TextUtils.isEmpty(callToActionText)) {
            ctaView.text = callToActionText
            nativePrepareInfo.ctaView = ctaView //bind cta button
            clickViewList.add(ctaView)
            ctaView.visibility = View.VISIBLE
        } else {
            ctaView.visibility = View.GONE
        }

//        // AppDownloadButton(Only Huawei Ads support)
//        val lastView = (selfRenderView as ViewGroup).getChildAt(selfRenderView.childCount - 1)
//        // Remove AppDownloadButton since last time added
//        if (lastView is AppDownloadButton) {
//            selfRenderView.removeView(lastView)
//        }
//        val appDownloadButton = adMaterial.appDownloadButton
//        if (appDownloadButton != null) {
//            if (appDownloadButton is AppDownloadButton) {
//                (appDownloadButton as AppDownloadButton).setTextSize(dip2px(context, 12f))
//            }
//            val ctaParams = ctaView.layoutParams
//            selfRenderView.addView(appDownloadButton, ctaParams)
//            appDownloadButton.visibility = View.VISIBLE
//            ctaView.visibility = View.INVISIBLE
//        }

        // media view
        val mediaView = adMaterial.getAdMediaView(contentArea)
        val mainImageHeight = adMaterial.mainImageHeight
        val mainImageWidth = adMaterial.mainImageWidth
        val mainImageParam = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
        )
        if (mediaView == null) {
            val viewTreeObserver = selfRenderView.getViewTreeObserver()
            viewTreeObserver.addOnGlobalLayoutListener(
                object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // 移除监听器
                        selfRenderView.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                        val realMainImageWidth = selfRenderView.getWidth() - dip2px(
                            context,
                            10f
                        )
                        var realMainHeight = 0
                        if (mainImageWidth > 0 && mainImageHeight > 0 && mainImageWidth > mainImageHeight) {
                            realMainHeight = realMainImageWidth * mainImageHeight / mainImageWidth
                            mainImageParam.width = realMainImageWidth
                            mainImageParam.height = realMainHeight
                        } else {
                            mainImageParam.width = FrameLayout.LayoutParams.MATCH_PARENT
                            mainImageParam.height = realMainImageWidth * 600 / 1024
                        }
                    }
                })
        } else {
            var realMainImageWidth = context.resources
                .displayMetrics.widthPixels - dip2px(context, 10f)
            if (context.resources
                    .displayMetrics.widthPixels > context.resources
                    .displayMetrics.heightPixels
            ) { //Horizontal screen
                realMainImageWidth = context.resources
                    .displayMetrics.widthPixels - dip2px(context, 10f) - dip2px(
                    context,
                    330f
                ) - dip2px(context, 130f)
            }
            if (mainImageWidth > 0 && mainImageHeight > 0 && mainImageWidth > mainImageHeight) {
                mainImageParam.width = FrameLayout.LayoutParams.MATCH_PARENT
                mainImageParam.height = realMainImageWidth * mainImageHeight / mainImageWidth
            } else {
                mainImageParam.width = FrameLayout.LayoutParams.MATCH_PARENT
                mainImageParam.height = realMainImageWidth * 600 / 1024
            }
        }
        val imageList = adMaterial.imageUrlList
        contentArea.removeAllViews()
        if (mediaView != null) {
            if (mediaView.parent != null) {
                (mediaView.parent as ViewGroup).removeView(mediaView)
            }
            mainImageParam.gravity = Gravity.CENTER
            mediaView.setLayoutParams(mainImageParam)
            contentArea.addView(mediaView, mainImageParam)
            clickViewList.add(mediaView)
            contentArea.visibility = View.VISIBLE
        } else if (imageList != null && imageList.size > 1) {
            val mutiImageView = MutiImageView(context)
            mutiImageView.setImageList(imageList, mainImageWidth, mainImageHeight)
            nativePrepareInfo.mainImageView = mutiImageView //bind main image
            contentArea.addView(mutiImageView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            clickViewList.add(mutiImageView)
        } else if (!TextUtils.isEmpty(adMaterial.mainImageUrl)) {
            val imageView = ATNativeImageView(context)
            imageView.setImage(adMaterial.mainImageUrl)
            imageView.setLayoutParams(mainImageParam)
            contentArea.addView(imageView, mainImageParam)
            nativePrepareInfo.mainImageView = imageView //bind main image
            clickViewList.add(imageView)
            contentArea.visibility = View.VISIBLE
        } else {
            contentArea.removeAllViews()
            contentArea.visibility = View.GONE
        }


        //Ad Logo
        val adLogoView = adMaterial.adLogoView
        if (adLogoView != null) {
            adLogoContainer.visibility = View.VISIBLE
            adLogoContainer.removeAllViews()
            adLogoContainer.addView(adLogoView)
        } else {
            adLogoContainer.visibility = View.GONE
            val adChoiceIconUrl = adMaterial.adChoiceIconUrl
            val adLogoBitmap = adMaterial.adLogo
            if (!TextUtils.isEmpty(adChoiceIconUrl)) {
                logoView.setImage(adChoiceIconUrl)
                nativePrepareInfo.adLogoView = logoView //bind ad choice
                logoView.setVisibility(View.VISIBLE)
            } else if (adLogoBitmap != null) {
                logoView.setImageBitmap(adLogoBitmap)
                logoView.setVisibility(View.VISIBLE)
            } else {
                logoView.setImageBitmap(null)
                logoView.setVisibility(View.GONE)
            }
        }

        //ad from (v6.1.52+)
        val adFrom = adMaterial.adFrom
        if (!TextUtils.isEmpty(adFrom)) {
            adFromView.text = adFrom
            adFromView.visibility = View.VISIBLE
        } else {
            adFromView.visibility = View.GONE
        }
        nativePrepareInfo.adFromView = adFromView //bind ad from

        //advertiser info (v6.1.70+)
        val advertiserInfoOperate = adMaterial.advertiserInfoOperate
        if (advertiserInfoOperate == null) {
            //When the advertiserInfoOperate is null, hide the advertiser information icon.
            advertiserIcon.visibility = View.GONE
        } else {
            //When the advertiserInfoOperate is not null, show the advertiser information icon and
            //call the API to pull up the advertiser information pop-up box.
            advertiserIcon.visibility = View.VISIBLE
            advertiserIcon.setOnClickListener { advertiserInfoOperate.showAdvertiserInfoDialog(advertiserIcon, true) }
        }
        val layoutParams = FrameLayout.LayoutParams(dip2px(context, 40f), dip2px(context, 10f)) //ad choice
        layoutParams.gravity = Gravity.BOTTOM or Gravity.RIGHT
        nativePrepareInfo.choiceViewLayoutParams = layoutParams //bind layout params for ad choice
        nativePrepareInfo.closeView = closeView //bind close button
        val domain = adMaterial.domain //(v6.1.20+) Yandex domain
        if (!TextUtils.isEmpty(domain)) {
            domainView.visibility = View.VISIBLE
            domainView.text = domain
            clickViewList.add(domainView)
            nativePrepareInfo.domainView = domainView
        } else {
            domainView.visibility = View.GONE
        }
        val warning = adMaterial.warning //(v6.1.20+) Yandex warning
        if (!TextUtils.isEmpty(warning)) {
            warningView.visibility = View.VISIBLE
            warningView.text = warning
            clickViewList.add(warningView)
            nativePrepareInfo.warningView = warningView
        } else {
            warningView.visibility = View.GONE
        }
        nativePrepareInfo.clickViewList = clickViewList //bind click view list
        if (nativePrepareInfo is ATNativePrepareExInfo) {
            val creativeClickViewList: MutableList<View> = ArrayList() //click views
            creativeClickViewList.add(ctaView)
            nativePrepareInfo.creativeClickViewList = creativeClickViewList //bind custom view list
        }
    }

    //    private static View initializePlayer(Context context, String url) {
    //        VideoView videoView = new VideoView(context);
    //        videoView.setVideoURI(Uri.parse(url));
    //        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
    //            @Override
    //            public void onPrepared(MediaPlayer mediaPlayer) {
    //            }
    //        });
    //        videoView.start();
    //
    //        return videoView;
    //    }
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    private fun printNativeAdMaterial(adMaterial: ATNativeMaterial?) {
        if (adMaterial == null) return
        val adType = adMaterial.adType
        when (adType) {
            CustomNativeAd.NativeAdConst.VIDEO_TYPE -> Log.i(TAG, "Ad source type: Video" + ", video duration: " + adMaterial.videoDuration)
            CustomNativeAd.NativeAdConst.IMAGE_TYPE -> Log.i(TAG, "Ad source type: Image")
            else -> Log.i(TAG, "Ad source type: Unknown")
        }
        when (adMaterial.nativeType) {
            CustomNativeAd.NativeType.FEED -> Log.i(TAG, "Native type: Feed")
            CustomNativeAd.NativeType.PATCH -> Log.i(TAG, "Native type: Patch")
        }
        Log.i(
            TAG, """
     show native material:
     adMaterial:$adMaterial
     getTitle:${adMaterial.title}
     getDescriptionText:${adMaterial.descriptionText}
     getNativeType:${adMaterial.nativeType}
     getAdMediaView:${adMaterial.getAdMediaView()}
     getAdIconView:${adMaterial.adIconView}
     getIconImageUrl:${adMaterial.iconImageUrl}
     getMainImageUrl:${adMaterial.mainImageUrl}
     getMainImageWidth:${adMaterial.mainImageWidth}
     getMainImageHeight:${adMaterial.mainImageHeight}
     getVideoWidth:${adMaterial.videoWidth}
     getVideoHeight:${adMaterial.videoHeight}
     getAppPrice:${adMaterial.appPrice}
     getAppCommentNum:${adMaterial.appCommentNum}
     getCallToActionText:${adMaterial.callToActionText}
     getStarRating:${adMaterial.starRating}
     getVideoUrl:${adMaterial.videoUrl}
     getAdChoiceIconUrl:${adMaterial.adChoiceIconUrl}
     getAdFrom:${adMaterial.adFrom}
     getImageUrlList:${adMaterial.imageUrlList}
     getNetworkInfoMap:${adMaterial.networkInfoMap}
     getAdAppInfo:${adMaterial.adAppInfo}
     getNativeAdInteractionType:${adMaterial.nativeAdInteractionType}
     getVideoDuration:${adMaterial.videoDuration}
     getAdvertiserName:${adMaterial.advertiserName}
     getNativeType:${adMaterial.nativeType}
     getAdType:${adMaterial.adType}
     getNativeCustomVideo:${adMaterial.nativeCustomVideo}
     getAdLogo:${adMaterial.adLogo}
     getNativeExpressWidth:${adMaterial.nativeExpressWidth}
     getNativeExpressHeight${adMaterial.nativeExpressHeight}
     
     """.trimIndent()
        )
    }
}

