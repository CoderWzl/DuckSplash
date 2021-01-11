package wzl.android.ducksplash.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Query
import wzl.android.ducksplash.BASE_URL
import wzl.android.ducksplash.model.PhotoModel

/**
 *Created on 2021/1/11
 *@author zhilin
 * 通过 retrofit 进行网络请求
 */
private val service: DuckSplashService by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(DuckSplashInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //.baseUrl("http://192.168.100.215:8080/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(DuckSplashService::class.java)
}

fun getNetworkService() = service

interface DuckSplashService {
    // page per_page 指定页数和一页中图片个数
    //@GET("resource/getPasterInfo/")
    @GET("photos/")
    suspend fun getPhotoList(@Query("page") page: Int, @Query("per_page") perPage: Int): List<PhotoModel>
}