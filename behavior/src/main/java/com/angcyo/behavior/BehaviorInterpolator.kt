package com.angcyo.behavior

/**
 * 数值差值器
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/01
 */

interface BehaviorInterpolator {
    fun getInterpolation(scroll: Int, input: Int, max: Int): Int {
        return input
    }
}