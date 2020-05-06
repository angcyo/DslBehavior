package com.angcyo.behavior.demo.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.angcyo.behavior.behavior
import com.angcyo.behavior.demo.BaseVpFragment
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.demo.dslitem.AppBackgroundItem
import com.angcyo.behavior.demo.dslitem.AppTextItem
import com.angcyo.behavior.demo.loadTextItem
import com.angcyo.behavior.refresh.IRefreshContentBehavior
import com.angcyo.behavior.refresh.ScaleHeaderRefreshEffectConfig
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.updateNow
import com.angcyo.item.getColor

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/05/06
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class LinkageBackgroundScaleTouchFragment : BaseVpFragment() {
    init {
        fragmentLayoutId = R.layout.fragment_linkage_background_scale_touch
    }

    override fun initBaseView(rootView: View, savedInstanceState: Bundle?) {
        super.initBaseView(rootView, savedInstanceState)
        rootView.findViewById<View>(R.id.header_wrap_layout)?.behavior()?.apply {
            if (this is IRefreshContentBehavior) {
                this.refreshBehaviorConfig = ScaleHeaderRefreshEffectConfig().apply {
                    getTargetView = {
                        rootView.findViewById<ViewGroup>(R.id.header_recycler_view)?.getChildAt(0)
                    }
                }
            }
        }
    }

    override fun DslAdapter.renderHeaderAdapter() {
        AppBackgroundItem()()

        AppTextItem()() {
            itemBottomInsert = 0
            itemText = "列表顶部"
            configTextStyle {
                textGravity = Gravity.CENTER
            }
        }
        loadTextItem {
            itemBackgroundDrawable = ColorDrawable(getColor(R.color.bg_primary_color))
        }
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