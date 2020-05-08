package com.angcyo.behavior.demo

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.angcyo.base.dslFHelper
import com.angcyo.behavior.demo.dslitem.AppTextItem
import com.angcyo.behavior.demo.fragment.*
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.dpi
import com.angcyo.dsladapter.filter.batchLoad
import com.angcyo.dsladapter.setWidth
import com.angcyo.item.DslBaseLabelItem
import com.angcyo.item.DslBottomItem
import com.angcyo.widget.span.span

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/29
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */
class MainFragment : BaseDslFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("DslBehavior by angcyo")
    }

    override fun renderDslAdapter(adapter: DslAdapter) {
        adapter.apply {

            AppTextItem()() {
                itemText = "刷新效果演示(上拉/下拉回调)"
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

            AppTextItem()() {
                itemText = "标题栏渐变+智能阴影+背景放大演示"
                itemClick = {
                    dslFHelper {
                        show(BackgroundScaleFragment::class.java)
                    }
                }
            }

            AppTextItem()() {
                itemText = "标题栏渐变+智能阴影+背景放大(带事件)演示"
                itemClick = {
                    dslFHelper {
                        show(BackgroundScaleTouchFragment::class.java)
                    }
                }
            }

            DslBaseLabelItem()() {
                itemBackgroundDrawable = null
                itemLabelText = span {
                    append("---------以下高能, 内嵌联动滚动Behavior---------")
                    appendln()
                    append("背景+头部+悬停+底部+标题栏,布局结构de联动行为")
                    appendln()
                    append("头部和底部,均支持`NestedScrollingChild`控件") {
                        foregroundColor = Color.RED
                    }
                    appendln()
                    append("和普通`View`控件") {
                        foregroundColor = Color.RED
                    }
                }
                configLabelTextStyle {
                    textGravity = Gravity.CENTER
                }
                itemBindOverride = { itemHolder, _, _, _ ->
                    itemHolder.view(R.id.lib_label_view)?.setWidth(-1)
                }
            }

            AppTextItem()() {
                itemText = "联动->标题渐变演示"
                itemClick = {
                    dslFHelper {
                        show(LinkageFragment::class.java)
                    }
                }
            }

            AppTextItem()() {
                itemText = "联动->刷新演示"
                itemClick = {
                    dslFHelper {
                        show(LinkageRefreshFragment::class.java)
                    }
                }
            }

            AppTextItem()() {
                itemText = "联动->背景放大演示"
                itemClick = {
                    dslFHelper {
                        show(LinkageBackgroundScaleFragment::class.java)
                    }
                }
            }

            AppTextItem()() {
                itemText = "联动->背景放大(带事件)演示"
                itemClick = {
                    dslFHelper {
                        show(LinkageBackgroundScaleTouchFragment::class.java)
                    }
                }
            }

            AppTextItem()() {
                itemText = "联动->模仿`酷安`应用详情页效果"
                itemClick = {
                    dslFHelper {
                        show(LinkageStickyHoldFragment::class.java)
                    }
                }
            }

            DslBottomItem()() {
                itemText = "演示的数据随机生成, 如果数据量不足以触发滚动时, 请重新打开试试."
                itemPaddingTop = 60 * dpi
                itemPaddingBottom = 60 * dpi

                configTextStyle {
                    textGravity = Gravity.CENTER
                }
            }

            batchLoad(60)
        }
    }
}