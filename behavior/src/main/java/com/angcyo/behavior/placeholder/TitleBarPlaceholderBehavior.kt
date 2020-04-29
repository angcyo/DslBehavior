package com.angcyo.behavior.placeholder

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.angcyo.behavior.BaseDependsBehavior
import com.angcyo.behavior.ITitleBarBehavior
import com.angcyo.behavior.mH

/**
 * 单纯用来标识当前[child]是什么行为
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/12/31
 */
class TitleBarPlaceholderBehavior(context: Context? = null, attributeSet: AttributeSet? = null) :
    BaseDependsBehavior<View>(context, attributeSet), ITitleBarBehavior {

    override fun getContentExcludeHeight(behavior: BaseDependsBehavior<*>): Int {
        return childView.mH()
    }

    override fun getContentOffsetTop(behavior: BaseDependsBehavior<*>): Int {
        return childView.mH()
    }
}