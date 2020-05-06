package com.angcyo.behavior

/**
 * [BaseScrollBehavior]滚动监听
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/01
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */

interface IScrollBehaviorListener {

    /**[com.angcyo.behavior.BaseScrollBehavior.scrollTo]*/
    fun onBehaviorScrollTo(scrollBehavior: BaseScrollBehavior<*>, x: Int, y: Int)
}