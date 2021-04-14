package wzl.android.ducksplash.util

import android.util.Log
import wzl.android.ducksplash.BuildConfig

/**
 *Created on 4/13/21
 *@author zhilin
 */
fun Any.debug(message: String) {
    if (BuildConfig.DEBUG){
        Log.d(this::class.java.simpleName, message)
    }
}

fun Any.debug(message: String, tr: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message, tr)
    }
}

fun Any.error(message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(this::class.java.simpleName, message)
    }
}

fun Any.error(message: String, tr: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.e(this::class.java.simpleName, message, tr)
    }
}

fun Any.info(message: String) {
    if (BuildConfig.DEBUG) {
        Log.i(this::class.java.simpleName, message)
    }
}

fun Any.info(message: String, tr: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.i(this::class.java.simpleName, message, tr)
    }
}

fun Any.verbose(message: String) {
    if (BuildConfig.DEBUG) {
        Log.v(this::class.java.simpleName, message)
    }
}

fun Any.verbose(message: String, tr: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.v(this::class.java.simpleName, message, tr)
    }
}

fun Any.warn(message: String) {
    if (BuildConfig.DEBUG) {
        Log.w(this::class.java.simpleName, message)
    }
}

fun Any.warn(message: String, tr: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.w(this::class.java.simpleName, message, tr)
    }
}

fun Any.wtf(message: String) {
    if (BuildConfig.DEBUG) {
        Log.wtf(this::class.java.simpleName, message)
    }
}

fun Any.wtf(message: String, tr: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.wtf(this::class.java.simpleName, message, tr)
    }
}

fun Any.wtf(tr: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.wtf(this::class.java.simpleName, tr)
    }
}

