package com.angcyo.behavior.demo.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.angcyo.behavior.behavior
import com.angcyo.behavior.demo.BaseDslFragment
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.demo.dslitem.AppBackgroundItem
import com.angcyo.behavior.demo.dslitem.AppTextItem
import com.angcyo.behavior.demo.loadTextItem
import com.angcyo.behavior.refresh.IRefreshContentBehavior
import com.angcyo.behavior.refresh.ScaleHeaderRefreshEffectConfig
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.updateNow

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/05/06
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class BackgroundScaleTouchFragment : BaseDslFragment() {
    init {
        fragmentLayoutId = R.layout.fragment_background_scale_touch
    }

    override fun initBaseView(rootView: View, savedInstanceState: Bundle?) {
        super.initBaseView(rootView, savedInstanceState)
        rootView.findViewById<View>(R.id.recycler_view)?.behavior()?.apply {
            if (this is IRefreshContentBehavior) {
                this.refreshBehaviorConfig = ScaleHeaderRefreshEffectConfig()
            }
        }
    }

    override fun DslAdapter.renderDslAdapter() {
        AppBackgroundItem()()

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