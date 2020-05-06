package com.angcyo.behavior.demo.behavior

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import android.widget.TextView
import com.angcyo.behavior.BaseScrollBehavior
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.mH
import com.angcyo.behavior.refresh.IRefreshBehavior
import com.angcyo.behavior.refresh.RefreshHeaderBehavior
import kotlin.math.min

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/29
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class TextRefreshHeaderBehavior(context: Context, attributeSet: AttributeSet? = null) :
    RefreshHeaderBehavior(context, attributeSet) {

    override fun onContentScrollTo(contentBehavior: BaseScrollBehavior<*>, x: Int, y: Int) {
        super.onContentScrollTo(contentBehavior, x, y)

        if (_refreshBehaviorStatus == IRefreshBehavior.STATUS_NORMAL) {

            val maxHeight = childView.mH()
            val progress = (y * 1f / maxHeight * 100).toInt()

            childView?.findViewById<ProgressBar>(R.id.progress_bar)?.apply {
                isIndeterminate = false
                this.progress = min(progress, 100)
            }

            childView?.findViewById<TextView>(R.id.text_view)?.apply {
                text = if (progress >= 100) {
                    "释放刷新"
                } else {
                    "下拉刷新"
                }
            }
        }
    }

    override fun onContentStopScroll(contentBehavior: BaseScrollBehavior<*>) {
        super.onContentStopScroll(contentBehavior)
    }

    override fun onRefreshStatusChange(contentBehavior: BaseScrollBehavior<*>, from: Int, to: Int) {
        if (to < IRefreshBehavior.STATUS_FINISH) {
            super.onRefreshStatusChange(contentBehavior, from, to)
        }

        childView?.findViewById<ProgressBar>(R.id.progress_bar)?.apply {
            if (to == IRefreshBehavior.STATUS_REFRESH) {
                isIndeterminate = true

                childView?.findViewById<TextView>(R.id.text_view)?.apply {
                    text = "正在刷新"
                }

            } else {
                isIndeterminate = false
                if (to >= IRefreshBehavior.STATUS_FINISH) {

                    childView?.findViewById<TextView>(R.id.text_view)?.apply {
                        text = "刷新完成"

                        //延迟关闭刷新状态
                        postDelayed({
                            super.onRefreshStatusChange(contentBehavior, from, to)
                        }, 700)
                    }

                    progress = 50
                }
            }
        }
    }
}