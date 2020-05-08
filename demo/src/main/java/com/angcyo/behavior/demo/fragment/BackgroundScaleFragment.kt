package com.angcyo.behavior.demo.fragment

import com.angcyo.behavior.demo.BaseDslFragment
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.demo.dslitem.AppUserInfoItem
import com.angcyo.dsladapter.DslAdapter

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

    override fun renderDslAdapter(adapter: DslAdapter) {
        adapter.apply {
            AppUserInfoItem()()
        }

        super.renderDslAdapter(adapter)
    }
}