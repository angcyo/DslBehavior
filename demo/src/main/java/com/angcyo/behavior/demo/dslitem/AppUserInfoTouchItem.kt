package com.angcyo.behavior.demo.dslitem

import android.widget.Toast
import com.angcyo.behavior.demo.R
import com.angcyo.dsladapter.DslAdapterItem

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/05/08
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class AppUserInfoTouchItem : DslAdapterItem() {
    init {
        itemLayoutId = R.layout.item_user_info_touch

        itemClick = {
            Toast.makeText(it.context, "点击...", Toast.LENGTH_SHORT).show()
        }
    }
}