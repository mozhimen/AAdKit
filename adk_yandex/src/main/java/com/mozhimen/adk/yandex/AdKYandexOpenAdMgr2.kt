package com.mozhimen.adk.yandex

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import com.mozhimen.adk.yandex.bases.BaseAppOpenAdLoadCallback
import com.mozhimen.basick.elemk.android.app.bases.BaseActivityLifecycleCallbacks
import com.mozhimen.basick.elemk.kotlin.properties.VarProperty_Set
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.lintk.optins.OApiInit_InApplication
import com.mozhimen.basick.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.basick.utilk.commons.IUtilK
import com.yandex.mobile.ads.appopenad.AppOpenAd
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

@OptIn(OApiCall_BindLifecycle::class)
@OApiInit_InApplication
@OApiInit_ByLazy
@OApiUse_BaseApplication
class AdKYandexOpenAdMgr2(application: Application, private val _keyWord: String, adUnitId: String) : IUtilK {
    //    private val _processLifecycleObserver = ActivityLifecycleObserver()
    private val _appOpenAdActivityObserver = ActivityLifecycleCallbacks()

    private val _atoShowOpenAd = AtomicBoolean(false)

    @OptIn(OApiCall_BindLifecycle::class)
    private val _adkYandexOpenProxy by lazy { AdKYandexOpenProxy() }

    private var _activityRef: WeakReference<Activity>? by VarProperty_Set(null) { _, value ->
        if (value != null && _isAdLoad) {
            value.get()?.let {
                showAppOpenAd(it)
            }
        }
        true
    }

    private var _isAdLoad by VarProperty_Set(false) { _, value ->
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

        _adkYandexOpenProxy.apply {
            initOpenAdListener(object : BaseAppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    _isAdLoad = true
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

    //////////////////////////////////////////////////////////////////////////////

    private fun showAppOpenAd(activity: Activity) {
        if (_atoShowOpenAd.compareAndSet(false, true)) {
            _adkYandexOpenProxy.showAppOpenAd(activity)
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
