package com.mozhimen.adk.basic.bases

import android.app.Activity
import android.app.Application
import android.util.Log
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.basick.elemk.android.app.bases.BaseActivityLifecycleCallbacks
import com.mozhimen.basick.elemk.kotlin.properties.VarProperty_Set
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.lintk.optins.OApiInit_InApplication
import com.mozhimen.basick.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.basick.utilk.commons.IUtilK
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @ClassName BaseAdKOpenAdMgr2
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/2
 * @Version 1.0
 */
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
abstract class BaseAdKOpenAdMgr2(application: Application, private val _keyWord: String) : IUtilK {
    //    private val _processLifecycleObserver = ActivityLifecycleObserver()
    private val _appOpenAdActivityObserver = ActivityLifecycleCallbacks()

    private val _atoShowOpenAd = AtomicBoolean(false)

    private var _activityRef: WeakReference<Activity>? by VarProperty_Set(null) { _, value ->
        if (value != null && _isAdLoad) {
            value.get()?.let {
                showAppOpenAd(it)
            }
        }
        true
    }

    protected var _isAdLoad by VarProperty_Set(false) { _, value ->
        if (value && _activityRef != null) {
            _activityRef?.get()?.let {
                showAppOpenAd(it)
            }
        }
        true
    }

    //////////////////////////////////////////////////////////////////////////////

    init {
        // observe Process lifecycle to choose moment for AppOpenAd show
//        ProcessLifecycleOwner.get().lifecycle.addObserver(_processLifecycleObserver)
        // observe Activity Callbacks to check if particular activity is started
        application.registerActivityLifecycleCallbacks(_appOpenAdActivityObserver)

//        _adkYandexOpenProxy.apply {
//            initOpenAdListener(object : BaseAppOpenAdLoadCallback() {
//                override fun onAdLoaded(p0: AppOpenAd) {
//                    _isAdLoad = true
//                }
//            }, null)
//            initOpenAdParams(adUnitId)
//            bindLifecycle(ProcessLifecycleOwner.get())
//        }
    }

    //////////////////////////////////////////////////////////////////////////////

    abstract fun getAdkOpenProxy() : IAdKOpenProxy

    //////////////////////////////////////////////////////////////////////////////

    fun init() {
        Log.d(TAG, "init: 开屏广告初始化")
    }

    //////////////////////////////////////////////////////////////////////////////

    private fun showAppOpenAd(activity: Activity) {
        if (_atoShowOpenAd.compareAndSet(false, true)) {
            getAdkOpenProxy().showOpenAd(activity)
        }
    }

    private inner class ActivityLifecycleCallbacks : BaseActivityLifecycleCallbacks() {
        override fun onActivityStarted(activity: Activity) {
            // example of choosing specific Activity for show AppOpenAd
            if (activity.javaClass.simpleName.contains(_keyWord)) {
                _activityRef = WeakReference(activity)
            }
        }
    }
}