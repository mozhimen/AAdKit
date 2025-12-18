package com.mozhimen.adk.topon.test.uis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.adk.topon.test.R

/**
 * @ClassName NativeVideoButtonAdapter
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

class NativeVideoButtonAdapter(data: MutableList<String>?, callback: OnNativeVideoButtonCallback?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val mData: MutableList<String>?
    private val mOnNativeVideoButtonCallback: OnNativeVideoButtonCallback?

    init {
        mData = data
        mOnNativeVideoButtonCallback = callback
    }

    fun addData(data: List<String>?) {
        if (data != null) {
            val oldSize = mData!!.size
            val InsertSize = data.size
            mData.addAll(data)
            this.notifyItemRangeChanged(oldSize, InsertSize)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return onCreateDataViewHolder(viewGroup)
    }

    private fun onCreateDataViewHolder(viewGroup: ViewGroup): ButtonViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.native_video_button_item, viewGroup, false)
        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        onBindDataViewHolder(viewHolder as ButtonViewHolder, position)
    }

    private fun onBindDataViewHolder(viewHolder: ButtonViewHolder, position: Int) {
        if (mData != null && mData.size != 0) {
            viewHolder.btAction.text = mData[position]
            viewHolder.mView.setOnClickListener { mOnNativeVideoButtonCallback?.onClick(mData[position]) }
        }
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    class ButtonViewHolder internal constructor(var mView: View) : RecyclerView.ViewHolder(mView) {
        var btAction: Button

        init {
            btAction = mView.findViewById<Button>(R.id.bt_action)
        }
    }

    interface OnNativeVideoButtonCallback {
        fun onClick(action: String?)
    }
}