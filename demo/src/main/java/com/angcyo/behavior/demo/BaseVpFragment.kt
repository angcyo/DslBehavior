package com.angcyo.behavior.demo

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.angcyo.behavior.demo.dslitem.AppTextItem
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.initDslAdapter
import com.angcyo.dsladapter.updateNow
import com.angcyo.fragment.AbsLifecycleFragment
import com.angcyo.tablayout.delegate.ViewPager1Delegate

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/30
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
abstract class BaseVpFragment : AbsLifecycleFragment() {

    lateinit var headerDslAdapter: DslAdapter

    override fun initBaseView(rootView: View, savedInstanceState: Bundle?) {
        super.initBaseView(rootView, savedInstanceState)
        rootView.findViewById<RecyclerView>(R.id.header_recycler_view)?.initDslAdapter {
            headerDslAdapter = this
            renderHeaderAdapter(this)
        }

        rootView.findViewById<ViewPager>(R.id.view_pager)?.apply {
            adapter = RFragmentAdapter(
                childFragmentManager,
                listOf(
                    RecyclerFragment(),
                    RecyclerFragment(),
                    RecyclerFragment(),
                    RecyclerFragment(),
                    RecyclerFragment(),
                    RecyclerFragment()
                )
            )
            ViewPager1Delegate.install(this, rootView.findViewById(R.id.tab_layout))
        }
    }

    open fun renderHeaderAdapter(adapter: DslAdapter) {
        adapter.apply {
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
}