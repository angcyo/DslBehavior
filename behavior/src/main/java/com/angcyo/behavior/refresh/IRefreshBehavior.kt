package com.angcyo.behavior.refresh

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.angcyo.behavior.BaseScrollBehavior
import com.angcyo.behavior.offsetTopTo

/**
 * 刷新行为[RefreshContentBehavior]的UI效果处理类
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/07
 */

interface IRefreshBehavior {

    companion object {
        //正常状态
        const val STATUS_NORMAL = 0

        //刷新状态
        const val STATUS_REFRESH = 1

        //刷新完成
        const val STATUS_FINISH = 10
    }

    /**刷新状态*/
    var _refreshBehaviorStatus: Int

    /**请调用此方法设置[_refreshBehaviorStatus]状态*/
    fun onSetRefreshBehaviorStatus(contentBehavior: BaseScrollBehavior<*>, newStatus: Int) {
        val old = _refreshBehaviorStatus
        if (old != newStatus) {
            _refreshBehaviorStatus = newStatus
            onRefreshStatusChange(contentBehavior, old, newStatus)
        }
    }

    /**当内容布局后, 用于保存一些需要初始化的变量*/
    fun onContentLayout(
        contentBehavior: BaseScrollBehavior<*>,
        parent: CoordinatorLayout,
        child: View
    ) {

    }

    /**当内容滚动时, 界面需要处理的回调*/
    fun onContentScrollTo(contentBehavior: BaseScrollBehavior<*>, x: Int, y: Int) {
        contentBehavior.childView?.offsetTopTo(y + contentBehavior.behaviorOffsetTop)
    }

    /**当内容over滚动时回调, 同样会触发[onContentScrollTo]*/
    fun onContentOverScroll(contentBehavior: BaseScrollBehavior<*>, dx: Int, dy: Int) {
        contentBehavior.scrollBy(0, -dy)
    }

    /**内容停止了滚动, 此时需要恢复界面*/
    fun onContentStopScroll(contentBehavior: BaseScrollBehavior<*>) {
        if (_refreshBehaviorStatus != STATUS_REFRESH && !contentBehavior.isTouchHold) {

            val resetScrollX = 0
            val resetScrollY = if (contentBehavior is IRefreshContentBehavior) {
                contentBehavior.getRefreshResetScrollY()
            } else {
                0
            }

            contentBehavior.startScrollTo(resetScrollX, resetScrollY)
        }
    }

    /**刷新状态改变,[touchHold]还处于[touch]状态*/
    fun onRefreshStatusChange(contentBehavior: BaseScrollBehavior<*>, from: Int, to: Int) {
        if (!contentBehavior.isTouchHold) {
            val resetScrollX = 0
            val resetScrollY = if (contentBehavior is IRefreshContentBehavior) {
                contentBehavior.getRefreshResetScrollY()
            } else {
                0
            }

            contentBehavior.startScrollTo(resetScrollX, resetScrollY)
        }
    }

    /**需要触发刷新回调*/
    fun onRefreshAction(contentBehavior: BaseScrollBehavior<*>) {
        if (contentBehavior is IRefreshContentBehavior) {
            contentBehavior.refreshAction(contentBehavior)
        }
    }
}