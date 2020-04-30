package com.angcyo.behavior.linkage

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.math.MathUtils.clamp
import com.angcyo.behavior.*

/**
 * 渐变标题栏颜色的行为. 配合[LinkageHeaderBehavior]使用
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/03/24
 */
open class LinkageGradientTitleBehavior(
    context: Context,
    attributeSet: AttributeSet? = null
) : BaseLinkageGradientBehavior(context, attributeSet), ITitleBarBehavior {

    /**标题控件的id, 用于单独操作控制标题控件*/
    var titleTextId: Int = -1

    /**标题文本渐变开始的颜色*/
    var titleTextColorFrom: Int = Color.TRANSPARENT

    /**标题文本渐变结束的颜色*/
    var titleTextColorTo: Int = Color.BLUE

    /**[titleTextId]*/
    var backViewId: Int = -1

    var backIconColorFrom: Int = Color.WHITE
    var backIconColorTo: Int = Color.BLUE

    /**其他[ImageView]控件图标颜色*/
    var iconColorFrom: Int = Color.WHITE
        set(value) {
            field = value
            backIconColorFrom = value
        }
    var iconColorTo: Int = Color.BLUE
        set(value) {
            field = value
            backIconColorTo = value
        }

    /**整个标题栏背景颜色渐变, 如果背景不是ColorDrawable,则使用alpha*/
    var backgroundColorFrom: Int = Color.TRANSPARENT
    var backgroundColorTo: Int = Color.WHITE

    init {
        val array =
            context.obtainStyledAttributes(
                attributeSet,
                R.styleable.LinkageGradientTitleBehavior_Layout
            )
        titleTextId = array.getResourceId(
            R.styleable.LinkageGradientTitleBehavior_Layout_layout_title_text_id,
            titleTextId
        )
        backViewId = array.getResourceId(
            R.styleable.LinkageGradientTitleBehavior_Layout_layout_title_back_id,
            backViewId
        )
        titleTextColorFrom = array.getColor(
            R.styleable.LinkageGradientTitleBehavior_Layout_layout_title_text_color_from,
            titleTextColorFrom
        )
        titleTextColorTo = array.getColor(
            R.styleable.LinkageGradientTitleBehavior_Layout_layout_title_text_color_to,
            titleTextColorTo
        )

        backgroundColorFrom = array.getColor(
            R.styleable.LinkageGradientTitleBehavior_Layout_layout_background_color_from,
            backgroundColorFrom
        )
        backgroundColorTo = array.getColor(
            R.styleable.LinkageGradientTitleBehavior_Layout_layout_background_color_to,
            backgroundColorTo
        )

        iconColorFrom = array.getColor(
            R.styleable.LinkageGradientTitleBehavior_Layout_layout_icon_color_from,
            iconColorFrom
        )
        iconColorTo = array.getColor(
            R.styleable.LinkageGradientTitleBehavior_Layout_layout_icon_color_to,
            iconColorTo
        )

        backIconColorFrom = array.getColor(
            R.styleable.LinkageGradientTitleBehavior_Layout_layout_back_icon_color_from,
            backIconColorFrom
        )
        backIconColorTo = array.getColor(
            R.styleable.LinkageGradientTitleBehavior_Layout_layout_back_icon_color_to,
            backIconColorTo
        )
        array.recycle()
    }

    override fun getContentExcludeHeight(behavior: BaseDependsBehavior<*>): Int {
        return childView.mH()
    }

    override fun getContentOffsetTop(behavior: BaseDependsBehavior<*>): Int {
        return childView.mH()
    }


    /**开始渐变*/
    override fun onGradient(percent: Float) {
        val fraction = if (percent > 0) 0f else clamp(-percent, 0f, 1f)

        //背景
        childView?.apply {
            if (background == null || background is ColorDrawable) {
                setBackgroundColor(
                    evaluateColor(
                        fraction,
                        backgroundColorFrom,
                        backgroundColorTo
                    )
                )
            } else {
                background?.apply {
                    alpha = (255 * fraction).toInt()
                }
            }
        }

        if (childView is ViewGroup) {
            (childView as ViewGroup).each(true) {
                when (it) {
                    //文本
                    is TextView -> if (it.id == titleTextId) {
                        it.setTextColor(
                            evaluateColor(
                                fraction,
                                titleTextColorFrom,
                                titleTextColorTo
                            )
                        )
                    }
                    //图片icon
                    is ImageView -> if (it.id == backViewId) {
                        it.setImageDrawable(
                            it.drawable?.colorFilter(
                                evaluateColor(
                                    fraction,
                                    backIconColorFrom,
                                    backIconColorTo
                                )
                            )
                        )
                    } else {
                        it.setImageDrawable(
                            it.drawable?.colorFilter(
                                evaluateColor(
                                    fraction,
                                    iconColorFrom,
                                    iconColorTo
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}