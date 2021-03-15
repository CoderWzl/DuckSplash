package wzl.android.ducksplash.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 *Created on 2021/3/15
 *@author zhilin
 */
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.toast(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), duration)
}