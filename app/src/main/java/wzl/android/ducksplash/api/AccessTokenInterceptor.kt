package wzl.android.ducksplash.api

import okhttp3.Interceptor
import okhttp3.Response
import wzl.android.ducksplash.USPLASH_CLIENT_ID
import wzl.android.ducksplash.api.login.AccessTokenProvider
import javax.inject.Inject

/**
 *Created on 2021/1/23
 *@author zhilin
 */
private const val TAG = "AccessTokenInterceptor"

class AccessTokenInterceptor @Inject constructor(
    private val tokenProvider: AccessTokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", "Client-ID $USPLASH_CLIENT_ID")
                .build()
        )
    }
//
//    override fun intercept(chain: Interceptor.Chain) = runBlocking {
//        var isAuthorized = false
//        var token = ""
//        launch {
//            tokenProvider.isAuthorized.collectLatest {
//                isAuthorized = it
//            }
//            tokenProvider.accessToken.collectLatest {
//                token = it ?: ""
//            }
//        }
//        Log.d(TAG, "intercept: isAuthorized $isAuthorized, token $token")
//        if (isAuthorized) {
//            chain.proceed(
//                chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer $token")
//                    .build()
//            )
//        } else {
//            chain.proceed(
//                chain.request().newBuilder()
//                    .addHeader("Authorization", "Client-ID $USPLASH_CLIENT_ID")
//                    .build()
//            )
//        }
//    }

}