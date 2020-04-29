package com.angcyo.behavior.refresh

import android.content.Context
import android.util.AttributeSet
import com.angcyo.behavior.BaseScrollBehavior

/**
 * 刷新效果的行为, 只有效果, 不触发回调.
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/07
 */

open class RefreshEffectBehavior(context: Context, attributeSet: AttributeSet? = null) :
    RefreshHeaderBehavior(context, attributeSet) {

    override fun onContentOverScroll(contentBehavior: BaseScrollBehavior<*>, dx: Int, dy: Int) {
        _refreshEffectConfig.onContentOverScroll(contentBehavior, dx, dy)
    }

    override fun onContentStopScroll(contentBehavior: BaseScrollBehavior<*>) {
        if (!contentBehavior.isTouchHold) {
            onSetRefreshBehaviorStatus(contentBehavior, IRefreshBehavior.STATUS_NORMAL)
            contentBehavior.startScrollTo(0, 0)
        }
    }
}