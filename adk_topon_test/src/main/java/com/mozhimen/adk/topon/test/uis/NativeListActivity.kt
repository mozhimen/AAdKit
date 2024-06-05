package com.mozhimen.adk.topon.test.uis

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anythink.core.api.AdError
import com.anythink.nativead.api.ATNative
import com.anythink.nativead.api.ATNativeNetworkListener
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.test.mos.RecycleViewDataBean
import com.mozhimen.adk.topon.basic.test.utils.PlacementIdUtil
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName NativeListActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

class NativeListActivity : Activity(), IUtilK {
    private var dataRecycleView: RecyclerView? = null
    private var mAdapter: com.mozhimen.adk.topon.test.uis.NativeListAdapter? = null
    private var mPage = -1
    private val mDataCountInPerPage = 12
    private var placementId: String? = null
    private var mATNative: ATNative? = null
    var nativeNetworkListener: ATNativeNetworkListener = object : ATNativeNetworkListener {
        override fun onNativeAdLoaded() {
            UtilKLogWrapper.i(TAG, "native ad onNativeAdLoaded------------- ")
        }

        override fun onNativeAdLoadFail(adError: AdError) {
            UtilKLogWrapper.e(TAG, "native ad onNativeAdLoadFail------------- " + adError.fullErrorInfo)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_list)

//        placementId = PlacementIdUtil.getNativePlacements(this).get("Toutiao");
//        placementId = PlacementIdUtil.getNativePlacements(this).get("All");
//        placementId = PlacementIdUtil.getNativePlacements(this).get("Mintegral");
//        placementId = PlacementIdUtil.getNativePlacements(this).get("GDT");
//        placementId = PlacementIdUtil.getNativePlacements(this).get("Toutiao Draw");
//        placementId = PlacementIdUtil.getNativePlacements(this).get("Baidu");
        placementId = PlacementIdUtil.getListPlacementId()
        initView()
        requestNativeAd()
        startRequestData()
        findViewById<View>(R.id.rv_native).visibility = View.VISIBLE
        findViewById<View>(R.id.pb).visibility = View.GONE
    }

    private fun startRequestData() {
        val data: MutableList<com.mozhimen.adk.topon.test.mos.RecycleViewDataBean> = createMockData()
        if (mAdapter == null) {
            mAdapter = _root_ide_package_.com.mozhimen.adk.topon.test.uis.NativeListAdapter(data, object : com.mozhimen.adk.topon.test.uis.NativeListAdapter.OnNativeListCallback {
                override fun onClickLoadMore() {
                    startRequestData()
                }
            })
            mAdapter!!.setNativeAdHandler(mATNative)
            dataRecycleView!!.adapter = mAdapter
        } else {
            mAdapter!!.addData(data)
        }
    }

    private fun initView() {
        dataRecycleView = findViewById<RecyclerView>(R.id.rv_native)
        val layoutManager = LinearLayoutManager(this@NativeListActivity, LinearLayoutManager.VERTICAL, false)
        dataRecycleView!!.setLayoutManager(layoutManager)
        //        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#444444")));
//        dataRecycleView.addItemDecoration(dividerItemDecoration);
    }

    private fun createMockData(): MutableList<com.mozhimen.adk.topon.test.mos.RecycleViewDataBean> {
        mPage++
        val data: MutableList<com.mozhimen.adk.topon.test.mos.RecycleViewDataBean> = ArrayList<com.mozhimen.adk.topon.test.mos.RecycleViewDataBean>()
        for (i in 0 until mDataCountInPerPage) {
            val recycleViewDataBean = com.mozhimen.adk.topon.test.mos.RecycleViewDataBean()
            if (i != 0 && (i + 1) % 6 == 0) {
                recycleViewDataBean.dataType = com.mozhimen.adk.topon.test.mos.RecycleViewDataBean.AD_DATA_TYPE
            } else {
                recycleViewDataBean.dataType = com.mozhimen.adk.topon.test.mos.RecycleViewDataBean.NORMAL_DATA_TYPE
                recycleViewDataBean.content = "Data: " + (mPage * mDataCountInPerPage + i)
            }
            data.add(recycleViewDataBean)
        }
        return data
    }

    override fun onDestroy() {
        if (mAdapter != null) {
            mAdapter!!.onDestroy()
            mAdapter = null
        }
        if (dataRecycleView != null) {
            dataRecycleView!!.adapter = null
            dataRecycleView = null
        }
        super.onDestroy()
    }

    override fun onPause() {
        mAdapter?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAdapter?.onResume()
        super.onResume()
    }

    // ----------------------------------------------------------------------------------------
    private fun requestNativeAd() {
        if (mATNative == null) {
            mATNative = ATNative(this, placementId, nativeNetworkListener)
        }

        //load ad
        mATNative!!.makeAdRequest()
        UtilKLogWrapper.i(TAG, "native ad start to load ad------------- ")
    }
}

