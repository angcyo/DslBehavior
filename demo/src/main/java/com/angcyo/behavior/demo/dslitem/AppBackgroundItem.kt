package com.angcyo.behavior.demo.dslitem

import android.widget.Toast
import com.angcyo.behavior.demo.R
import com.angcyo.dsladapter.DslAdapterItem
import com.angcyo.dsladapter.dpi

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/05/06
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class AppBackgroundItem : DslAdapterItem() {
    init {
        itemLayoutId = R.layout.layout_background
        itemHeight = 200 * dpi
        itemClick = {
            Toast.makeText(it.context, "点击...", Toast.LENGTH_SHORT).show()
        }
    }
}