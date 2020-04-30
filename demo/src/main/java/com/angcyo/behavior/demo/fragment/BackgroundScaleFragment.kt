package com.angcyo.behavior.demo.fragment

import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import com.angcyo.behavior.demo.BaseVpFragment
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.demo.dslitem.AppTextItem
import com.angcyo.behavior.demo.loadTextItem
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.dpi
import com.angcyo.dsladapter.renderEmptyItem
import com.angcyo.dsladapter.updateNow
import com.angcyo.item.getColor

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/30
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */

class BackgroundScaleFragment : BaseVpFragment() {
    init {
        fragmentLayoutId = R.layout.fragment_background_scale
    }

    override fun DslAdapter.renderHeaderAdapter() {
        //通过 com.angcyo.behavior.linkage.LinkageScaleBehavior 实现的背景放大
        //需要在头部布局预留出背景可见的区域大小
        //这种方式有几点缺点:
        //1:头部布局必须使用透明颜色, 防止挡住背景
        //2:背景无法触发Touch事件

        renderEmptyItem(180 * dpi)

        AppTextItem()() {
            itemText = "列表顶部"
            configTextStyle {
                textGravity = Gravity.CENTER
            }
        }
        loadTextItem {
            itemBackgroundDrawable = ColorDrawable(getColor(R.color.bg_primary_color))
        }
        AppTextItem()() {
            itemText = "列表底部"
            configTextStyle {
                textGravity = Gravity.CENTER
            }
        }
        updateNow()
    }
}