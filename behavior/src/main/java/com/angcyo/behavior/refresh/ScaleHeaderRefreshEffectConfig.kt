package com.angcyo.behavior.refresh

import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.angcyo.behavior.BaseScrollBehavior
import com.angcyo.behavior.setHeight

/**
 * 当[Content]over滚动的时候, 缩放指定的[View].
 *
 * 不要太频繁调用requestLayout
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/03/08
 */

open class ScaleHeaderRefreshEffectConfig : RefreshEffectConfig() {

    /**当 滚动距离/原始target,到达一定比例时, 开始高度开启缩放, 否则只改变高度. 负数表示关闭*/
    var scaleThreshold: Float = 0.3f

    /**目标view, 在content ViewGroup中的index*/
    var targetViewIndexInContent: Int = 0

    var targetViewIdInContent: Int = -1

    /**获取目标View*/
    var getTargetView: (behavior: BaseScrollBehavior<*>) -> View? = { behavior ->
        when {
            targetViewIdInContent > 0 -> behavior.childView?.findViewById(targetViewIdInContent)
            behavior.childView is ViewGroup -> (behavior.childView as ViewGroup).getChildAt(
                targetViewIndexInContent
            )
            else -> null
        }
    }

    //记录默认值
    var _defaultLayoutParams: ViewGroup.LayoutParams? = null
    var _defaultTargetHeight: Int = -1

    override fun onContentLayout(
        contentBehavior: BaseScrollBehavior<*>,
        parent: CoordinatorLayout,
        child: View
    ) {
        super.onContentLayout(contentBehavior, parent, child)
        getTargetView(contentBehavior)?.apply {
            if (contentBehavior.behaviorScrollY == 0 && _defaultLayoutParams == null) {
                _defaultLayoutParams = layoutParams
                _defaultTargetHeight = measuredHeight
            }
        }
    }

    override fun onContentScrollTo(contentBehavior: BaseScrollBehavior<*>, x: Int, y: Int) {
        if (_defaultTargetHeight > 0) {
            if (y > 0) {
                //L.i("$_defaultTargetHeight $y")
                //当内容需要向下滚动时, 改变目标view的高度
                getTargetView(contentBehavior)?.apply {
                    val height = _defaultTargetHeight + y
                    post {
                        setHeight(height)
                    }
                    val ratio = y * 1f / _defaultTargetHeight

                    if (ratio >= scaleThreshold) {
                        scaleX = 1f + ratio - scaleThreshold
                        scaleY = scaleX
                    } else {
                        scaleX = 1f
                        scaleY = scaleX
                    }
                }
            } /*else if (y == 0) {
            onGetTargetView(behavior)?.apply {
                _defaultLayoutParams?.also {
                    layoutParams = it
                }
                _defaultLayoutParams = null
            }
        } */ else {
                super.onContentScrollTo(contentBehavior, x, y)
            }
        } else {
            onContentLayout(
                contentBehavior,
                contentBehavior.parentLayout!!,
                contentBehavior.childView!!
            )
            super.onContentScrollTo(contentBehavior, x, y)
        }
    }
}