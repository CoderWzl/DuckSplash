package wzl.android.ducksplash.api

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 *Created on 2021/1/11
 *@author zhilin
 * 请求发出前添加一些必须且通用的头部信息
 */
private const val CONTENT_TYPE = "Content-Type"
private const val APPLICATION_JSON = "application/json"
private const val ACCEPT_VERSION = "Accept-Version"

class HeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
            .newBuilder()
            .addHeader(CONTENT_TYPE, APPLICATION_JSON)
            .addHeader(ACCEPT_VERSION, "v1")
            .build()
        return chain.proceed(request = newRequest)
    }
}