package wzl.android.ducksplash.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import wzl.android.ducksplash.api.AccessTokenInterceptor
import wzl.android.ducksplash.api.HeaderInterceptor
import javax.inject.Qualifier

/**
 *Created on 2021/1/23
 *@author zhilin
 */
@Qualifier
annotation class HeaderInterceptorType

@Qualifier
annotation class AccessTokenInterceptorType

@Qualifier
annotation class HttpLoggingInterceptorType

@InstallIn(ApplicationComponent::class)
@Module
abstract class InterceptorModule {

    @HeaderInterceptorType
    @Binds
    abstract fun bindHeaderInterceptor(headerInterceptor: HeaderInterceptor): Interceptor

    @AccessTokenInterceptorType
    @Binds
    abstract fun bindAccessTokenInterceptor(headerInterceptor: AccessTokenInterceptor): Interceptor

    @HttpLoggingInterceptorType
    @Binds
    abstract fun bindHttpLoggingInterceptor(headerInterceptor: HttpLoggingInterceptor): Interceptor

}

@InstallIn(ApplicationComponent::class)
@Module
class HttpLoggingInterceptorModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
        redactHeader("Authorization")
    }
}