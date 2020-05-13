package com.angcyo.behavior.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.angcyo.behavior.BaseScrollBehavior
import com.angcyo.behavior.IContentBehavior
import com.angcyo.behavior.behavior
import com.angcyo.behavior.mH
import com.angcyo.behavior.refresh.IRefreshBehavior.Companion.STATUS_FINISH
import com.angcyo.behavior.refresh.IRefreshBehavior.Companion.STATUS_NORMAL
import com.angcyo.behavior.refresh.IRefreshBehavior.Companion.STATUS_REFRESH

/**
 * 布局的位置在[IContentBehavior]的上面.
 * 标准的刷新回调回调, 只是简单的将刷新控件滚动到界面上和滚动到界面外.
 * 如果需要更细粒度的控制滚动进度之类的, 请继承此类重写控制方法.
 *
 * 所以这个行为可以用来实现, 不需要细粒度控制视图, 常规布局的自定义刷新头行为.
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/07
 */

open class RefreshHeaderBehavior(context: Context, attributeSet: AttributeSet? = null) :
    BaseScrollBehavior<View>(context, attributeSet), IRefreshBehavior {

    override var _refreshBehaviorStatus: Int = STATUS_NORMAL

    //为了阻尼效果的算法
    var _refreshEffectConfig = RefreshEffectConfig()

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        super.layoutDependsOn(parent, child, dependency)
        return enableDependsOn && dependency.behavior() is IContentBehavior
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        behaviorOffsetTop = dependency.top - child.measuredHeight
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onContentOverScroll(contentBehavior: BaseScrollBehavior<*>, dx: Int, dy: Int) {
        _refreshEffectConfig.onContentOverScroll(contentBehavior, dx, dy)
    }

    override fun onContentStopScroll(contentBehavior: BaseScrollBehavior<*>) {
        //L.i("${behavior.refreshStatus} $touchHold ${behavior.scrollY}")

        val resetScrollX = 0
        val resetScrollY = if (contentBehavior is IRefreshContentBehavior) {
            contentBehavior.getRefreshResetScrollY()
        } else {
            0
        }

        when (_refreshBehaviorStatus) {
            STATUS_FINISH -> {
                //刷新完成状态, 手势放开
                onSetRefreshBehaviorStatus(contentBehavior, STATUS_NORMAL)
                contentBehavior.startScrollTo(resetScrollX, resetScrollY)
            }
            STATUS_REFRESH -> {
                if (!contentBehavior.isTouchHold) {
                    //刷新状态, 手势放开
                    if (contentBehavior.behaviorScrollY >= childView.mH()) {
                        contentBehavior.startScrollTo(resetScrollX, childView.mH())
                    } else {
                        contentBehavior.startScrollTo(resetScrollX, resetScrollY)
                    }
                }
            }
            STATUS_NORMAL -> {
                if (!contentBehavior.isTouchHold) {
                    //正常状态, 手势放开
                    if (contentBehavior.behaviorScrollY >= childView.mH()) {
                        //触发刷新, 切换刷新状态
                        onSetRefreshBehaviorStatus(contentBehavior, STATUS_REFRESH)
                    } else {
                        //滚到默认
                        contentBehavior.startScrollTo(resetScrollX, resetScrollY)
                    }
                }
            }
            else -> {
                onSetRefreshBehaviorStatus(contentBehavior, STATUS_NORMAL)
            }
        }
    }

    override fun onRefreshStatusChange(contentBehavior: BaseScrollBehavior<*>, from: Int, to: Int) {
        val resetScrollX = 0
        val resetScrollY = if (contentBehavior is IRefreshContentBehavior) {
            contentBehavior.getRefreshResetScrollY()
        } else {
            0
        }

        when (to) {
            STATUS_REFRESH -> {
                if (!contentBehavior.isTouchHold) {
                    contentBehavior.startScrollTo(0, childView.mH())
                }
                onRefreshAction(contentBehavior)
            }
            STATUS_FINISH -> {
                //可以提示一些UI, 然后再[Scroll]
                if (!contentBehavior.isTouchHold) {
                    contentBehavior.startScrollTo(resetScrollX, resetScrollY)
                    _refreshBehaviorStatus = STATUS_NORMAL
                }
            }
            else -> {
                if (!contentBehavior.isTouchHold) {
                    contentBehavior.startScrollTo(resetScrollX, resetScrollY)
                    _refreshBehaviorStatus = STATUS_NORMAL
                }
            }
        }
    }
}