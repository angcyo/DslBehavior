package com.angcyo.behavior.linkage

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.NestedScrollingChild
import androidx.core.view.ViewCompat
import com.angcyo.behavior.*
import java.lang.ref.WeakReference
import kotlin.math.absoluteValue

/**
 * 不支持 margin 属性
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/03/20
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
abstract class BaseLinkageBehavior(
    context: Context,
    attributeSet: AttributeSet? = null
) : BaseGestureBehavior<View>(context, attributeSet) {

    companion object {
        //正在快速fling的视图
        var _linkageFlingScrollView: WeakReference<NestedScrollingChild>? = null
    }

    //联动相关布局
    var headerView: View? = null //头部布局
    var footerView: View? = null //底部布局
    var stickyView: View? = null //悬停布局

    //behavior 作用在的[RecyclerView], 通常会等于[headerRecyclerView] [footerRecyclerView]其中的一个
    val childScrollView: NestedScrollingChild?
        get() = childView?.findNestedScrollingChild()

    val headerScrollView: NestedScrollingChild?
        get() = headerView?.findNestedScrollingChild()

    val footerScrollView: NestedScrollingChild?
        get() = footerView?.findNestedScrollingChild()

    val stickyScrollView: NestedScrollingChild?
        get() = stickyView?.findNestedScrollingChild()

    //对应的Behavior
    val linkageStickyBehavior: LinkageStickyBehavior? get() = stickyView.behavior() as LinkageStickyBehavior?
    val linkageHeaderBehavior: LinkageHeaderBehavior? get() = headerView.behavior() as LinkageHeaderBehavior?
    val linkageFooterBehavior: LinkageFooterBehavior? get() = footerView.behavior() as LinkageFooterBehavior?

    /**关联布局依赖*/
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val result = super.layoutDependsOn(parent, child, dependency)

        for (i in 0 until parent.childCount) {
            val childView = parent.getChildAt(i)
            val targetView = if (childView.visibility == View.VISIBLE) childView else null
            when (childView.behavior()) {
                is LinkageHeaderBehavior -> headerView = targetView
                is LinkageFooterBehavior -> footerView = targetView
                is LinkageStickyBehavior -> stickyView = targetView
            }
        }

        return result
    }

    /**接收内嵌滚动*/
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
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL &&
                (headerView.mH() + stickyView.mH() + footerView.mH()) > coordinatorLayout.mH()
    }

    override fun onTouchDown(parent: CoordinatorLayout, child: View, ev: MotionEvent) {
        super.onTouchDown(parent, child, ev)
        stopNestedScroll()
        stopNestedFling()
    }

    /**停止滚动*/
    open fun stopNestedScroll() {
        _overScroller.abortAnimation()
        _nestedScrollView?.apply {
            e(" 停止Scroll:${this.simpleHash()}")
            stopScroll()
        }
        _nestedScrollView = null
    }

    open fun stopNestedFling() {
        _linkageFlingScrollView?.get()?.apply {
            e(" 停止Fling:${this.simpleHash()}")
            stopScroll()
        }
        _linkageFlingScrollView?.clear()
        _linkageFlingScrollView = null

        _nestedPreFling = false
    }

    /**设置正在fling的视图*/
    open fun setFlingView(child: NestedScrollingChild) {
        stopNestedScroll()
        stopNestedFling()
        _linkageFlingScrollView = WeakReference(child)
        if (child is View) {
            _nestedFlingView = child
        }
        //L.e("fling $child")
    }

    //Fling访问标识, child包裹的view
    var _nestedPreFling = false

    //Fling的方向, >0 手指向上, <0 手指向下.
    var _nestedFlingDirection = 0

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (childScrollView == target && velocityY.absoluteValue > velocityX.absoluteValue) {
            _nestedPreFling = true
            _nestedFlingDirection = velocityY.toInt()
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
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

        if (dyUnconsumed != 0 /*处于over scroll的情况*/
            && _nestedPreFling /*Fling访问*/
            && childScrollView == target /*只处理自己内部的RecyclerView*/
        ) {

            //fling速度衰减, 获取到的速度永远是正值. 需要根据方向自行调整正负值
            val velocityY = (target.getLastVelocity() * 0.9).toInt()
            if (velocityY < minFlingVelocity) {
                return
            }

            val footerRV = footerScrollView
            val headerRV = headerScrollView

            var scrollTarget: NestedScrollingChild? = null

            when (target) {
                footerRV -> {
                    //L.i("footer fling $velocityY")
                    //来自Footer的Fling, 那么要传给Header
                    headerRV?.apply {
                        if (_linkageFlingScrollView?.get() == this ||
                            dyUnconsumed > 0 /*footer fling到底后, 不需要传递给header*/) {
                            return
                        }
                        scrollTarget = this
                    }
                }
                headerRV -> {
                    //L.i("header fling $velocityY")
                    //来自Header的Fling, 那么要传给Footer
                    footerRV?.apply {
                        if (topCanScroll() || bottomCanScroll()) {
                            if (_linkageFlingScrollView?.get() == this
                                || dyUnconsumed < 0 /*header fling到顶后, 不需要传给footer*/) {
                                return
                            }
                            scrollTarget = this
                        } else {
                            scrollTarget = headerRV
                        }
                    }
                }
                else -> {
                    //来自其他位置的Fling, 比如Sticky
                    val delegateScrollView: NestedScrollingChild? =
                        footerScrollView ?: headerScrollView
                    delegateScrollView?.apply {
                        scrollTarget = this
                    }
                }
            }

            scrollTarget?.apply {
                if (_linkageFlingScrollView?.get() != this) {
                    setFlingView(this)
                    //根据方向, 调整fling正负值
                    if (_nestedFlingDirection > 0) {
                        fling(0, velocityY)
                    } else {
                        fling(0, -velocityY)
                    }
                }
            }
        }
    }
}