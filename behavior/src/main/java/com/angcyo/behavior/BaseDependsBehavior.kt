package com.angcyo.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.angcyo.widget.layout.RCoordinatorLayout
import kotlin.math.max
import kotlin.math.min

/**
 * 必须有2个参数的构造方法
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/09/10
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
abstract class BaseDependsBehavior<T : View>(
    context: Context? = null,
    attrs: AttributeSet? = null
) : LogBehavior<T>(context, attrs) {

    /**依赖的视图, 用于触发[onDependentViewChanged]*/
    var dependsView: View? = null

    /**是否需要监听[dependsView]的改变*/
    var enableDependsOn = true

    //常用对象

    /**[com.angcyo.widget.layout.RCoordinatorLayout.onAttachedToWindow]*/
    var childView: T? = null
    var parentLayout: CoordinatorLayout? = null

    /**是否正在Touch, 手指未放开. 需要[RCoordinatorLayout]的支持*/
    val isTouchHold: Boolean
        get() = (parentLayout as? RCoordinatorLayout)?._isTouch ?: false

    /**手势是否被其他子控件拦截了*/
    val isDisallowIntercept: Boolean
        get() = (parentLayout as? RCoordinatorLayout)?._disallowIntercept ?: false

    /**当[CoordinatorLayout]只有一个child时, 这个方法不会回调*/
    @CallSuper
    override fun layoutDependsOn(parent: CoordinatorLayout, child: T, dependency: View): Boolean {
        parentLayout = parent
        childView = child
        return enableDependsOn && dependsView == dependency
    }

    @CallSuper
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: T,
        dependency: View
    ): Boolean {
        return super.onDependentViewChanged(parent, child, dependency)
    }

    @CallSuper
    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: T, dependency: View) {
        super.onDependentViewRemoved(parent, child, dependency)
        dependsView = null
    }

    fun postInvalidate() {
        parentLayout?.postInvalidate()
    }

    fun postInvalidateOnAnimation() {
        parentLayout?.apply { ViewCompat.postInvalidateOnAnimation(this) }
    }

    fun invalidate() {
        parentLayout?.invalidate()
    }

    /**是否处于内嵌滚动中*/
    val isNestedScrollAccepted: Boolean
        get() = _nestedScrollView != null

    /**是否处于内嵌Fling中*/
    val isFlingAccepted: Boolean
        get() = _nestedFlingView != null

    /**内嵌滚动的视图, 不管是否是child包裹的*/
    var _nestedScrollView: View? = null

    //fling访问的view, 不管是否是child包裹的
    var _nestedFlingView: View? = null

    override fun onNestedScrollAccepted(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ) {
        super.onNestedScrollAccepted(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
        _nestedScrollView = target
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        _nestedFlingView = target
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        _nestedScrollView = null
        //此方法在手势up的时候就会触发, 但此时fling操作可能并没有结束
        //_nestedFlingView = null
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: T,
        ev: MotionEvent
    ): Boolean {
        if (ev.isTouchDown()) {
            _nestedFlingView?.stopScroll()
            _nestedFlingView = null
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }

//    override fun onLayoutChild(parent: CoordinatorLayout, child: T, layoutDirection: Int): Boolean {
//        return super.onLayoutChild(parent, child, layoutDirection)
//    }

    /**
     * 某一个 [child] 布局结束之后的回调, 可以用来恢复[offset]的值
     * [RCoordinatorLayout.onLayoutChild]
     * */
    open fun onLayoutChildAfter(parent: CoordinatorLayout, child: T, layoutDirection: Int) {

    }

    /**
     * 所有[child]布局结束之后回调
     * */
    open fun onLayoutAfter(parent: CoordinatorLayout, child: T, layoutDirection: Int) {

    }

//    override fun onMeasureChild(
//        parent: CoordinatorLayout,
//        child: T,
//        parentWidthMeasureSpec: Int,
//        widthUsed: Int,
//        parentHeightMeasureSpec: Int,
//        heightUsed: Int
//    ): Boolean {
//        return super.onMeasureChild(
//            parent,
//            child,
//            parentWidthMeasureSpec,
//            widthUsed,
//            parentHeightMeasureSpec,
//            heightUsed
//        )
//    }

    /**
     * 某一个 [child] 测量结束之后的回调
     * [RCoordinatorLayout.onMeasureChild]
     * */
    open fun onMeasureChildAfter(
        parent: CoordinatorLayout,
        child: T,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ) {
    }

    /**
     * 所有[child]测量结束之后回调
     * */
    open fun onMeasureAfter(parent: CoordinatorLayout, child: T) {

    }

    //<editor-fold desc="辅助方法">

    /**计算垂直方向,滚动范围内,需要消耗的滚动值*/
    open fun consumedScrollVertical(
        dy: Int,
        current: Int,
        min: Int,
        max: Int,
        consumed: IntArray? = null
    ): Int {

        //修正范围, 主要用于兼容 over scroll
        val minValue = if (dy < 0) {
            min(min, current)
        } else {
            min
        }

        val maxValue = if (dy > 0) {
            max(max, current)
        } else {
            max
        }

        if (current < minValue || current > maxValue) {
            //不在范围内, 不消耗滚动
            return 0
        }

        val target = current - dy

        val result: Int

        result = if (dy < 0) {
            //手指向下滑动
            if (target > maxValue) {
                current - maxValue
            } else {
                dy
            }
        } else {
            if (target < minValue) {
                current - minValue
            } else {
                dy
            }
        }

        consumed?.let {
            it[1] = result
        }

        return result
    }

    /**内容在同向上是否可以滚动*/
    fun isNestedPreContentScrollVertical(target: View, dy: Int): Boolean {
        if (dy > 0 && target.bottomCanScroll()) {
            //手指向上滚动
            return true
        } else if (dy < 0 && target.topCanScroll()) {
            return true
        }
        return false
    }

    //</editor-fold desc="辅助方法">
}