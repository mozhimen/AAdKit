package com.mozhimen.adk.yandex

import android.app.Activity
import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.mozhimen.basick.BuildConfig
import com.mozhimen.basick.elemk.android.app.bases.BaseActivityLifecycleCallbacks
import com.mozhimen.basick.elemk.commons.I_Listener
import com.mozhimen.basick.utilk.android.widget.showToast
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.common.AdRequestError
import java.lang.ref.WeakReference

//class AppOpenAdManager<A : Activity>(application: Application) {
//    private val _processLifecycleObserver = ActivityLifecycleObserver(_onForeground = ::showAppOpenAd)
//    private val _appOpenAdActivityObserver = ActivityLifecycleCallbacks()
//    private var _activityRef: WeakReference<Activity>? = null
//    private var _appOpenAdLoadListener: AppOpenAdLoadListener = object : AppOpenAdLoadListener {
//        override fun onAdLoaded(p0: AppOpenAd) {
//            if (BuildConfig.DEBUG)
//                "AppOpenAd loaded".showToast()
//        }
//
//        override fun onAdFailedToLoad(p0: AdRequestError) {
//            if (BuildConfig.DEBUG)
//                "AppOpenAd failed to load".showToast()
//        }
//    }
//
//    //////////////////////////////////////////////////////////////////////////////
//
//    init {
//        // observe Process lifecycle to choose moment for AppOpenAd show
//        ProcessLifecycleOwner.get().lifecycle.addObserver(_processLifecycleObserver)
//        // observe Activity Callbacks to check if particular activity is started
//        application.registerActivityLifecycleCallbacks(_appOpenAdActivityObserver)
//    }
//
//    //////////////////////////////////////////////////////////////////////////////
//
//    private inner class ActivityLifecycleObserver(private val _onForeground: I_Listener) : DefaultLifecycleObserver {
//        override fun onStart(owner: LifecycleOwner) {
//            _onForeground.invoke()
//        }
//    }
//
//    private inner class ActivityLifecycleCallbacks : BaseActivityLifecycleCallbacks() {
//        override fun onActivityStarted(activity: Activity) {
//            _activityRef = if (activity is A) WeakReference(activity) else null// example of choosing specific Activity for show AppOpenAd
//        }
//    }
//}
