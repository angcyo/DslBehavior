package com.angcyo.behavior.refresh

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.angcyo.behavior.*
import com.angcyo.behavior.refresh.IRefreshBehavior.Companion.STATUS_FINISH
import com.angcyo.behavior.refresh.IRefreshBehavior.Companion.STATUS_NORMAL
import com.angcyo.behavior.refresh.IRefreshBehavior.Companion.STATUS_REFRESH


/**
 * 下拉刷新行为处理类, UI效果处理代理给[IRefreshBehavior]
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/12/31
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
open class RefreshContentBehavior(
    context: Context,
    attrs: AttributeSet? = null
) : BaseScrollBehavior<View>(context, attrs), IContentBehavior, IRefreshContentBehavior {

    /**标题栏的行为, 用于布局在标题栏bottom里面*/
    var titleBarPlaceholderBehavior: ITitleBarBehavior? = null

    /**[child]需要排除多少高度*/
    val excludeHeight
        get() = if (excludeTitleBarHeight)
            titleBarPlaceholderBehavior?.getContentExcludeHeight(this) ?: 0 else 0

    /**child是否要排除标题栏的高度*/
    var excludeTitleBarHeight: Boolean = true

    /**child布局是否要考虑标题栏*/
    var offsetTitleBarHeight: Boolean = true

    /**刷新行为界面处理*/
    override var refreshBehaviorConfig: IRefreshBehavior? = RefreshEffectConfig()

    /**刷新触发的回调*/
    override var refreshAction: (IRefreshContentBehavior) -> Unit =
        { Log.i(this::class.java.simpleName, "触发刷新:${it.simpleHash()}") }

    /**刷新状态*/
    var refreshStatus: Int
        get() = refreshBehaviorConfig?._refreshBehaviorStatus ?: STATUS_NORMAL
        set(value) {
            refreshBehaviorConfig?.onSetRefreshBehaviorStatus(this, value)
        }

    init {
        showLog = false

        behaviorScrollTo = { x, y ->
            refreshBehaviorConfig?.onContentScrollTo(this, x, y)
        }

        val array = context.obtainStyledAttributes(attrs, R.styleable.RefreshContentBehavior_Layout)
        excludeTitleBarHeight = array.getBoolean(
            R.styleable.RefreshContentBehavior_Layout_layout_exclude_title_bar_height,
            excludeTitleBarHeight
        )
        offsetTitleBarHeight = array.getBoolean(
            R.styleable.RefreshContentBehavior_Layout_layout_offset_title_bar_height,
            excludeTitleBarHeight
        )
        array.recycle()
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {

        super.onMeasureChild(
            parent,
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed
        )

        parent.onMeasureChild(
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed + excludeHeight
        )

        return true
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        behaviorOffsetTop = if (offsetTitleBarHeight) {
            titleBarPlaceholderBehavior?.getContentOffsetTop(this) ?: 0
        } else {
            0
        }
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onLayoutChildAfter(parent: CoordinatorLayout, child: View, layoutDirection: Int) {
        super.onLayoutChildAfter(parent, child, layoutDirection)
        refreshBehaviorConfig?.onContentLayout(this, parent, child)
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val behavior = dependency.behavior()
        behavior?.let {
            if (it is ITitleBarBehavior) {
                titleBarPlaceholderBehavior = it
            }
            if (it is IRefreshBehavior) {
                refreshBehaviorConfig = it
            }
        }
        super.layoutDependsOn(parent, child, dependency)
        return offsetTitleBarHeight && enableDependsOn && behavior is ITitleBarBehavior
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        behaviorOffsetTop = dependency.bottom
        return super.onDependentViewChanged(parent, child, dependency)
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
        _overScroller.abortAnimation()
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
            refreshBehaviorConfig?.onContentOverScroll(this, dxUnconsumed, dyUnconsumed)
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        refreshBehaviorConfig?.onContentStopScroll(this)
    }

    override fun getContentScrollY(behavior: BaseDependsBehavior<*>): Int {
        return behaviorScrollY
    }

    override fun getRefreshCurrentScrollY(dy: Int): Int {
        return behaviorScrollY
    }

    override fun getRefreshMaxScrollY(dy: Int): Int {
        return childView.mH()
    }

    override fun getRefreshResetScrollY() = 0

    /**开始刷新*/
    fun startRefresh() {
        refreshStatus = STATUS_REFRESH
    }

    /**结束刷新*/
    fun finishRefresh() {
        refreshStatus = STATUS_FINISH
    }

    override fun setRefreshContentStatus(status: Int) {
        refreshStatus = status
    }
}