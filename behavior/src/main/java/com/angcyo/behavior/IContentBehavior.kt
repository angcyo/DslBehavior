package com.angcyo.behavior

/**
 * 内容行为协调
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/06
 */
interface IContentBehavior {

    /**获取内容滚动的Y值*/
    fun getContentScrollY(behavior: BaseDependsBehavior<*>): Int
}