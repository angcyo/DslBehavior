package com.angcyo.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/03
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
open class HideTitleBarBehavior(
    context: Context,
    attrs: AttributeSet? = null
) : BaseScrollBehavior<View>(context, attrs), ITitleBarBehavior {

    /**忽略状态栏的高度, 如果为true, 会是沉浸式*/
    var ignoreStatusBar = false

    /**
     * 激活[Over]滚动时才监听, 否则内容滚动时就监听,
     * 通常在内容不够高度时, 是否要监听行为.
     * */
    var enableOverScroll = false

    /**
     * 是否只有在内容滚动到边界时才监听, 否则scrollY!=0时, 就会监听,
     * 通常在内容很高, 是否到首尾时, 才监听行为.
     * */
    var enableEdgeScroll = false

    var contentBehavior: IContentBehavior? = null

    init {
        showLog = false
        behaviorScrollTo = { _, y ->
            childView?.offsetTopTo(y)
        }
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency.behavior() is IContentBehavior) {
            contentBehavior = dependency.behavior() as IContentBehavior
        }
        return super.layoutDependsOn(parent, child, dependency)
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
        return axes.isVertical()
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

        val contentScrollY = contentBehavior?.getContentScrollY(this) ?: 0

        val minScrollY = getContentExcludeHeight(this) - child.measuredHeight
        val maxScrollY = 0

        var handle = false
        if (enableOverScroll && !target.topCanScroll() && !target.bottomCanScroll()) {
            //内容不可滚动, 并且开启了over scroll 监听
            if (contentScrollY == 0) {
                handle = true
            }
        } else if (enableEdgeScroll) {
            //内容无法滚动时, 才监听
            if (dy > 0 && contentScrollY == 0) {
                handle = true
            } else if (dy < 0 && contentScrollY == 0 && !target.topCanScroll()) {
                handle = true
            }
        } else {
            handle = if (target.topCanScroll() || target.bottomCanScroll()) {
                if (contentScrollY == 0) {
                    true
                } else {
                    behaviorScrollY in (minScrollY + 1) until maxScrollY
                }
            } else {
                behaviorScrollY != 0
            }
        }

        if (handle) {
            consumedScrollVertical(
                dy,
                behaviorScrollY,
                minScrollY,
                maxScrollY,
                consumed
            )
        }
    }

    override fun getContentExcludeHeight(behavior: BaseDependsBehavior<*>): Int {
        return if (ignoreStatusBar) 0 else childView?.getStatusBarHeight() ?: 0
    }

    override fun getContentOffsetTop(behavior: BaseDependsBehavior<*>): Int {
        return when {
            childView == null -> 0
            ViewCompat.isLaidOut(childView!!) -> childView?.bottom ?: 0
            else -> childView.mH() + behaviorScrollY
        }
    }
}