package com.mozhimen.adk.google.impls

import android.app.Application
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.lintk.optins.OApiInit_InApplication
import com.mozhimen.basick.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.basick.stackk.cb.StackKCb
import com.mozhimen.basick.utilk.commons.IUtilK
import java.util.concurrent.atomic.AtomicBoolean

@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKGoogleOpenAdMgr(application: Application, private val _keyWord: String, adUnitId: String) : IUtilK {
    //    private val _processLifecycleObserver = ActivityLifecycleObserver()
//    private val _appOpenAdActivityObserver = ActivityLifecycleCallbacks()

    private val _showOpenAd = AtomicBoolean(false)

    @OptIn(OApiCall_BindLifecycle::class)
    private val _adkGoogleOpenProxy by lazy { AdKGoogleOpenProxy() }

//    private var _activityRef: WeakReference<Activity>? = null

    //////////////////////////////////////////////////////////////////////////////

    init {
        // observe Process lifecycle to choose moment for AppOpenAd show
//        ProcessLifecycleOwner.get().lifecycle.addObserver(_processLifecycleObserver)
        // observe Activity Callbacks to check if particular activity is started
//        application.registerActivityLifecycleCallbacks(_appOpenAdActivityObserver)

        _adkGoogleOpenProxy.apply {
            initOpenAdListener(object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    if (_showOpenAd.compareAndSet(false, true)) {
                        StackKCb.instance.getStackTopActivity()?.let {
                            _adkGoogleOpenProxy.showOpenAd(it)
                        }
                    }
                }
            }, null)
            initOpenAdParams(adUnitId)
            bindLifecycle(ProcessLifecycleOwner.get())
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    fun init() {
        Log.d(TAG, "init: ")
    }

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
