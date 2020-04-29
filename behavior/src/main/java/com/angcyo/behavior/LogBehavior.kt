package com.angcyo.behavior

import android.content.Context
import android.graphics.Rect
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 打印相应方法日志
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/06/28
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
open class LogBehavior<T : View>(
    context: Context? = null,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<T>(context, attrs) {
    var showLog = false

    init {
        //behavior需要在xml使用layout_behavior属性声明, 才能有效
        //并且属性是声明在child上的
//        val array = context.obtainStyledAttributes(attrs, R.styleable.LogBehavior_Layout)
//        val test =
//            array.getDimensionPixelOffset(R.styleable.LogBehavior_Layout_layout_behavior_test, -1)
//        w("自定义属性...$test")
//        array.recycle()
    }

    //<editor-fold desc="内嵌滚动回调">

    /**
     * 是否要处理此次内嵌滚动
     *
     * @param axes 事件的方向:  1:[ViewCompat.SCROLL_AXIS_HORIZONTAL] 2:[ViewCompat.SCROLL_AXIS_VERTICAL]
     * @param type 滚动事件类型: 0:scroll 1:fling
     * @return true 接收内嵌滚动事件
     * */
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        e("child:${directTargetChild.simpleHash()} target:${target.simpleHash()} axes:${axes.toAxesString()} type:${type.toTypeString()}")
        return super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
    }

    /**内嵌滚动开始访问*/
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
        i("${target.simpleHash()}...axes:${axes.toAxesString()} type:${type.toTypeString()}")
    }

    /**内嵌滚动将要滚动, 可以消耗对应的滚动量*/
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        i("${target.simpleHash()} dx:$dx dy:$dy consumed:${consumed.toStr()} type:${type.toTypeString()}")
    }

    /**内嵌滚动滚动的量*/
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: T,
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
        i("${target.simpleHash()} dxC:$dxConsumed dyC:$dyConsumed dxUC:$dxUnconsumed dyUC:$dyUnconsumed consumed:${consumed.toStr()} type:${type.toTypeString()}")
    }

    /**内嵌滚动停止*/
    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        i("${target.simpleHash()} type:${type.toTypeString()}")
    }

    /**即将fling操作*/
    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        w("${target.simpleHash()} velocityX:$velocityX velocityY:$velocityY")
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    /**内嵌滚动开始fling操作*/
    override fun onNestedFling(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        w("${target.simpleHash()} velocityX:$velocityX velocityY:$velocityY consumed:$consumed")
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
    }

    //</editor-fold desc="内嵌滚动回调">

    //<editor-fold desc="测量相关">

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: T,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        w("widthUsed:$widthUsed heightUsed:$heightUsed")
        return super.onMeasureChild(
            parent,
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed
        )
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: T,
        layoutDirection: Int
    ): Boolean {
        w("${child.simpleHash()} ld:$layoutDirection")
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onAttachedToLayoutParams(params: CoordinatorLayout.LayoutParams) {
        super.onAttachedToLayoutParams(params)
        w("")
    }

    override fun onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams()
        w("")
    }

    //</editor-fold desc="测量相关">

    //<editor-fold desc="依赖其他布局">

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: T,
        dependency: View
    ): Boolean {
        w("${child.simpleHash()} ${dependency.simpleHash()}")
        return super.layoutDependsOn(parent, child, dependency)
    }

    /**如果在此方法中, 改变了[child]的size, 需要return true*/
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: T,
        dependency: View
    ): Boolean {
        w("${dependency.simpleHash()}")
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onDependentViewRemoved(
        parent: CoordinatorLayout,
        child: T,
        dependency: View
    ) {
        super.onDependentViewRemoved(parent, child, dependency)
        w("${dependency.simpleHash()}")
    }

    //</editor-fold desc="依赖其他布局">

    //<editor-fold desc="其他不常见">

    override fun onRequestChildRectangleOnScreen(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        rectangle: Rect,
        immediate: Boolean
    ): Boolean {
        w("")
        return super.onRequestChildRectangleOnScreen(coordinatorLayout, child, rectangle, immediate)
    }

    override fun onApplyWindowInsets(
        coordinatorLayout: CoordinatorLayout,
        child: T,
        insets: WindowInsetsCompat
    ): WindowInsetsCompat {
        w("$insets")
        return super.onApplyWindowInsets(coordinatorLayout, child, insets)
    }

    override fun blocksInteractionBelow(
        parent: CoordinatorLayout,
        child: T
    ): Boolean {
        v("")
        return super.blocksInteractionBelow(parent, child)
    }

    override fun getInsetDodgeRect(
        parent: CoordinatorLayout,
        child: T,
        rect: Rect
    ): Boolean {
        v("")
        return super.getInsetDodgeRect(parent, child, rect)
    }

    override fun getScrimColor(
        parent: CoordinatorLayout,
        child: T
    ): Int {
        v("")
        return super.getScrimColor(parent, child)
    }

    /**[blocksInteractionBelow]*/
    override fun getScrimOpacity(
        parent: CoordinatorLayout,
        child: T
    ): Float {
        return super.getScrimOpacity(parent, child).apply {
            v("scrimOpacity:$this")
        }
    }

    override fun onSaveInstanceState(
        parent: CoordinatorLayout,
        child: T
    ): Parcelable? {
        w("${child.simpleHash()}")
        return super.onSaveInstanceState(parent, child)
    }

    override fun onRestoreInstanceState(
        parent: CoordinatorLayout,
        child: T,
        state: Parcelable
    ) {
        super.onRestoreInstanceState(parent, child, state)
        w("")
    }

    //</editor-fold desc="其他不常见">

    //<editor-fold desc="Touch相关">

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: T,
        ev: MotionEvent
    ): Boolean {
        w("child:${child.simpleHash()} ${ev.actionToString()}")
        return super.onInterceptTouchEvent(parent, child, ev)
    }

    override fun onTouchEvent(
        parent: CoordinatorLayout,
        child: T,
        ev: MotionEvent
    ): Boolean {
        /*${parent.requestDisallowInterceptTouchEvent()}*/
        w("child:${child.simpleHash()} ${ev.actionToString()}")
        return super.onTouchEvent(parent, child, ev)
    }

    //</editor-fold desc="Touch相关">

    //<editor-fold desc="日志输出">

    fun e(msg: String? = null) {
        if (showLog) {
            Log.e(this.simpleHash(), msg ?: "")
        }
    }

    fun w(msg: String? = null) {
        if (showLog) {
            Log.w(this.simpleHash(), msg ?: "")
        }
    }

    fun i(msg: String? = null) {
        if (showLog) {
            Log.i(this.simpleHash(), msg ?: "")
        }
    }

    fun d(msg: String? = null) {
        if (showLog) {
            Log.d(this::class.java.simpleName, msg ?: "")
        }
    }

    fun v(msg: String? = null) {
        if (showLog) {
            Log.v(this::class.java.simpleName, msg ?: "")
        }
    }

    fun Int.toTypeString(): String {
        return when (this) {
            ViewCompat.TYPE_TOUCH -> "TYPE_TOUCH"
            ViewCompat.TYPE_NON_TOUCH -> "TYPE_NON_TOUCH"
            else -> "TYPE_UNKNOWN"
        }
    }

    fun Int.toAxesString(): String {
        return when (this) {
            ViewCompat.SCROLL_AXIS_NONE -> "SCROLL_AXIS_NONE"
            ViewCompat.SCROLL_AXIS_HORIZONTAL -> "SCROLL_AXIS_HORIZONTAL"
            ViewCompat.SCROLL_AXIS_VERTICAL -> "SCROLL_AXIS_VERTICAL"
            else -> "SCROLL_AXIS_UNKNOWN"
        }
    }

    fun Int.isVertical(): Boolean {
        return this == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    fun Int.isTouch(): Boolean {
        return this == ViewCompat.TYPE_TOUCH
    }

    fun IntArray.toStr(): String {
        return buildString {
            append("[")
            this@toStr.forEachIndexed { index, i ->
                append(i)
                if (index != this@toStr.lastIndex) {
                    append(",")
                }
            }
            append("]")
        }
    }

    //</editor-fold desc="日志输出">
}