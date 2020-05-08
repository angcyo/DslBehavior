package com.angcyo.behavior.demo.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.angcyo.behavior.HideTitleBarBehavior
import com.angcyo.behavior.demo.BaseDslFragment
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.demo.behavior.TextRefreshHeaderBehavior
import com.angcyo.behavior.refresh.RefreshContentBehavior
import com.angcyo.behavior.setBehavior

/**
 * 刷新回调的演示
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/29
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class RefreshFragment : BaseDslFragment() {

    init {
        fragmentLayoutId = R.layout.fragment_refresh
    }

    override fun initBaseView(rootView: View, savedInstanceState: Bundle?) {
        super.initBaseView(rootView, savedInstanceState)
        rootView.findViewById<View>(R.id.recycler_view)
            ?.setBehavior(RefreshContentBehavior(fContext()).apply {
                refreshAction = {
                    Toast.makeText(fContext(), "正在刷新...", Toast.LENGTH_SHORT).show()

                    //1秒后, 完成刷新
                    rootView.postDelayed({

                        dslAdapter.apply {
                            clearItems()
                            renderDslAdapter(this)
                        }

                        finishRefresh()
                    }, 1000)
                }
            })

        rootView.findViewById<View>(R.id.toolbar)?.setBehavior(HideTitleBarBehavior(fContext()))

        rootView.findViewById<View>(R.id.refresh_header_layout)
            ?.setBehavior(TextRefreshHeaderBehavior(fContext()))
    }
}