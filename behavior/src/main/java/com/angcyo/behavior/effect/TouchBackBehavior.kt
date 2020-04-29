package com.angcyo.behavior.effect

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.core.view.ViewCompat
import com.angcyo.behavior.BaseGestureBehavior
import com.angcyo.behavior.R
import com.angcyo.behavior.mH
import com.angcyo.behavior.offsetTopTo
import kotlin.math.absoluteValue


/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/17
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
open class TouchBackBehavior(
    context: Context,
    attributeSet: AttributeSet? = null
) : BaseGestureBehavior<View>(context, attributeSet) {

    /**关闭阈值, 滚动大于这个值时, 判定是需要关闭*/
    var touchBackSlop: Int = 30

    /**首次布局时, 需要滚动的偏移距离. 实现半屏效果*/
    var defaultScrollOffsetY: Int = 0

    //实际的滚动偏移, 参与计算
    var _scrollOffsetY = 0

    var _scaledTouchSlop: Int = 0

    init {

        val array =
            context.obtainStyledAttributes(attributeSet, R.styleable.TouchBackBehavior_Layout)

        val dp = context.resources.displayMetrics.density
        touchBackSlop = array.getDimensionPixelOffset(
            R.styleable.TouchBackBehavior_Layout_layout_touch_back_slop,
            (touchBackSlop * dp).toInt()
        )

        defaultScrollOffsetY =
            array.getDimensionPixelOffset(
                R.styleable.TouchBackBehavior_Layout_layout_default_scroll_offset_y,
                0
            )

        array.recycle()

        _scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onMeasureAfter(parent: CoordinatorLayout, child: View) {
        super.onMeasureAfter(parent, child)
        _scrollOffsetY = defaultScrollOffsetY
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        if (!ViewCompat.isLaidOut(child)) {
            //首次布局
            scrollTo(0, _scrollOffsetY)
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }


    override fun scrollTo(x: Int, y: Int) {
        val min = 0
        val max = childView.mH()
        val targetY = MathUtils.clamp(y, min, max)
        super.scrollTo(x, targetY)
        childView?.offsetTopTo(targetY + behaviorOffsetTop)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        if (behaviorScrollY != 0 && dy != 0) {
            //内容产生过偏移, 那么此次的内嵌滚动肯定是需要消耗的
            consumedScrollVertical(dy, consumed)
        }
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )

        if (dyConsumed == 0 && type.isTouch()) {
            //内嵌滚动视图已经不需要消耗滚动值了, 通常是到达了首尾两端
            scrollBy(0, -dyUnconsumed)
        }
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (_nestedScrollView == null) {
            scrollBy(0, -distanceY.toInt())
            return true
        }
        return false
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        if (!isTouchHold) {
            resetScroll()
        }
    }

    override fun onTouchFinish(parent: CoordinatorLayout, child: View, ev: MotionEvent) {
        super.onTouchFinish(parent, child, ev)
        if (!isTouchHold && _nestedScrollView == null && ViewCompat.isLaidOut(child)) {
            //在非nested scroll 视图上滚动过
            resetScroll()
        }
    }

    /**重置滚动状态*/
    fun resetScroll() {
        //滚动距离大于这个值时, 就要关闭界面
        val minSlop = touchBackSlop
        val scrollDy = behaviorScrollY - _scrollOffsetY
        val offsetY = defaultScrollOffsetY

        if (scrollDy > minSlop) {
            //滑动大于child的5分之一
            scrollToClose()
        } else {
            _scrollOffsetY = if (_scrollOffsetY <= 0) {
                0
            } else if (scrollDy > 0) {
                //手指向下拖拽, 但是未达到阈值
                offsetY
            } else {
                //手指向上拖拽
                if (_scrollOffsetY > 0 && scrollDy.absoluteValue > _scaledTouchSlop) {
                    //大于阈值
                    0
                } else {
                    offsetY
                }
            }
            scrollToNormal()
        }
    }

    /**滚动至正常*/
    fun scrollToNormal() {
        startScrollTo(0, _scrollOffsetY)
    }

    /**滚动至关闭*/
    fun scrollToClose() {
        startScrollTo(0, childView.mH())
    }
}