package com.mozhimen.adk.topon.basic.test.helpers

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import com.mozhimen.basick.lintk.optins.OApiUse_BaseApplication
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.view.UtilKScreen
import com.mozhimen.basick.utilk.android.view.UtilKView

/**
 * @ClassName SplashZoomOutManager
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

class SplashZoomOutManager private constructor(context: Context) {
    private var zoomOutWidth: Int//悬浮窗的宽度
    private var zoomOutHeight: Int//悬浮窗的高度
    private val zoomOutMargin: Int//悬浮窗最小离屏幕边缘的距离
    private val zoomOutMarginBottom: Int//悬浮窗默认距离屏幕底端的高度
    private val zoomOutPos: Int//悬浮窗默认位于屏幕左面或右面
    private val zoomOutAnimationTime: Int//悬浮窗缩放动画的，单位ms
    private var splashView: View? = null
    private var originSplashWidth = 0
    private var originSplashHeight = 0
    private val originSplashPos = IntArray(2)
    private var decorViewWidth = 0
    private var decorViewHeight = 0

    interface AnimationCallBack {
        fun animationStart(animationTime: Int)
        fun animationEnd()
    }

    init {
        val deviceWidth: Int = Math.min(UtilKScreen.getWidth_ofAppMetrics(), UtilKScreen.getHeight_ofAppMetrics())
        zoomOutWidth = Math.round(deviceWidth * 0.3f) //屏幕宽度的30%，之前使用PxUtils.dpToPx(context, 90);
        zoomOutHeight = Math.round((zoomOutWidth * 16 / 9).toFloat()) //根据宽度计算高度，之前使用PxUtils.dpToPx(context, 160);
        zoomOutMargin = 6f.dp2px.toInt()
        zoomOutMarginBottom = 100f.dp2px.toInt()
        zoomOutPos = RIGHT
        zoomOutAnimationTime = 300
    }

    /**
     * 用于开屏v+在两个activity之间传递数据
     *
     * @param splashView 开屏对应显示view，外部提供开屏container的子view
     * @param decorView  因为在另一个单独的activity启动时获取不到view尺寸，在这里获取下decorView的尺寸，在展示悬挂的
     * activity使用该尺寸布局
     */
    fun setSplashInfo(splashView: View, decorView: View) {
        this.splashView = splashView
        splashView.getLocationOnScreen(originSplashPos)
        originSplashWidth = splashView.width
        originSplashHeight = splashView.height
        decorViewWidth = decorView.width
        decorViewHeight = decorView.height
    }

    fun setSplashEyeAdViewSize(width: Int, height: Int) {
        zoomOutWidth = width
        zoomOutHeight = height
    }

    fun clearStaticData() {
        splashView = null
    }

    /**
     * 开屏采用单独的activity时候，悬浮窗显示在另外一个activity使用该函数进行动画
     * 调用前要先调用setSplashInfo设置数据，该函数会使用setSplashInfo设置的数据，并会清除对设置数据的引用
     *
     * @param animationContainer 一般是decorView
     * @param zoomOutContainer   最终浮窗所在的父布局
     * @param callBack           动画完成的回调
     */
    @OptIn(OApiUse_BaseApplication::class)
    fun startZoomOut(
        animationContainer: ViewGroup?,
        zoomOutContainer: ViewGroup?,
        callBack: AnimationCallBack?
    ): ViewGroup? {
        Log.d(TAG, "zoomOut startZoomOut activity")
        if (animationContainer == null || zoomOutContainer == null) {
            Log.d(TAG, "zoomOut animationContainer or zoomOutContainer is null")
            return null
        }
        if (splashView == null) {
            Log.d(TAG, "zoomOut splashAD or splashView is null")
            return null
        }
        //先把view按照原来的尺寸显示出来
        val animationContainerPos = IntArray(2)
        animationContainer.getLocationOnScreen(animationContainerPos)
        val x = originSplashPos[0] - animationContainerPos[0]
        val y = originSplashPos[1] - animationContainerPos[1]
        UtilKView.removeViewForParent(splashView!!)
        val layoutParams = FrameLayout.LayoutParams(
            originSplashWidth,
            originSplashHeight
        )
        animationContainer.addView(splashView, layoutParams)
        splashView!!.x = x.toFloat()
        splashView!!.y = y.toFloat()
        return startZoomOut(splashView, animationContainer, zoomOutContainer, callBack)
    }

    /**
     * 开屏显示和悬浮窗显示在同一个activity中
     * 使用该函数会清除setSplashInfo设置的数据
     * * 动画步骤：
     * 1、把需要动画的view从父布局中移除出来，目的是在动画时可以隐藏其他开屏的view
     * 2、把splash对应的view加到动画的view里开始动画，因为动画窗口可能比较最终的布局要大
     * 3、在动画结束把splash view加到zoomOutContainer里
     *
     * @param splash             开屏对应的view;
     * @param animationContainer 开屏动画所在的layout
     * @param zoomOutContainer   动画结束时，最终悬浮窗所在的父布局
     * @param callBack           动画结束时的回调，splashAdView无法感知动画的执行时间，需要使用该函数通知动画结束了
     */
    @OptIn(OApiUse_BaseApplication::class)
    fun startZoomOut(
        splash: View?, animationContainer: ViewGroup,
        zoomOutContainer: ViewGroup?,
        callBack: AnimationCallBack?
    ): ViewGroup? {
        clearStaticData() //单例清除下引用的view和ad数据，免得内存泄漏
        if (splash == null || zoomOutContainer == null) {
            return null
        }
        val context = zoomOutContainer.context
        val splashScreenPos = IntArray(2)
        splash.getLocationOnScreen(splashScreenPos)
        val fromWidth = splash.width
        val fromHeight = splash.height
        var animationContainerWidth = animationContainer.width
        var animationContainerHeight = animationContainer.height
        if (animationContainerWidth == 0) {
            animationContainerWidth = decorViewWidth
        }
        if (animationContainerHeight == 0) {
            animationContainerHeight = decorViewHeight
        }
        val xScaleRatio = zoomOutWidth.toFloat() / fromWidth
        val yScaleRation = zoomOutHeight.toFloat() / fromHeight
        val animationDistX = (if (zoomOutPos == LEFT) zoomOutMargin else animationContainerWidth - zoomOutMargin - zoomOutWidth).toFloat()
        val animationDistY = (animationContainerHeight - zoomOutMarginBottom - zoomOutHeight).toFloat() //最终位于container的y坐标
        Log.d(
            TAG, "zoomOut animationContainerWidth:" + animationContainerWidth + " " +
                    "animationContainerHeight:" + animationContainerHeight
        )
        Log.d(TAG, "zoomOut splashScreenX:" + splashScreenPos[0] + " splashScreenY:" + splashScreenPos[1])
        Log.d(TAG, "zoomOut splashWidth:$fromWidth splashHeight:$fromHeight")
        Log.d(TAG, "zoomOut width:$zoomOutWidth height:$zoomOutHeight")
        Log.d(TAG, "zoomOut animationDistX:$animationDistX animationDistY:$animationDistY")
        UtilKView.removeViewForParent(splash)
        val animationParams = FrameLayout.LayoutParams(fromWidth, fromHeight)
        animationContainer.addView(splash, animationParams)
        val zoomOutView: ViewGroup = SplashZoomOutLayout(context, zoomOutMargin)
        splash.pivotX = 0f
        splash.pivotY = 0f
        splash.animate()
            .scaleX(xScaleRatio)
            .scaleY(yScaleRation)
            .x(animationDistX)
            .y(animationDistY)
            .setInterpolator(OvershootInterpolator(0f))
            .setDuration(zoomOutAnimationTime.toLong())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    Log.d(TAG, "zoomOut onAnimationStart")
                    callBack?.animationStart(zoomOutAnimationTime)
                }

                override fun onAnimationEnd(animation: Animator) {
                    Log.d(TAG, "zoomOut onAnimationEnd")
                    UtilKView.removeViewForParent(splash)
                    splash.scaleX = 1f
                    splash.scaleY = 1f
                    splash.x = 0f
                    splash.y = 0f
                    val zoomOutContainerScreenPos = IntArray(2)
                    zoomOutContainer.getLocationOnScreen(zoomOutContainerScreenPos)
                    val distX = animationDistX - zoomOutContainerScreenPos[0] + splashScreenPos[0]
                    val distY = animationDistY - zoomOutContainerScreenPos[1] + splashScreenPos[1]
                    Log.d(TAG, "zoomOut distX:$distX distY:$distY")
                    Log.d(
                        TAG, "zoomOut containerScreenX:" + zoomOutContainerScreenPos[0] + " " +
                                "containerScreenY:" + zoomOutContainerScreenPos[1]
                    )
                    zoomOutView.addView(
                        splash, FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    val zoomOutParams = FrameLayout.LayoutParams(
                        zoomOutWidth,
                        zoomOutHeight
                    )
                    zoomOutContainer.addView(zoomOutView, zoomOutParams)
                    zoomOutView.translationX = distX
                    zoomOutView.translationY = distY
                    callBack?.animationEnd()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
        return zoomOutView
    }

    companion object {
        private const val TAG = "SplashZoomOutManager"
        private const val LEFT = 0
        private const val RIGHT = 1

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: SplashZoomOutManager? = null

        fun getInstance(context: Context): SplashZoomOutManager {
            if (instance == null) {
                synchronized(SplashZoomOutManager::class.java) { if (instance == null) instance = SplashZoomOutManager(context) }
            }
            return instance!!
        }
    }
}
