package wzl.android.ducksplash.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import wzl.android.ducksplash.BASE_URL
import wzl.android.ducksplash.api.CollectionService
import wzl.android.ducksplash.api.PhotoService
import wzl.android.ducksplash.api.SearchService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *Created on 2021/1/23
 *@author zhilin
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

    private inline fun <reified T> createApiService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory = GsonConverterFactory.create()
    ) : T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
            .create(T::class.java)
    }

}
