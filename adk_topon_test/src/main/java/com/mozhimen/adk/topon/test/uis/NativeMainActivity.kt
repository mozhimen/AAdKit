package com.mozhimen.adk.topon.test.uis

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import com.mozhimen.adk.topon.basic.test.R
import com.mozhimen.adk.topon.basic.test.databinding.ActivityNativeMainBinding
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB

/**
 * @ClassName NativeMainActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

class NativeMainActivity : BaseActivityVDB<ActivityNativeMainBinding>(), View.OnClickListener {
    override fun initFlag() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun initView(savedInstanceState: Bundle?) {
        vdb.titleBar.setTitle(R.string.anythink_title_native)
        vdb.titleBar.setListener { finish() }
        vdb.nativeBtn.setOnClickListener(this)
        vdb.nativeExpressBtn.setOnClickListener(this)
        vdb.nativeListBtn.setOnClickListener(this)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.nativeBtn -> {
                val intent1 = Intent(this@NativeMainActivity, com.mozhimen.adk.topon.test.uis.NativeAdActivity::class.java)
                intent1.putExtra("native_type", com.mozhimen.adk.topon.test.uis.NativeAdActivity.NATIVE_SELF_RENDER_TYPE)
                startActivity(intent1)
            }

            R.id.nativeExpressBtn -> {
                val intent2 = Intent(this@NativeMainActivity, com.mozhimen.adk.topon.test.uis.NativeAdActivity::class.java)
                intent2.putExtra("native_type", com.mozhimen.adk.topon.test.uis.NativeAdActivity.NATIVE_EXPRESS_TYPE)
                startActivity(intent2)
            }

            R.id.nativeListBtn -> {
                val intent3 = Intent(this@NativeMainActivity, com.mozhimen.adk.topon.test.uis.NativeListActivity::class.java)
                startActivity(intent3)
            }
        }
    }
}
