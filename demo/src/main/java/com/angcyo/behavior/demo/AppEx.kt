package com.angcyo.behavior.demo

import android.graphics.drawable.ColorDrawable
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.nowTime
import com.angcyo.item.DslTextInfoItem
import com.angcyo.item._color
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/29
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */


/**快速加载demo数据*/
fun DslAdapter.loadTextItem(
    count: Int = Random.nextInt(2, 40),
    action: DslTextInfoItem.() -> Unit = {}
) {
    for (i in 0 until count) {
        DslTextInfoItem()() {
            itemBackgroundDrawable = ColorDrawable(_color(R.color.bg_primary_color))
            itemInfoText = "Position:$i ${tx()}"
            itemDarkText = nowTimeString()
            action()
        }
    }
}

val textList = listOf(
    "矫情的分割线",
    "暗度陈仓",
    "金蝉脱壳",
    "抛砖引玉",
    "借刀杀人",
    "以逸待劳",
    "擒贼擒王",
    "趁火打劫",
    "关门捉贼",
    "浑水摸鱼",
    "打草惊蛇",
    "瞒天过海",
    "反间计",
    "笑里藏刀",
    "顺手牵羊",
    "调虎离山",
    "李代桃僵",
    "指桑骂槐",
    "隔岸观火",
    "树上开花",
    "暗渡陈仓",
    "走为上",
    "假痴不癫",
    "欲擒故纵",
    "釜底抽薪",
    "空城计",
    "苦肉计",
    "远交近攻",
    "反客为主",
    "上屋抽梯",
    "偷梁换柱",
    "无中生有",
    "美人计",
    "借尸还魂",
    "声东击西",
    "围魏救赵",
    "连环计",
    "假道伐虢"
)

fun tx(): String {
    return textList[Random.nextInt(textList.size)]
}

fun nowTimeString(pattern: String = "yyyy-MM-dd HH:mm:ss.SSS"): String {
    return nowTime().fullTime(pattern)
}

/**时间全格式输出*/
fun Long.fullTime(pattern: String = "yyyy-MM-dd HH:mm:ss.SSS"): String {
    return toTime(pattern)
}

/**格式化时间输出*/
fun Long.toTime(pattern: String = "yyyy-MM-dd HH:mm"): String {
    val format: SimpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
    format.applyPattern(pattern)
    return format.format(Date(this))
}
