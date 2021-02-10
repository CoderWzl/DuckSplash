package wzl.android.ducksplash.util

import android.content.Context

/**
 *Created on 2021/2/5
 *@author zhilin
 */
private const val DATA_STORE_DEFAULT_PREFIX = "_data_store"

fun getDefaultDataStoreName(context: Context) = context.packageName + DATA_STORE_DEFAULT_PREFIX