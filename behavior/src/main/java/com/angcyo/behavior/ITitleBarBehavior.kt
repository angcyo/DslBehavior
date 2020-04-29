package com.angcyo.behavior

/**
 * 标题栏协调行为
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/12/31
 */
interface ITitleBarBehavior {

    /**获取内容布局需要排除的高度*/
    fun getContentExcludeHeight(behavior: BaseDependsBehavior<*>): Int

    /**获取内容布局开始布局的位置*/
    fun getContentOffsetTop(behavior: BaseDependsBehavior<*>): Int
}