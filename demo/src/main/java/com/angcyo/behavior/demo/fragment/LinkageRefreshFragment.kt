package com.angcyo.behavior.demo.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.angcyo.behavior.BaseScrollBehavior
import com.angcyo.behavior.behavior
import com.angcyo.behavior.demo.BaseVpFragment
import com.angcyo.behavior.demo.R
import com.angcyo.behavior.refresh.IRefreshBehavior
import com.angcyo.behavior.refresh.IRefreshContentBehavior

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/05/06
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class LinkageRefreshFragment : BaseVpFragment() {
    init {
        fragmentLayoutId = R.layout.fragment_linkage_refresh
    }

    override fun initBaseView(rootView: View, savedInstanceState: Bundle?) {
        super.initBaseView(rootView, savedInstanceState)
        rootView.findViewById<View>(R.id.header_wrap_layout)?.behavior()?.apply {
            if (this is IRefreshContentBehavior) {
                refreshAction = {
                    Toast.makeText(fContext(), "正在刷新...", Toast.LENGTH_SHORT).show()

                    //1秒后, 完成刷新
                    rootView.postDelayed({

                        headerDslAdapter.apply {
                            clearItems()
                            renderHeaderAdapter(this)
                        }

                        refreshBehaviorConfig?.onSetRefreshBehaviorStatus(
                            this as BaseScrollBehavior<*>,
                            IRefreshBehavior.STATUS_FINISH
                        )
                    }, 1000)
                }
            }
        }
    }
}