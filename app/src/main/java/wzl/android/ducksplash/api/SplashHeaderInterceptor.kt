package wzl.android.ducksplash.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import wzl.android.ducksplash.USPLASH_CLIENT_ID

/**
 *Created on 2021/1/11
 *@author zhilin
 * 请求发出前添加一些必须且通用的头部信息
 */
private const val TAG = "DuckSplashInterceptor"

class DuckSplashInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, "intercept: $chain")
        val request = chain.request().newBuilder()
            .addHeader("Accept-Version", "v1")
            .addHeader("Authorization", "Client-ID $USPLASH_CLIENT_ID")
            .build()
        return chain.proceed(request)
    }
}