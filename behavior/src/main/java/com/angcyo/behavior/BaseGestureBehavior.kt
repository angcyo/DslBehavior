package com.angcyo.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * 支持[GestureDetector]的处理.
 *
 * 使用时, 请注意[CoordinatorLayout]具有消耗事件的能力, 否则[TOUCH_DOWN]之后, 就无法收到其他事件了.
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/03/20
 */
abstract class BaseGestureBehavior<T : View>(
    context: Context,
    attributeSet: AttributeSet? = null
) : BaseScrollBehavior<T>(context, attributeSet), Runnable {

    /**touch scroll 阈值*/
    var touchSlop = 0

    /**是否开启touch捕捉*/
    var enableGesture = true

    //手势检测
    val _gestureDetector: GestureDetector by lazy {
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                return onGestureFling(e1, e2, velocityX, velocityY)
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                return onGestureScroll(e1, e2, distanceX, distanceY)
            }
        })
    }

    /**手势捕捉*/
    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: T,
        ev: MotionEvent
    ): Boolean {
        return super.onInterceptTouchEvent(parent, child, ev) || handleTouchEvent(parent, child, ev)
    }

    /**手势捕捉*/
    override fun onTouchEvent(parent: CoordinatorLayout, child: T, ev: MotionEvent): Boolean {
        return super.onTouchEvent(parent, child, ev) || handleTouchEvent(parent, child, ev)
    }

    //是否需要touch event, 当子view调用了[requestDisallowInterceptTouchEvent]后, 还是能收到MOVE事件.
    //典型的案例就是ViewPager
    var _needHandleTouch = true

    //首次滚动检查, 如果首次滚动不需要处理事件, 那么之后都收不到.
    var _isFirstScroll = true

    /**统一手势处理*/
    open fun handleTouchEvent(parent: CoordinatorLayout, child: T, ev: MotionEvent): Boolean {
        var result = false
        if (ev.isTouchFinish()) {
            parent.requestDisallowInterceptTouchEvent(false)
            onTouchFinish(parent, child, ev)
        } else if (ev.isTouchDown()) {
            _needHandleTouch = true
            _isFirstScroll = true
            onTouchDown(parent, child, ev)
        }
        if (enableGesture && _needHandleTouch) {
            result = _gestureDetector.onTouchEvent(ev)
        }
        return result
    }

    open fun onTouchDown(parent: CoordinatorLayout, child: T, ev: MotionEvent) {

    }

    open fun onTouchFinish(parent: CoordinatorLayout, child: T, ev: MotionEvent) {

    }

    open fun onGestureFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return onFling(e1, e2, velocityX, velocityY)
    }

    open fun onGestureScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        val result = onScroll(e1, e2, distanceX, distanceY)

        if (_isFirstScroll) {
            _needHandleTouch = result
            _isFirstScroll = false
        }

        return result
    }

    /**手势Fling处理*/
    open fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    /**手势Scroll处理*/
    open fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun run() {

    }
}