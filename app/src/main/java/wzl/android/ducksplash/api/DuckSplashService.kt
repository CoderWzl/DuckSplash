package wzl.android.ducksplash.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import wzl.android.ducksplash.BASE_URL
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.model.SearchModel

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
    suspend fun getPhotoList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<PhotoModel>

    @GET("collections/")
    suspend fun getCollectionList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<CollectionModel>

    @GET("collections/{id}/photos")
    suspend fun getPhotoListWithCollectionId(
        @Path("id") int: Int,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<PhotoModel>

    // search
    @GET("search/photos")
    suspend fun searchPhotoList(
        @Query("query") query: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("order_by") orderBy: String?, //relevant, latest
        @Query("content_filter") contentFilter: String?, //low , high
        @Query("color") color: String?, //black_and_white, black, white, yellow, orange, red, purple, magenta, green, teal, and blue
        @Query("orientation") orientation: String? //landscape, portrait, squarish
    ): SearchModel<PhotoModel>

    @GET("search/collections")
    suspend fun searchCollectionList(
        @Query("query") query: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?
    ): SearchModel<CollectionModel>

}