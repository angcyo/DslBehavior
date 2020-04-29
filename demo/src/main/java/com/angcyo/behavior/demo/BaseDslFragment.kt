package com.angcyo.behavior.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
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
            renderDslAdapter()
        }
    }

    open fun DslAdapter.renderDslAdapter() {
        loadTextItem()
        updateNow()
    }
}