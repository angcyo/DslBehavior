package com.angcyo.behavior.demo

import com.angcyo.base.dslFHelper
import com.angcyo.behavior.demo.dslitem.AppTextItem
import com.angcyo.behavior.demo.fragment.GradientTitleFragment
import com.angcyo.behavior.demo.fragment.RefreshEffectFragment
import com.angcyo.behavior.demo.fragment.RefreshFragment
import com.angcyo.behavior.demo.fragment.TitleLineFragment
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.dpi
import com.angcyo.item.DslBottomItem

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

        AppTextItem()() {
            itemText = "智能阴影演示"
            itemClick = {
                dslFHelper {
                    show(TitleLineFragment::class.java)
                }
            }
        }

        AppTextItem()() {
            itemText = "标题栏渐变+智能阴影演示"
            itemClick = {
                dslFHelper {
                    show(GradientTitleFragment::class.java)
                }
            }
        }

        DslBottomItem()() {
            itemText = "演示的数据随机生成, 如果数据量不足以触发滚动时, 请重新打开试试."
            itemPaddingBottom = 60 * dpi
        }
    }
}