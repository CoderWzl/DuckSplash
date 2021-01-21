package wzl.android.ducksplash.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import wzl.android.ducksplash.model.PhotoModel

/**
 *Created on 2021/1/21
 *@author zhilin
 * Retrofit 用来获取 Photo 数据的接口
 */
interface PhotoService {

    @GET("photos")
    suspend fun getPhotos(
            @Query("page") page: Int?,
            @Query("per_page") perPage: Int?,
            @Query("order_by") orderBy: String?
    ): List<PhotoModel>

    @GET("collections/{id}/photos")
    suspend fun getCollectionPhotos(
            @Path("id") id: Int,
            @Query("page") page: Int?,
            @Query("per_page") perPage: Int?
    ): List<PhotoModel>
}