package com.angcyo.behavior.refresh

/**
 * 刷新内容的行为接口
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/07
 */

interface IRefreshContentBehavior {

    /**刷新行为的支持*/
    var refreshBehaviorConfig: IRefreshBehavior?

    /**触发了刷新回调*/
    var refreshAction: (IRefreshContentBehavior) -> Unit

    /**刷新完成后, 需要重置滚动到的y轴距离*/
    fun getRefreshResetScrollY(): Int

    /**OverScroll时, 当前滚动距离*/
    fun getRefreshCurrentScrollY(dy: Int): Int

    /**OverScroll时, 最大滚动距离*/
    fun getRefreshMaxScrollY(dy: Int): Int

    /**设置刷新状态*/
    fun setRefreshContentStatus(status: Int)
}