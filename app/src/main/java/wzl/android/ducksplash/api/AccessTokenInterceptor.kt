package wzl.android.ducksplash.api

import okhttp3.Interceptor
import okhttp3.Response
import wzl.android.ducksplash.USPLASH_CLIENT_ID
import javax.inject.Inject

/**
 *Created on 2021/1/23
 *@author zhilin
 */
class AccessTokenInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", "Client-ID $USPLASH_CLIENT_ID")
                .build()
        )
    }

}