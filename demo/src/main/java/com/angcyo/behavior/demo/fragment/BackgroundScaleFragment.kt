package com.angcyo.behavior.demo.fragment

import android.view.Gravity
import com.angcyo.behavior.demo.BaseDslFragment
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.demo.dslitem.AppTextItem
import com.angcyo.behavior.demo.loadTextItem
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.dpi
import com.angcyo.dsladapter.renderEmptyItem
import com.angcyo.dsladapter.updateNow

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/05/06
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class BackgroundScaleFragment : BaseDslFragment() {
    init {
        fragmentLayoutId = R.layout.fragment_background_scale
    }

    override fun DslAdapter.renderDslAdapter() {
        renderEmptyItem(180 * dpi)

        AppTextItem()() {
            itemBottomInsert = 0
            itemText = "列表顶部"
            configTextStyle {
                textGravity = Gravity.CENTER
            }
        }
        loadTextItem()
        AppTextItem()() {
            itemBottomInsert = 0
            itemText = "列表底部"
            configTextStyle {
                textGravity = Gravity.CENTER
            }
        }
        updateNow()
    }
}