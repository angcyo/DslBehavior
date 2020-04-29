package com.angcyo.behavior.linkage

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils.clamp
import com.angcyo.behavior.IScrollBehaviorListener
import com.angcyo.behavior.R
import com.angcyo.behavior.behavior
import com.angcyo.behavior.mH

/**
 * 背景放大缩小的行为. 配合[LinkageHeaderBehavior]使用
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/03/21
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
open class LinkageScaleBehavior(
    context: Context,
    attributeSet: AttributeSet? = null
) : BaseLinkageBehavior(context, attributeSet), IScrollBehaviorListener {

    /**效果作用的目标, 不设置就是 child*/
    var scaleTargetView: View? = null

    /**激活高度变化*/
    var enableHeightEffect: Boolean = true

    /**激活Scale变化*/
    var enableScaleEffect: Boolean = true

    /**比例计算的分母, -1是child的高度*/
    var scaleMaxHeight: Int = -1

    /**scale计算的额外控制因子*/
    var scaleFactor: Float = 3f

    /**最大放大倍数*/
    var maxScale = 4f

    val _maxHeight: Int
        get() = if (scaleMaxHeight > 0) scaleMaxHeight else childView.mH()

    val _targetView: View?
        get() = scaleTargetView ?: childView

    val _scale: Float
        get() = clamp(1f + behaviorScrollY * 1f / _maxHeight * scaleFactor, 1f, maxScale)

    init {
        showLog = false
        onBehaviorScrollTo = { x, y ->
            //L.i("->$y $_scale")
            if (enableScaleEffect) {
                _targetView?.apply {
                    scaleX = _scale
                    scaleY = scaleX
                }
            }
        }

        val array =
            context.obtainStyledAttributes(attributeSet, R.styleable.LinkageScaleBehavior_Layout)
        enableHeightEffect = array.getBoolean(
            R.styleable.LinkageScaleBehavior_Layout_layout_enable_height_effect,
            enableHeightEffect
        )
        enableScaleEffect = array.getBoolean(
            R.styleable.LinkageScaleBehavior_Layout_layout_enable_scale_effect,
            enableScaleEffect
        )
        scaleMaxHeight =
            array.getDimensionPixelOffset(
                R.styleable.LinkageScaleBehavior_Layout_layout_scale_max_height,
                scaleMaxHeight
            )
        maxScale = array.getFloat(
            R.styleable.LinkageScaleBehavior_Layout_layout_max_scale,
            maxScale
        )
        scaleFactor = array.getFloat(
            R.styleable.LinkageScaleBehavior_Layout_layout_scale_factor,
            scaleFactor
        )
        array.recycle()
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        super.layoutDependsOn(parent, child, dependency)
        headerView.behavior().apply {
            if (this is LinkageHeaderBehavior) {
                this.addScrollListener(this@LinkageScaleBehavior)
            }
        }
        return false
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

    override fun onBehaviorScrollTo(x: Int, y: Int) {
        if (y >= 0) {
            val oldY = behaviorScrollY
            scrollTo(0, y)
            if (enableHeightEffect && oldY != y) {
                _targetView?.apply {
                    requestLayout()
                }
            }
        }
    }
}