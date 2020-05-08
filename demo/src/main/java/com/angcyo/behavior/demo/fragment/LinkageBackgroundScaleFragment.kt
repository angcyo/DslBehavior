package com.angcyo.behavior.demo.fragment

import com.angcyo.behavior.demo.BaseVpFragment
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.demo.dslitem.AppUserInfoItem
import com.angcyo.dsladapter.DslAdapter

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/30
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */

class LinkageBackgroundScaleFragment : BaseVpFragment() {
    init {
        fragmentLayoutId = R.layout.fragment_linkage_background_scale
    }

    override fun renderHeaderAdapter(adapter: DslAdapter) {
        //通过 com.angcyo.behavior.linkage.LinkageScaleBehavior 实现的背景放大
        //需要在头部布局预留出背景可见的区域大小
        //这种方式有几点缺点:
        //1:头部布局必须使用透明颜色, 防止挡住背景
        //2:背景无法触发Touch事件

        adapter.apply {
            AppUserInfoItem()()
        }

        super.renderHeaderAdapter(adapter)
    }
}