package wzl.android.ducksplash.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

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
fun View.reserveStatusBar() {
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

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.dp2px(dp: Float): Float {
    return dp * resources.displayMetrics.density + 0.5f
}

fun Context.px2dp(px: Float): Float {
    return px / resources.displayMetrics.density + 0.5f
}

fun Context.sp2px(sp: Float): Float {
    return sp * resources.displayMetrics.scaledDensity + 0.5f
}

fun Context.px2sp(px: Float): Float {
    return px / resources.displayMetrics.scaledDensity + 0.5f
}