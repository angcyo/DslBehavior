package com.angcyo.behavior

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.OverScroller
import android.widget.ScrollView
import androidx.annotation.ColorInt
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.math.MathUtils
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.core.widget.ScrollerCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/04/29
 * Copyright (c) 2020 ShenZhen Wayto Ltd. All rights reserved.
 */

fun MotionEvent.isTouchDown(): Boolean {
    return actionMasked == MotionEvent.ACTION_DOWN
}

fun MotionEvent.isTouchFinish(): Boolean {
    return actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_CANCEL
}

fun ViewGroup.eachChildVisibility(map: (index: Int, child: View) -> Unit) {
    for (index in 0 until childCount) {
        val childAt = getChildAt(index)
        if (childAt.visibility != View.GONE) {
            map.invoke(index, childAt)
        }
    }
}

fun ViewGroup.LayoutParams.coordinatorParams(config: CoordinatorLayout.LayoutParams.() -> Unit = {}): CoordinatorLayout.LayoutParams? {
    return (this as? CoordinatorLayout.LayoutParams)?.run {
        config()
        this
    }
}

fun ViewGroup.each(recursively: Boolean = false, map: (child: View) -> Unit) {
    eachChild(recursively) { _, child ->
        map.invoke(child)
    }
}

/**枚举所有child view
 * [recursively] 递归所有子view*/
fun ViewGroup.eachChild(recursively: Boolean = false, map: (index: Int, child: View) -> Unit) {
    for (index in 0 until childCount) {
        val childAt = getChildAt(index)
        map.invoke(index, childAt)
        if (recursively && childAt is ViewGroup) {
            childAt.eachChild(recursively, map)
        }
    }
}

fun View?.mH(def: Int = 0): Int {
    return this?.measuredHeight ?: def
}

fun View?.mW(def: Int = 0): Int {
    return this?.measuredWidth ?: def
}

fun View.offsetTop(offset: Int) {
    ViewCompat.offsetTopAndBottom(this, offset)
}

/**限制滚动偏移的范围, 返回值表示 需要消耗的 距离*/
fun View.offsetTop(offset: Int, minTop: Int, maxTop: Int): Int {
    val offsetTop = top + offset
    val newTop = MathUtils.clamp(offsetTop, minTop, maxTop)

    offsetTopTo(newTop)

    return -(offset - (offsetTop - newTop))
}

fun View.offsetTopTo(newTop: Int) {
    offsetTop(newTop - top)
}

fun View.offsetTopTo(newTop: Int, minTop: Int, maxTop: Int) {
    offsetTop(newTop - top, minTop, maxTop)
}

fun View.offsetLeft(offset: Int) {
    ViewCompat.offsetLeftAndRight(this, offset)
}

/**限制滚动偏移的范围, 返回值表示 需要消耗的 距离*/
fun View.offsetLeft(offset: Int, minLeft: Int, maxLeft: Int): Int {
    val offsetLeft = left + offset
    val newLeft = MathUtils.clamp(offsetLeft, minLeft, maxLeft)

    offsetTopTo(newLeft)

    return -(offset - (offsetLeft - newLeft))
}

fun View.offsetLeftTo(newLeft: Int) {
    offsetLeft(newLeft - left)
}

fun Any?.stopScroll() {
    if (this is NestedScrollingChild2) {
        this.stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
    } else if (this is NestedScrollingChild) {
        this.stopNestedScroll()
    }

    if (this is RecyclerView) {
        this.stopScroll()
    } else if (this is NestedScrollView) {
        val mScroller = this.getMember(NestedScrollView::class.java, "mScroller")
        if (mScroller is OverScroller) {
            mScroller.abortAnimation()
        } else if (mScroller is ScrollerCompat) {
            mScroller.abortAnimation()
        }
    }
}

/** View 顶部是否还有可滚动的距离 */
fun Any?.topCanScroll(): Boolean {
    return canChildScroll(-1)
}

/** View 底部是否还有可滚动的距离 */
fun Any?.bottomCanScroll(): Boolean {
    return canChildScroll(1)
}

fun Any?.canChildScroll(direction: Int, depth: Int = 5): Boolean {
    if (this == null || depth < 0) {
        return false
    }
    if (this is RecyclerView || this is ListView) {
        //no op
    } else if (this is ViewGroup) {
        val group = this
        var child: View?
        var result: Boolean
        for (i in 0 until group.childCount) {
            child = group.getChildAt(i)
            result = when (child) {
                is RecyclerView -> child.canScrollVertically(direction)
                is ListView -> child.canScrollVertically(direction)
                is ViewGroup -> child.canChildScroll(direction, depth - 1)
                else -> (child?.canScrollVertically(direction) ?: child.canChildScroll(direction))
            }
            if (result) {
                return true
            }
        }
    }
    return this is View && this.canScrollVertically(direction)
}

/**
 * 从一个对象中, 获取指定的成员对象
 */
fun Any?.getMember(member: String): Any? {
    return this?.run { this.getMember(this.javaClass, member) }
}

fun Any?.getMember(
    cls: Class<*>,
    member: String
): Any? {
    var result: Any? = null
    try {
        val memberField = cls.getDeclaredField(member)
        memberField.isAccessible = true
        result = memberField[this]
    } catch (e: Exception) {
        //L.i("错误:" + cls.getSimpleName() + " ->" + e.getMessage());
    }
    return result
}

fun View?.behavior(): CoordinatorLayout.Behavior<*>? {
    return (this?.layoutParams as? CoordinatorLayout.LayoutParams?)?.run { this.behavior }
}

fun View?.setBehavior(behavior: CoordinatorLayout.Behavior<*>?) {
    (this?.layoutParams as? CoordinatorLayout.LayoutParams?)?.apply { this.behavior = behavior }
}

fun View.getStatusBarHeight(): Int {
    return context.getStatusBarHeight()
}

/**获取状态栏高度*/
fun Context.getStatusBarHeight(): Int {
    val resources = resources
    var result = 0
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Any?.hash(): String? {
    return this?.hashCode()?.run { Integer.toHexString(this) }
}

fun Any.simpleHash(): String {
    return "${this.javaClass.simpleName}(${this.hash()})"
}

fun MotionEvent.actionToString(): String {
    val action = this.actionMasked
    when (action) {
        MotionEvent.ACTION_DOWN -> return "ACTION_DOWN"
        MotionEvent.ACTION_UP -> return "ACTION_UP"
        MotionEvent.ACTION_CANCEL -> return "ACTION_CANCEL"
        MotionEvent.ACTION_OUTSIDE -> return "ACTION_OUTSIDE"
        MotionEvent.ACTION_MOVE -> return "ACTION_MOVE"
        MotionEvent.ACTION_HOVER_MOVE -> return "ACTION_HOVER_MOVE"
        MotionEvent.ACTION_SCROLL -> return "ACTION_SCROLL"
        MotionEvent.ACTION_HOVER_ENTER -> return "ACTION_HOVER_ENTER"
        MotionEvent.ACTION_HOVER_EXIT -> return "ACTION_HOVER_EXIT"
        11 -> return "ACTION_BUTTON_PRESS"
        12 -> return "ACTION_BUTTON_RELEASE"
    }
    val index =
        action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
    return when (action and MotionEvent.ACTION_MASK) {
        MotionEvent.ACTION_POINTER_DOWN -> "ACTION_POINTER_DOWN($index)"
        MotionEvent.ACTION_POINTER_UP -> "ACTION_POINTER_UP($index)"
        else -> action.toString()
    }
}


/**[NestedScrollingChild]*/
fun View.findNestedScrollingChild(
    predicate: (View) -> Boolean = {
        it is NestedScrollingChild &&
                it.measuredWidth > this.measuredWidth / 2 &&
                it.measuredHeight > this.measuredHeight / 2
    }
): NestedScrollingChild? {
    return findView(predicate) as? NestedScrollingChild
}

/**查找指定的[View]*/
fun View.findView(isIt: (View) -> Boolean): View? {
    return when {
        isIt(this) -> this
        this is ViewPager -> {
            var result: View? = null
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child.left >= scrollX &&
                    child.top >= scrollY &&
                    child.right <= scrollX + measuredWidth &&
                    child.bottom <= scrollY + measuredHeight
                ) {
                    result = child
                }
            }

            if (result != null) {
                result.findView(isIt)
            } else {
                if (isIt(this)) {
                    this
                } else {
                    null
                }
            }
        }
        this is ViewGroup -> {
            var result: View? = null
            for (i in 0 until childCount) {
                val childAt = getChildAt(i)
                result = childAt.findView(isIt)
                if (result != null) {
                    break
                }
            }
            result
        }
        else -> null
    }
}

fun View?.getLastVelocity(): Float {
    var currVelocity = 0f
    try {
        when (this) {
            is RecyclerView -> currVelocity = this.getLastVelocity()
            is NestedScrollView -> {
                val mScroller = this.getMember(NestedScrollView::class.java, "mScroller")
                currVelocity = mScroller.getCurrVelocity()
            }
            is ScrollView -> {
                val mScroller = this.getMember(ScrollView::class.java, "mScroller")
                currVelocity = mScroller.getCurrVelocity()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return currVelocity
}

/**
 * 获取[RecyclerView] [Fling] 时的速率
 * */
fun RecyclerView?.getLastVelocity(): Float {
    var currVelocity = 0f
    try {
        val mViewFlinger = this.getMember(RecyclerView::class.java, "mViewFlinger")
        var mScroller = mViewFlinger.getMember("mScroller")
        if (mScroller == null) {
            mScroller = mViewFlinger.getMember("mOverScroller")
        }
        currVelocity = mScroller.getCurrVelocity()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return currVelocity
}

fun Any?.getCurrVelocity(): Float {
    return when (this) {
        is OverScroller -> currVelocity
        is ScrollerCompat -> currVelocity
        else -> {
            0f
        }
    }
}

fun Any?.fling(velocityX: Int, velocityY: Int) {
    when (this) {
        is RecyclerView -> fling(velocityX, velocityY)
        is NestedScrollView -> fling(velocityY)
        is ScrollView -> fling(velocityY)
    }
}

fun evaluateColor(fraction: Float /*0-1*/, startColor: Int, endColor: Int): Int {
    val fraction = MathUtils.clamp(fraction, 0f, 1f)
    val startA = startColor shr 24 and 0xff
    val startR = startColor shr 16 and 0xff
    val startG = startColor shr 8 and 0xff
    val startB = startColor and 0xff
    val endA = endColor shr 24 and 0xff
    val endR = endColor shr 16 and 0xff
    val endG = endColor shr 8 and 0xff
    val endB = endColor and 0xff
    return startA + (fraction * (endA - startA)).toInt() shl 24 or
            (startR + (fraction * (endR - startR)).toInt() shl 16) or
            (startG + (fraction * (endG - startG)).toInt() shl 8) or
            startB + (fraction * (endB - startB)).toInt()
}

/**
 * 颜色过滤
 */
fun Drawable?.colorFilter(@ColorInt color: Int): Drawable? {
    if (this == null) {
        return null
    }
    val wrappedDrawable = DrawableCompat.wrap(this).mutate()
    wrappedDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    return wrappedDrawable
}

fun View?.invisible(value: Boolean = true) {
    this?.visibility = if (value) View.INVISIBLE else View.VISIBLE
}

fun View.setWidth(width: Int) {
    val params = layoutParams
    params.width = width
    layoutParams = params
}

fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
}