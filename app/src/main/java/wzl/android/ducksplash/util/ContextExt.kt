package wzl.android.ducksplash.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

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

fun Context.hasWritePermission(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
            hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

fun Context.hasPermission(vararg permissions: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        permissions.all { singlePermission ->
            ContextCompat.checkSelfPermission(this, singlePermission) == PackageManager.PERMISSION_GRANTED
        }
    else true
}

fun Activity.requestPermission(vararg permissions: String, @IntRange(from = 0) requestCode: Int) =
    ActivityCompat.requestPermissions(this, permissions, requestCode)

fun Fragment.requestPermission(vararg permissions: String, @IntRange(from = 0) requestCode: Int) =
    requestPermissions(permissions, requestCode)