package wzl.android.ducksplash.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import wzl.android.ducksplash.BASE_LOGIN_URL
import wzl.android.ducksplash.BASE_URL
import wzl.android.ducksplash.api.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *Created on 2021/1/23
 *@author zhilin
 * Service Di
 */
@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(
        @AccessTokenInterceptorType accessTokenInterceptor: Interceptor,
        @HeaderInterceptorType headerInterceptor: Interceptor,
        @HttpLoggingInterceptorType httpLoggingInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(headerInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(accessTokenInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    fun providePhotoService(
        okHttpClient: OkHttpClient
    ): PhotoService = createApiService(okHttpClient)

    @Provides
    fun provideCollectionService(
        okHttpClient: OkHttpClient
    ): CollectionService = createApiService(okHttpClient)

    @Provides
    fun provideSearchService(
        okHttpClient: OkHttpClient
    ): SearchService = createApiService(okHttpClient)

    @Provides
    fun provideUserService(
        okHttpClient: OkHttpClient
    ): UserService = createApiService(okHttpClient)

    @Provides
    fun provideLoginService(
        okHttpClient: OkHttpClient
    ): LoginService {
        return Retrofit.Builder()
            .baseUrl(BASE_LOGIN_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(LoginService::class.java)
    }

    private inline fun <reified T> createApiService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory = MoshiConverterFactory.create()
    ) : T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
            .create(T::class.java)
    }

    @Provides
    fun provideDownloadService(
        @AccessTokenInterceptorType accessTokenInterceptor: Interceptor,
        @HeaderInterceptorType headerInterceptor: Interceptor,
        @HttpLoggingInterceptorType httpLoggingInterceptor: Interceptor
    ): DownloadService {
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(headerInterceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(accessTokenInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(DownloadService::class.java)
    }

}
