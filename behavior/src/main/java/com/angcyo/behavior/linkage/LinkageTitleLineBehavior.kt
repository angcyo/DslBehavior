package com.angcyo.behavior.linkage

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.angcyo.behavior.*

/**
 * 布局在ITitleBarBehavior下面, 根据内容滚动距离决定是否隐藏或者显示child
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020-4-29
 */
open class LinkageTitleLineBehavior(
    context: Context,
    attributeSet: AttributeSet? = null
) : BaseLinkageGradientBehavior(context, attributeSet) {

    /**当内容滚动了大于等于多少比例(标题栏的高度/滚动距离)时, 显示child*/
    var titleLineShowThreshold: Float = 1f

    var _titleBarView: View? = null

    init {
        val array =
            context.obtainStyledAttributes(
                attributeSet,
                R.styleable.LinkageTitleLineBehavior_Layout
            )
        titleLineShowThreshold = array.getFloat(
            R.styleable.LinkageTitleLineBehavior_Layout_layout_title_line_show_threshold,
            titleLineShowThreshold
        )
        array.recycle()
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        val behavior = dependency.behavior()
        behavior?.let {
            if (it is ITitleBarBehavior) {
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

    override fun onLayoutChildAfter(parent: CoordinatorLayout, child: View, layoutDirection: Int) {
        super.onLayoutChildAfter(parent, child, layoutDirection)
        child.offsetTopTo(_titleBarView?.bottom ?: 0)
    }

    override fun getMaxGradientHeight() = (_titleBarView ?: childView).mH()

    /**开始渐变*/
    override fun onGradient(percent: Float) {
        //GONE后的view, 收不到内嵌滚动事件.所以这里使用INVISIBLE
        if (childView?.isInEditMode == false) {
            if (percent > 0) {
                childView?.invisible(true)
            } else {
                childView?.invisible(-percent < titleLineShowThreshold)
            }
        }
    }
}