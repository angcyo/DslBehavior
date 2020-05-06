package com.angcyo.behavior.effect

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils.clamp
import androidx.core.view.ViewCompat
import com.angcyo.behavior.BaseGestureBehavior
import com.angcyo.behavior.mH
import kotlin.math.abs

/**
 * 背景放大缩小的行为
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/03/21
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
@Deprecated("使用[LinkageScaleBehavior]")
open class ScaleBehavior(
    context: Context,
    attributeSet: AttributeSet? = null
) : BaseGestureBehavior<View>(context, attributeSet) {

    /**效果作用的目标, 不设置就是 child*/
    var scaleTargetView: View? = null

    /**激活高度变化*/
    var enableHeightEffect: Boolean = true

    /**激活Scale变化*/
    var enableScaleEffect: Boolean = true

    var scaleMaxHeight: Int = -1

    /**最大放大倍数*/
    var maxScale = 4f

    val _maxHeight: Int
        get() = if (scaleMaxHeight > 0) scaleMaxHeight else childView.mH()

    val _targetView: View?
        get() = scaleTargetView ?: childView

    val _scale: Float
        get() = clamp(1f + behaviorScrollY * 1f / _maxHeight, 1f, maxScale)

    init {
        showLog = false
        behaviorScrollTo = { x, y ->
            //L.i("->$y $_scale")
            if (enableScaleEffect) {
                _targetView?.apply {
                    scaleX = _scale
                    scaleY = scaleX
                }
            }
        }
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return super.layoutDependsOn(parent, child, dependency)
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
        if (enableHeightEffect) {
            val childHeight =
                if (child.layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    parent.onMeasureChild(
                        child,
                        parentWidthMeasureSpec,
                        widthUsed,
                        parentHeightMeasureSpec,
                        heightUsed
                    )
                    child.mH()
                } else {
                    child.layoutParams.height
                }
            parent.onMeasureChild(
                child,
                parentWidthMeasureSpec,
                widthUsed,
                parentHeightMeasureSpec,
                (heightUsed - childHeight * (_scale - 1)).toInt()
            )
            return true
        } else {
            return false
        }
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
        if (dyUnconsumed != 0 || behaviorScrollY != 0) {
            onTargetOverScroll(target, dyUnconsumed)
        }
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        val absX = abs(distanceX)
        val absY = abs(distanceY)

        if (_nestedScrollView == null && absY > absX && absY > touchSlop) {
            //L.i("scroll $distanceY")
            onTargetOverScroll(childView, distanceY.toInt())
            return true
        }
        return super.onScroll(e1, e2, distanceX, distanceY)
    }

    override fun onTouchFinish(parent: CoordinatorLayout, child: View, ev: MotionEvent) {
        super.onTouchFinish(parent, child, ev)
        if (!isTouchHold && _nestedScrollView == null) {
            //在非nested scroll 视图上滚动过
            startScrollTo(0, 0)
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        startScrollTo(0, 0)
    }

    /**目标Over状态*/
    open fun onTargetOverScroll(target: View?, dy: Int) {
        scrollBy(0, -dy)
        if (enableHeightEffect) {
            _targetView?.apply {
                requestLayout()
            }
        }
    }
}