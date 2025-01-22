package com.mozhimen.adk.basic.bases

import android.app.Activity
import android.app.Application
import androidx.annotation.CallSuper
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.adk.basic.commons.IAdKOpenProxy
import com.mozhimen.kotlin.elemk.android.app.bases.BaseActivityLifecycleCallbacks
import com.mozhimen.kotlin.elemk.kotlin.properties.VarProperty_Set
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.lintk.optins.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.taskk.handler.TaskKIdleHandler
import com.mozhimen.kotlin.utilk.commons.IUtilK
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
abstract class BaseAdKOpenAdMgr2(private val _keyWord: String) : IUtilK {
    //    private val _processLifecycleObserver = ActivityLifecycleObserver()
    private val _appOpenAdActivityObserver by lazy { ActivityLifecycleCallbacks() }

    protected val _autoShowOpenAd = AtomicBoolean(false)

    protected var _activityRef: WeakReference<Activity>? by VarProperty_Set(null) { _, value ->
        UtilKLogWrapper.d(TAG, "_activityRef: ")
        if (value?.get() != null && _isAdLoad) {
            showAppOpenAd(value.get()!!)
        }
        true
    }

    protected var _isAdLoad by VarProperty_Set(false) { _, value ->
        UtilKLogWrapper.d(TAG, "_isAdLoad: ")
        if (value && _activityRef?.get() != null) {
            showAppOpenAd(_activityRef!!.get()!!)
        }
        true
    }

    //////////////////////////////////////////////////////////////////////////////

    abstract fun getAdkOpenProxy(): IAdKOpenProxy

    protected abstract fun initOpenAdProxy(adUnitId: String)

    //////////////////////////////////////////////////////////////////////////////

    @CallSuper
    open fun init(application: Application) {
        UtilKLogWrapper.d(TAG, "init: 开屏广告初始化")
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

    protected open fun showAppOpenAd(activity: Activity) {
        TaskKIdleHandler.addTask {
            if (_autoShowOpenAd.compareAndSet(false, true)) {
                getAdkOpenProxy().showOpenAd(activity)
            }
        }.start()
    }

    private inner class ActivityLifecycleCallbacks : BaseActivityLifecycleCallbacks() {
        override fun onActivityStarted(activity: Activity) {
            // example of choosing specific Activity for show AppOpenAd
            UtilKLogWrapper.d(TAG, "onActivityStarted activity ${activity.javaClass.simpleName}")
            if (activity.javaClass.simpleName.contains(_keyWord)) {
                _activityRef = WeakReference(activity)
            }
        }
    }
}