package com.angcyo.behavior.linkage

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils.clamp
import com.angcyo.behavior.BaseScrollBehavior
import com.angcyo.behavior.R
import com.angcyo.behavior.mH
import kotlin.math.max
import kotlin.math.min

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
) : BaseLinkageGradientBehavior(context, attributeSet) {

    /**效果作用的目标, 不设置就是 child*/
    var scaleTargetView: View? = null

    /**激活高度变化*/
    var enableHeightEffect: Boolean = true

    /**激活Scale变化*/
    var enableScaleEffect: Boolean = true

    /**当滚动比例(滚动距离/child高度)大于等于此值时, 开始缩放. 需要先激活[enableScaleEffect]*/
    var scaleThreshold: Float = 0.3f

    /**比例计算的分母, -1是child的高度*/
    var scaleMaxHeight: Int = -1

    /**scale计算的额外控制因子, 控制下拉距离与高度变化的比例因子*/
    var scaleFactor: Float = 0.7f

    /**最大放大倍数*/
    var maxScale = 4f

    val _maxHeight: Int
        get() = if (scaleMaxHeight > 0) scaleMaxHeight else getMaxGradientHeight()

    val _targetView: View?
        get() = scaleTargetView ?: childView

    val _scale: Float
        get() = clamp(1f + behaviorScrollY * 1f / _maxHeight * scaleFactor, 1f, maxScale)

    var _targetViewId: Int = View.NO_ID

    init {
        showLog = false

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
        scaleThreshold = array.getFloat(
            R.styleable.LinkageScaleBehavior_Layout_layout_scale_threshold,
            scaleThreshold
        )
        _targetViewId =
            array.getResourceId(R.styleable.LinkageScaleBehavior_Layout_layout_scale_view_id, -1)

        array.recycle()
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        super.layoutDependsOn(parent, child, dependency)
        scaleTargetView = child.findViewById(_targetViewId)
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
        if (enableHeightEffect && _targetDefaultHeight > 0) {

            val childHeight = max(_scale - 1f, 0f) * _targetDefaultHeight + _targetDefaultHeight
            child.layoutParams.height = childHeight.toInt()

            parent.onMeasureChild(
                child,
                parentWidthMeasureSpec,
                widthUsed,
                parentHeightMeasureSpec,
                heightUsed
            )

            return true
        } else {
            return false
        }
    }

    //第一次布局后, 保存target的高度, 方便之后恢复. 如果之后手动修改过target的高度, 请同时修改此值
    var _targetDefaultHeight = -1

    override fun onMeasureChildAfter(
        parent: CoordinatorLayout,
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ) {
        super.onMeasureChildAfter(
            parent,
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed
        )
        if (_targetDefaultHeight < 0) {
            _targetDefaultHeight = _targetView.mH()
        }
    }

    override fun onBehaviorScrollTo(scrollBehavior: BaseScrollBehavior<*>, x: Int, y: Int) {
        if (y >= 0) {
            val oldY = behaviorScrollY
            super.onBehaviorScrollTo(scrollBehavior, x, y)
            if (enableHeightEffect && oldY != y) {
                _targetView?.apply {
                    post {
                        requestLayout()
                    }
                }
            }
        } else {
            super.onBehaviorScrollTo(scrollBehavior, x, y)
        }
    }

    override fun getMaxGradientHeight(): Int {
        return if (_targetDefaultHeight > 0) _targetDefaultHeight else _targetView.mH()
    }

    override fun onGradient(percent: Float) {
        //Log.i("angcyo", "" + percent)
        if (enableScaleEffect) {
            if (percent >= scaleThreshold) {
                _targetView?.apply {
                    scaleX = min(1 + percent - scaleThreshold, maxScale)
                    scaleY = scaleX
                }
            } else {
                _targetView?.apply {
                    scaleX = 1f
                    scaleY = 1f
                }
            }
        }
    }
}