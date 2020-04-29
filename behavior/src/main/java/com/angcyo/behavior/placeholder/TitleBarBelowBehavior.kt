package com.angcyo.behavior.placeholder

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.angcyo.behavior.BaseDependsBehavior
import com.angcyo.behavior.ITitleBarBehavior
import com.angcyo.behavior.behavior
import com.angcyo.behavior.offsetTopTo

/**
 * 布局在标题栏下的行为, 可以覆盖在内容上, 也可以被内容覆盖
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/03/01
 */

open class TitleBarBelowBehavior(
    context: Context? = null,
    attrs: AttributeSet? = null
) : BaseDependsBehavior<View>(context, attrs) {

    /**标题栏的行为, 用于布局在标题栏bottom里面*/
    var titleBarPlaceholderBehavior: ITitleBarBehavior? = null

    /**[child]需要排除多少高度*/
    val excludeHeight get() = titleBarPlaceholderBehavior?.getContentExcludeHeight(this) ?: 0

    var _titleBarView: View? = null

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        val behavior = dependency.behavior()
        behavior?.let {
            if (it is ITitleBarBehavior) {
                titleBarPlaceholderBehavior = it
                _titleBarView = dependency
            }
        }

        super.layoutDependsOn(parent, child, dependency)

        return behavior is ITitleBarBehavior
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        super.onDependentViewChanged(parent, child, dependency)
        val top = dependency.bottom
        child.layout(child.left, top, child.right, top + child.measuredHeight)
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

        parent.onMeasureChild(
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed + excludeHeight
        )

        return true
    }

    override fun onLayoutChildAfter(parent: CoordinatorLayout, child: View, layoutDirection: Int) {
        super.onLayoutChildAfter(parent, child, layoutDirection)
        child.offsetTopTo(_titleBarView?.bottom ?: 0)
    }
}