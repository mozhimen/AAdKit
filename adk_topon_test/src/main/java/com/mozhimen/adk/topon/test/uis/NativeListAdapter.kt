package com.mozhimen.adk.topon.test.uis

import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATSDK
import com.anythink.core.api.AdError
import com.anythink.nativead.api.ATNative
import com.anythink.nativead.api.ATNativeAdView
import com.anythink.nativead.api.ATNativeDislikeListener
import com.anythink.nativead.api.ATNativeEventListener
import com.anythink.nativead.api.ATNativeNetworkListener
import com.anythink.nativead.api.ATNativePrepareInfo
import com.anythink.nativead.api.ATNativeView
import com.anythink.nativead.api.NativeAd
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.test.mos.RecycleViewDataBean
import com.mozhimen.adk.topon.basic.test.utils.SelfRenderViewUtil
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.commons.IUtilK
import java.util.concurrent.ConcurrentHashMap

/**
 * @ClassName NativeListAdapter
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

class NativeListAdapter(data: MutableList<com.mozhimen.adk.topon.test.mos.RecycleViewDataBean>, onNativeListCallback: OnNativeListCallback?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>(), IUtilK {
    private var mData: MutableList<com.mozhimen.adk.topon.test.mos.RecycleViewDataBean>?
    val limitAdSize = 20
    private val mNativeAdBeanList: MutableList<com.mozhimen.adk.topon.test.mos.RecycleViewDataBean>
    private var mOnNativeListCallback: OnNativeListCallback?
    private var mATNative: ATNative? = null
    var mImpressionAdMap: ConcurrentHashMap<String, NativeAd>

    init {
        mData = data
        mImpressionAdMap = ConcurrentHashMap()
        mOnNativeListCallback = onNativeListCallback
        mNativeAdBeanList = ArrayList<com.mozhimen.adk.topon.test.mos.RecycleViewDataBean>(5)
    }

    fun setNativeAdHandler(nativeAdHandler: ATNative?) {
        mATNative = nativeAdHandler
    }

    fun addData(data: List<com.mozhimen.adk.topon.test.mos.RecycleViewDataBean>?) {
        if (data != null) {
            val oldSize = mData!!.size
            val InsertSize = data.size
            mData!!.addAll(data)
            this.notifyItemRangeChanged(oldSize, InsertSize)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, itemViewType: Int): RecyclerView.ViewHolder {
        return when (itemViewType) {
            TYPE_AD -> onCreateAdViewHolder(viewGroup)
            TYPE_MORE -> onCreateMoreViewHolder(viewGroup)
            TYPE_DATA -> onCreateDataViewHolder(viewGroup)
            else -> onCreateDataViewHolder(viewGroup)
        }
    }

    private fun onCreateAdViewHolder(viewGroup: ViewGroup): AdViewHolder {
        UtilKLogWrapper.i(TAG, "onCreateAdViewHolder: create adView")
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.native_list_ad_item, viewGroup, false)
        return AdViewHolder(view)
    }

    private fun onCreateDataViewHolder(viewGroup: ViewGroup): DataViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.native_list_data_item, viewGroup, false)
        return DataViewHolder(view)
    }

    private fun onCreateMoreViewHolder(viewGroup: ViewGroup): MoreViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.native_list_more_item, viewGroup, false)
        return MoreViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        when (itemViewType) {
            TYPE_AD -> if (viewHolder is AdViewHolder) {
                UtilKLogWrapper.i(TAG, "onBindViewHolder:$viewHolder")
                onBindAdViewHolder(viewHolder, position)
            }

            TYPE_MORE -> if (viewHolder is MoreViewHolder) {
                onBindMoreViewHoler(viewHolder)
            }

            TYPE_DATA -> if (viewHolder is DataViewHolder) {
                onBindDataViewHolder(viewHolder, position)
            }

            else -> if (viewHolder is DataViewHolder) {
                onBindDataViewHolder(viewHolder, position)
            }
        }
    }

    /**
     * bind ad view,use nativeAd cache
     */
    private fun onBindAdViewHolder(viewHolder: AdViewHolder, position: Int) {
        UtilKLogWrapper.i(TAG, "onBindAdViewHolder")
        val recycleViewDataBean: com.mozhimen.adk.topon.test.mos.RecycleViewDataBean = mData!![position]

        //海外SDK处理
        if (!ATSDK.isCnSDK() && !recycleViewDataBean.isLoadingAd) {
            recycleViewDataBean.isLoadingAd = true
            mATNative!!.setAdListener(object : ATNativeNetworkListener {
                override fun onNativeAdLoaded() {
                    viewHolder.mAdContainerRoot.visibility = View.VISIBLE
                    recycleViewDataBean.isLoadingAd = false
                    val nativeAd = mATNative!!.nativeAd
                    recycleViewDataBean.nativeAd = nativeAd
                    bindNativeAdWithViewHolder(nativeAd, viewHolder, recycleViewDataBean, position)
                }

                override fun onNativeAdLoadFail(error: AdError) {
                    viewHolder.mAdContainerRoot.visibility = View.GONE
                    recycleViewDataBean.isLoadingAd = false
                }
            })
            mATNative!!.makeAdRequest()
            return
        }
        var hasUseNewAd = false
        if (recycleViewDataBean.nativeAd == null) {
            recycleViewDataBean.nativeAd = mATNative!!.nativeAd
            hasUseNewAd = true
        }
        if (hasUseNewAd) {
            UtilKLogWrapper.i(TAG, "start to request new ad object.")
            //It is judged that if a new Ad has been obtained or the advertisement cannot be obtained temporarily, the Ad will be loaded immediately
            mATNative!!.makeAdRequest()
        }

        //Controll the Ad Cache Size
        controlNativeAdCacheSize(recycleViewDataBean, hasUseNewAd)
        bindNativeAdWithViewHolder(recycleViewDataBean.nativeAd, viewHolder, recycleViewDataBean, position)
    }

    private fun bindNativeAdWithViewHolder(nativeAd: NativeAd?, viewHolder: AdViewHolder, recycleViewDataBean: com.mozhimen.adk.topon.test.mos.RecycleViewDataBean, position: Int) {
        if (nativeAd == null) {
            //Temporarily hide the item when the ad object is empty, and display it after the Ad is obtained
            UtilKLogWrapper.i(TAG, "onBindAdViewHolder: NativeAd is null, it would be gone now.")
            val param = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams
            viewHolder.itemView.visibility = View.GONE
            param.height = 0
            param.width = 0
            viewHolder.itemView.setLayoutParams(param)
        } else {
            //Show Ad
            UtilKLogWrapper.i(TAG, "onBindAdViewHolder: NativeAd exist, start to render view.")
            UtilKLogWrapper.i(TAG, "onBindAdViewHolder: RenderAd: " + nativeAd.adInfo.toString())
            viewHolder.itemView.visibility = View.VISIBLE
            val param = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams
            param.height = RelativeLayout.LayoutParams.WRAP_CONTENT
            param.width = RelativeLayout.LayoutParams.MATCH_PARENT
            viewHolder.itemView.setLayoutParams(param)
            val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.CENTER_HORIZONTAL
            viewHolder.mATNativeView.setLayoutParams(params)
            viewHolder.setCurrentRecycleViewDataBean(recycleViewDataBean)
            mImpressionAdMap[nativeAd.hashCode().toString()] = nativeAd
            renderAdView(nativeAd, viewHolder, position)
        }
    }

    //Simple implementation to limit the specified number of advertisement caches in a List to avoid excessive memory
    private fun controlNativeAdCacheSize(recycleViewDataBean: com.mozhimen.adk.topon.test.mos.RecycleViewDataBean, hasUseNewAd: Boolean) {
        if (hasUseNewAd) {
            mNativeAdBeanList.add(recycleViewDataBean)
        }
        if (mNativeAdBeanList.size > limitAdSize) {
            val removeBean: com.mozhimen.adk.topon.test.mos.RecycleViewDataBean? = mNativeAdBeanList[0]
            if (removeBean?.nativeAd != null) {
                //Remove the oldest Ad directly after the number is exceeded
                mNativeAdBeanList.removeAt(0)
                UtilKLogWrapper.i(TAG, "controlNativeAdCacheSize: Over Ad Size, Remove AD:" + removeBean.nativeAd.getAdInfo())
                removeBean.nativeAd.destory()
                removeBean.nativeAd = null
            }
        }
    }

    private fun renderAdView(nativeAd: NativeAd, adViewHolder: AdViewHolder, position: Int) {
        nativeAd.setNativeEventListener(object : ATNativeEventListener {
            override fun onAdImpressed(view: ATNativeAdView, entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "native ad onAdImpressed--------\n$entity")
            }

            override fun onAdClicked(view: ATNativeAdView, entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "native ad onAdClicked--------\n$entity")
            }

            override fun onAdVideoStart(view: ATNativeAdView) {
                UtilKLogWrapper.i(TAG, "native ad onAdVideoStart--------")
            }

            override fun onAdVideoEnd(view: ATNativeAdView) {
                UtilKLogWrapper.i(TAG, "native ad onAdVideoEnd--------")
            }

            override fun onAdVideoProgress(view: ATNativeAdView, progress: Int) {
                UtilKLogWrapper.i(TAG, "native ad onAdVideoProgress--------:$progress")
            }
        })
        nativeAd.setDislikeCallbackListener(object : ATNativeDislikeListener() {
            override fun onAdCloseButtonClick(view: ATNativeAdView, entity: ATAdInfo) {
                UtilKLogWrapper.i(TAG, "onAdCloseButtonClick: remove $position")
                removeAdView(position)
            }
        })
        try {
            UtilKLogWrapper.i(TAG, "native ad start to render ad------------- ")
            var nativePrepareInfo: ATNativePrepareInfo? = null
            adViewHolder.mATNativeView.removeAllViews()
            if (nativeAd.isNativeExpress) {
                //Ad rendering for templates
                adViewHolder.mATNativeView.layoutParams.height = 300f.dp2px.toInt()
                nativeAd.renderAdContainer(adViewHolder.mATNativeView, null)
                adViewHolder.mSelfRenderView.visibility = View.GONE
            } else {
                //Ad rendering for self-render
                adViewHolder.mATNativeView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                nativePrepareInfo = ATNativePrepareInfo()
                adViewHolder.mSelfRenderView.visibility = View.VISIBLE
                //                SelfRenderViewUtil.bindSelfRenderView(this, nativeAd.getAdMaterial(), selfRenderView, nativePrepareInfo, adViewHeight);
                SelfRenderViewUtil.bindSelfRenderView(adViewHolder.mATNativeView.context, nativeAd.adMaterial, adViewHolder.mSelfRenderView, nativePrepareInfo)
                nativeAd.renderAdContainer(adViewHolder.mATNativeView, adViewHolder.mSelfRenderView)
            }
            nativeAd.prepare(adViewHolder.mATNativeView, nativePrepareInfo)
            nativeAd.onResume()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * bind mock data
     */
    private fun onBindDataViewHolder(viewHolder: DataViewHolder, position: Int) {
        viewHolder.mTvData.setText(mData!![position].content)
    }

    private fun onBindMoreViewHoler(viewHolder: MoreViewHolder) {
        viewHolder.mView.setOnClickListener {
            if (mOnNativeListCallback != null) {
                mOnNativeListCallback!!.onClickLoadMore()
            }
        }
    }

    override fun getItemCount(): Int {
        return if (mData != null) mData!!.size + 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        if (position == mData!!.size) {
            return TYPE_MORE
        }
        val recycleViewDataBean: com.mozhimen.adk.topon.test.mos.RecycleViewDataBean = mData!![position]
        return when (recycleViewDataBean.dataType) {
            com.mozhimen.adk.topon.test.mos.RecycleViewDataBean.AD_DATA_TYPE -> TYPE_AD
            com.mozhimen.adk.topon.test.mos.RecycleViewDataBean.NORMAL_DATA_TYPE -> TYPE_DATA
            else -> TYPE_DATA
        }
    }

    fun removeAdView(position: Int) {
        mData!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun onResume() {
        val iterator: Iterator<NativeAd> = mImpressionAdMap.values.iterator()
        while (iterator.hasNext()) {
            val nativeAd = iterator.next()
            nativeAd.onResume()
            UtilKLogWrapper.i(TAG, "Ad View onResume:$nativeAd")
        }
    }

    fun onPause() {
        val iterator: Iterator<NativeAd> = mImpressionAdMap.values.iterator()
        while (iterator.hasNext()) {
            val nativeAd = iterator.next()
            nativeAd.onPause()
            UtilKLogWrapper.i(TAG, "Ad View onPause:$nativeAd")
        }
    }

    fun onDestroy() {
        if (mData != null) {
            UtilKLogWrapper.i(TAG, "Recycle Destory:" + mData!!.size)
            val dataBeanIterator: Iterator<com.mozhimen.adk.topon.test.mos.RecycleViewDataBean> = mData!!.iterator()
            while (dataBeanIterator.hasNext()) {
                val recycleViewDataBean: com.mozhimen.adk.topon.test.mos.RecycleViewDataBean = dataBeanIterator.next()
                if (recycleViewDataBean.nativeAd != null) {
                    recycleViewDataBean.nativeAd.destory()
                }
            }
            mData!!.clear()
            mData = null
        }
        mOnNativeListCallback = null
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is AdViewHolder) {
            UtilKLogWrapper.i(TAG, "Ad View recycled:" + holder.getLayoutPosition() + "---holder:" + holder.toString())
            val recycleViewDataBean: com.mozhimen.adk.topon.test.mos.RecycleViewDataBean? = holder.recycleViewDataBean
            if (recycleViewDataBean != null && recycleViewDataBean.nativeAd != null) {
                mImpressionAdMap.remove(java.lang.String.valueOf(recycleViewDataBean.nativeAd.hashCode()), recycleViewDataBean.nativeAd)
                if (holder.getAdapterPosition() == -1) {
                    recycleViewDataBean.nativeAd.destory()
                    mNativeAdBeanList.remove(recycleViewDataBean)
                } else {
                    recycleViewDataBean.nativeAd.onPause()
                }
            }
        }
    }

    class DataViewHolder internal constructor(var mView: View) : RecyclerView.ViewHolder(mView) {
        var mTvData: TextView

        init {
            mTvData = mView.findViewById<TextView>(R.id.tv_data)
        }
    }

    class AdViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mAdContainerRoot: View
        var mATNativeView: ATNativeView
        var mSelfRenderView: View
        var recycleViewDataBean: com.mozhimen.adk.topon.test.mos.RecycleViewDataBean? = null

        init {
            mAdContainerRoot = itemView.findViewById<View>(R.id.fl_ad_container_root)
            mATNativeView = itemView.findViewById<ATNativeView>(R.id.ad_container)
            mSelfRenderView = mATNativeView.findViewById<View>(R.id.self_render_view)
        }

        fun setCurrentRecycleViewDataBean(recycleViewDataBean: com.mozhimen.adk.topon.test.mos.RecycleViewDataBean?) {
            this.recycleViewDataBean = recycleViewDataBean
        }
    }

    class MoreViewHolder internal constructor(var mView: View) : RecyclerView.ViewHolder(mView) {
        var mTvMore: TextView

        init {
            mTvMore = itemView.findViewById<TextView>(R.id.tv_more)
        }
    }

    interface OnNativeListCallback {
        fun onClickLoadMore()
    }

    companion object {
        /**
         * data
         */
        private const val TYPE_DATA = 0

        /**
         * ad
         */
        private const val TYPE_AD = 1

        /**
         * load more data
         */
        private const val TYPE_MORE = 2
    }
}

