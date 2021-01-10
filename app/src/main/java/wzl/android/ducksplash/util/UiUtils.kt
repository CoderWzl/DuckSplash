package wzl.android.ducksplash.util

import android.content.Context
import android.view.ViewGroup

/**
 *Created on 1/10/21
 *@author zhilin
 */

/**
 * 获取状态栏的高度
 */
fun Context.statusBarHeight(): Int {
    val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resId > 0) {
        resources.getDimensionPixelSize(resId)
    } else {
        0
    }
}

/**
 * ViewGroup 设置 padding 预留出状态栏的高度
 */
fun ViewGroup.reserveStatusBar() {
    if (tag is Boolean && tag as Boolean) {
        return
    }
    setPadding(
        paddingLeft,
        paddingTop + context.statusBarHeight(),
        paddingRight,
        paddingBottom
    )
    tag = true
}