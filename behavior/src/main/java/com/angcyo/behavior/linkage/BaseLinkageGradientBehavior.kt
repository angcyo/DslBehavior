package com.angcyo.behavior.linkage

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.angcyo.behavior.IScrollBehaviorListener
import com.angcyo.behavior.behavior
import com.angcyo.behavior.mH
import kotlin.math.min

/**
 * 监听内嵌滚动, 用于显示渐变效果的Behavior, 如果标题栏渐变等
 * 支持LinkageHeaderBehavior, 兼容普通的NestedScrollingChild
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/03/24
 */
abstract class BaseLinkageGradientBehavior(
    context: Context,
    attributeSet: AttributeSet? = null
) : BaseLinkageBehavior(context, attributeSet), IScrollBehaviorListener {

    init {
        behaviorScrollTo = { _, _ ->
            //no op
        }
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        super.layoutDependsOn(parent, child, dependency)
        headerView.behavior()?.apply {
            if (this is LinkageHeaderBehavior) {
                this.addScrollListener(this@BaseLinkageGradientBehavior)
            }
        }
        return false
    }

    override fun onScrollTo(x: Int, y: Int) {
        //向下滚动是正值
        val percent = y * 1f / getMaxGradientHeight()
        onGradient(percent)
    }

    override fun onBehaviorScrollTo(x: Int, y: Int) {
        //转发给自己处理
        if (y > 0) {
            _gestureScrollY = 0
            scrollTo(x, y)
        } else {
            //L.w("$_ovserScrollY $_nestedScrollY $_gestureScrollY")
            scrollTo(0, min(y, _gestureScrollY))
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
        return super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        ) || axes == ViewCompat.SCROLL_AXIS_VERTICAL
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
        if (headerScrollView == null || _nestedScrollView == headerScrollView) {
            if (target == headerScrollView || headerScrollView == null) {
                if (dyUnconsumed == 0) {
                    //非OverScroll
                    _gestureScrollY -= dyConsumed
                    scrollTo(0, _gestureScrollY)
                } else {
                    //OverScroll
                    _gestureScrollY -= dyUnconsumed
                    scrollTo(0, _gestureScrollY)
                }
            }
        }
    }

    //LinkageHeaderBehavior回调的是OverScroll值.
    var _gestureScrollY = 0

    override fun onGestureScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (headerScrollView == null) {
            //在非LinkageHeaderBehavior行为中
            if (_nestedScrollView == null) {
                _gestureScrollY -= distanceY.toInt()
                scrollTo(0, _gestureScrollY)
            }
        } else if (_nestedScrollView != headerScrollView) {
            _gestureScrollY -= distanceY.toInt()
            scrollTo(0, _gestureScrollY)
        }
        //L.e("...$distanceX $distanceY $_gestureScrollY")
        return false
    }

    /**参与计算的最大高度. 分母*/
    open fun getMaxGradientHeight() = childView.mH()

    /**开始渐变*/
    abstract fun onGradient(percent: Float)
}