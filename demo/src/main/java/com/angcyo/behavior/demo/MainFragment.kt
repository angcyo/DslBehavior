package com.angcyo.behavior.demo

import com.angcyo.base.dslFHelper
import com.angcyo.behavior.demo.dslitem.AppTextItem
import com.angcyo.behavior.demo.fragment.RefreshEffectFragment
import com.angcyo.behavior.demo.fragment.RefreshFragment
import com.angcyo.dsladapter.DslAdapter

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/29
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class MainFragment : BaseDslFragment() {
    override fun DslAdapter.renderDslAdapter() {
        AppTextItem()() {
            itemText = "刷新效果演示"
            itemClick = {
                dslFHelper {
                    show(RefreshEffectFragment::class.java)
                }
            }
        }

        AppTextItem()() {
            itemText = "刷新回调演示"
            itemClick = {
                dslFHelper {
                    show(RefreshFragment::class.java)
                }
            }
        }
    }
}