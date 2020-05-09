package com.angcyo.behavior.linkage

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.angcyo.behavior.behavior
import com.angcyo.behavior.offsetTopTo

/**
 * 头/悬浮/尾 联动滚动, 悬浮的行为
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/03/20
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
class LinkageStickyBehavior(
    context: Context,
    attributeSet: AttributeSet? = null
) : BaseLinkageBehavior(context, attributeSet) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        super.layoutDependsOn(parent, child, dependency)
        val dependencyBehavior = dependency.behavior()
        return if (dependencyBehavior is LinkageHeaderBehavior) {
            dependsView = dependency
            true
        } else {
            false
        }
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val result = super.onDependentViewChanged(parent, child, dependency)
        if (linkageHeaderBehavior?.isStickyHoldScroll != true) {
            child.offsetTopTo(dependency.bottom)
        }
        return result
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View, dependency: View) {
        super.onDependentViewRemoved(parent, child, dependency)
    }

    override fun onLayoutChildAfter(parent: CoordinatorLayout, child: View, layoutDirection: Int) {
        super.onLayoutChildAfter(parent, child, layoutDirection)
        dependsView?.apply {
            if (linkageHeaderBehavior?.isStickyHoldScroll != true) {
                child.offsetTopTo(bottom)
            }
        }
    }
}