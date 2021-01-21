package wzl.android.ducksplash.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import wzl.android.ducksplash.BASE_URL
import wzl.android.ducksplash.USPLASH_CLIENT_ID
import java.util.concurrent.TimeUnit

/**
 *Created on 2021/1/21
 *@author zhilin
 */
private const val CONTENT_TYPE = "Content-Type"
private const val APPLICATION_JSON = "application/json"
private const val ACCEPT_VERSION = "Accept-Version"

val httpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
            .addNetworkInterceptor(createHeaderInterceptor())
            .addInterceptor(createHttpLogInterceptor())
            .addInterceptor(createAccessTokenInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
}

private fun createHeaderInterceptor(): Interceptor {
    return Interceptor { chain ->
        val newRequest = chain.request()
                .newBuilder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(ACCEPT_VERSION, "v1")
                .build()
        chain.proceed(request = newRequest)
    }
}

private fun createHttpLogInterceptor(): Interceptor {
    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
        redactHeader("Authorization")
    }
}

private fun createAccessTokenInterceptor(): Interceptor {
    return Interceptor { chain ->
        chain.proceed(
                chain.request().newBuilder()
                        .addHeader("Authorization", "Client-ID $USPLASH_CLIENT_ID")
                        .build()
        )
    }
}

inline fun <reified T> createApiService(
        okHttpClient: OkHttpClient = httpClient,
        converterFactory: Converter.Factory = GsonConverterFactory.create()
) : T {
    return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
            .create(T::class.java)
}