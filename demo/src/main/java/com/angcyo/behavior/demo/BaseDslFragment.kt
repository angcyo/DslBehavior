package com.angcyo.behavior.demo

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.angcyo.behavior.demo.dslitem.AppTextItem
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.initDslAdapter
import com.angcyo.dsladapter.updateNow
import com.angcyo.fragment.AbsLifecycleFragment

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/28
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
abstract class BaseDslFragment : AbsLifecycleFragment() {

    lateinit var dslAdapter: DslAdapter

    init {
        fragmentLayoutId = R.layout.fragment_dsl
    }

    override fun initBaseView(rootView: View, savedInstanceState: Bundle?) {
        super.initBaseView(rootView, savedInstanceState)
        rootView.findViewById<Toolbar>(R.id.toolbar)?.title = this::class.java.simpleName
        rootView.findViewById<RecyclerView>(R.id.recycler_view)?.initDslAdapter {
            dslAdapter = this
            renderDslAdapter(this)
        }
    }

    fun setTitle(title: CharSequence?) {
        view?.findViewById<Toolbar>(R.id.toolbar)?.title = title
    }

    open fun renderDslAdapter(adapter: DslAdapter) {
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