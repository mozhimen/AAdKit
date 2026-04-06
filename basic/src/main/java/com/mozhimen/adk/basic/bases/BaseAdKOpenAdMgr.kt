package com.mozhimen.adk.basic.bases

import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_InApplication
import com.mozhimen.kotlin.lintk.optins.api.OApiUse_BaseApplication
import com.mozhimen.kotlin.utilk.commons.IUtilK
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @ClassName BaseAdKOpenAdMgr
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/11
 * @Version 1.0
 */
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
open class BaseAdKOpenAdMgr : IUtilK {
    //    private val _processLifecycleObserver = ActivityLifecycleObserver()
//    private val _appOpenAdActivityObserver = ActivityLifecycleCallbacks()

    protected val _showOpenAd = AtomicBoolean(false)

    //    private var _activityRef: WeakReference<Activity>? = null

    //////////////////////////////////////////////////////////////////////////////

    init {
        // observe Process lifecycle to choose moment for AppOpenAd show
//        ProcessLifecycleOwner.get().lifecycle.addObserver(_processLifecycleObserver)
        // observe Activity Callbacks to check if particular activity is started
//        application.registerActivityLifecycleCallbacks(_appOpenAdActivityObserver)

    }

    fun init() {
        UtilKLogWrapper.d(TAG, "init: ")
    }

    //////////////////////////////////////////////////////////////////////////////


//    private inner class ActivityLifecycleObserver : DefaultLifecycleObserver {
//        override fun onStart(owner: LifecycleOwner) {
//            _activityRef?.get()?.let {
//                _adkYandexOpenProxy.showAppOpenAd(it)
//            }
//        }
//    }

//    private inner class ActivityLifecycleCallbacks : BaseActivityLifecycleCallbacks() {
//        override fun onActivityStarted(activity: Activity) {
//            // example of choosing specific Activity for show AppOpenAd
//            _activityRef = WeakReference(activity)
//        }
//    }
}