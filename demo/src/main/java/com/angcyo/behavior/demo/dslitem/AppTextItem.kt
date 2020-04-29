package com.angcyo.behavior.demo.dslitem

import com.angcyo.behavior.demo.R
import com.angcyo.item.DslTextItem
import com.angcyo.widget.span.dpi

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/29
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class AppTextItem : DslTextItem() {
    init {
        itemLayoutId = R.layout.item_app_text
        itemBottomInsert = 1 * dpi
    }
}