package com.mozhimen.adk.topon.basic.test.helpers

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlin.math.abs

/**
 * @ClassName SplashZoomOutLayout
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/22
 * @Version 1.0
 */

class SplashZoomOutLayout(context: Context?, m: Int) : FrameLayout(context!!) {
    private var dX = 0f
    private var dY = 0f
    private val margin: Int
    private var maxX = 0f
    private var maxY = 0f
    private var moveAccumulateX = 0f
    private var moveAccumulateY = 0f
    private val touchSlop //拖动和点击的触发阈值，采用系统的参数，超过该值认为是拖动，低于认为是点击
            : Int

    init {
        //设置悬浮窗的圆角
        val gd = GradientDrawable()
        gd.setCornerRadius(10f)
        setBackgroundDrawable(gd)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setClipToOutline(true)
        }
        margin = m
        touchSlop = ViewConfiguration.get(context!!).scaledTouchSlop
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post(Runnable {
            val parent = parent as View ?: return@Runnable
            val parentWidth = parent.width
            val parentHeight = parent.height
            maxY = (parentHeight - this@SplashZoomOutLayout.height - margin).toFloat()
            maxX = (parentWidth - this@SplashZoomOutLayout.width - margin).toFloat()
        })
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = x - event.rawX
                dY = y - event.rawY
                moveAccumulateX = 0f
                moveAccumulateY = 0f
            }

            MotionEvent.ACTION_MOVE -> {
                var newX = event.rawX + dX
                var newY = event.rawY + dY

                //这里采用累积，防止转一圈回到起点的情况也触发点击
                moveAccumulateX += abs(newX - x)
                moveAccumulateY += abs(newY - y)
                //限制浮窗不会超出父布局
                newX = if (newX < margin) margin.toFloat() else if (newX > maxX) maxX else newX
                newY = if (newY < margin) margin.toFloat() else if (newY > maxY) maxY else newY
                animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start()
            }

            MotionEvent.ACTION_UP -> {
                //拖动吸附，放开手时自动吸附到左右两边
                val animationX: Float
                val upX = event.rawX + dX
                animationX = if (upX * 2 > maxX) {
                    maxX.toFloat()
                } else {
                    margin.toFloat()
                }
                animate()
                    .x(animationX)
                    .setDuration(0)
                    .start()
                //如果拖动超过一定距离拦截发向子view的点击事件
                if (moveAccumulateX > touchSlop || moveAccumulateY > touchSlop) {
                    return true
                }
            }

            else -> {}
        }
        return super.onInterceptTouchEvent(event)
    }
}

