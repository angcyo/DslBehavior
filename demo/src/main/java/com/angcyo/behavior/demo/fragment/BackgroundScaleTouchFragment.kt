package com.angcyo.behavior.demo.fragment

import android.os.Bundle
import android.view.View
import com.angcyo.behavior.behavior
import com.angcyo.behavior.demo.BaseDslFragment
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.demo.dslitem.AppUserInfoTouchItem
import com.angcyo.behavior.refresh.IRefreshContentBehavior
import com.angcyo.behavior.refresh.ScaleHeaderRefreshEffectConfig
import com.angcyo.dsladapter.DslAdapter

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
                this.refreshBehaviorConfig = ScaleHeaderRefreshEffectConfig().apply {
                    targetViewIdInContent = R.id.image_view
                }
            }
        }
    }

    override fun renderDslAdapter(adapter: DslAdapter) {
        adapter.apply {
            AppUserInfoTouchItem()()
        }

        super.renderDslAdapter(adapter)
    }
}