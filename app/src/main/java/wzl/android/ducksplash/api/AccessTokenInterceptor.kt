package wzl.android.ducksplash.api

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import wzl.android.ducksplash.api.login.TokenProtoProvider
import javax.inject.Inject

/**
 *Created on 2021/1/23
 *@author zhilin
 */
class AccessTokenInterceptor @Inject constructor(
    private val tokenProvider: TokenProtoProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain) = runBlocking {
        val preferences = tokenProvider.loginPreferences.firstOrNull()
        if (!preferences?.accessToken.isNullOrEmpty()) {
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${preferences?.accessToken}")
                    .build()
            )
        } else {
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", "Client-ID ${tokenProvider.clientId}")
                    .build()
            )
        }
    }

}