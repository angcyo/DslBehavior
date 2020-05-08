package com.angcyo.behavior.demo.fragment

import android.os.Bundle
import android.view.View
import com.angcyo.behavior.demo.BaseDslFragment
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.demo.dslitem.AppTextItem
import com.angcyo.behavior.demo.loadTextItem
import com.angcyo.behavior.placeholder.TitleBarPlaceholderBehavior
import com.angcyo.behavior.setBehavior
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.updateNow

/**
 * 只有刷新效果(上拉和下拉), 没有刷新回调的演示
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/29
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class RefreshEffectFragment : BaseDslFragment() {
    override fun initBaseView(rootView: View, savedInstanceState: Bundle?) {
        super.initBaseView(rootView, savedInstanceState)
        rootView.findViewById<View>(R.id.toolbar)?.setBehavior(TitleBarPlaceholderBehavior())
    }

    override fun renderDslAdapter(adapter: DslAdapter) {
        adapter.apply {
            AppTextItem()() {
                itemText = "到了边界, 继续下拉试试?"
            }
            loadTextItem()
            AppTextItem()() {
                itemText = "到了边界, 继续上拉试试?"
            }
            updateNow()
        }
    }
}