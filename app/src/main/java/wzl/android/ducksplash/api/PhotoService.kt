package wzl.android.ducksplash.api

import retrofit2.http.*
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
            @Path("id") id: String,
            @Query("page") page: Int?,
            @Query("per_page") perPage: Int?
    ): List<PhotoModel>

    @GET("photos/{id}")
    suspend fun getPhoto(
        @Path("id") id: String
    ): PhotoModel

    @GET("photos/random")
    suspend fun getRandomPhoto(
        @Query("collections") collections: String? = null, //Public collection ID(‘s) to filter selection. If multiple, comma-separated
        @Query("username") username: String? = null, //Limit selection to a single user.
        @Query("query") query: String? = null, //Limit selection to photos matching a search term.
        @Query("orientation") orientation: String? = null, //Filter by photo orientation. (Valid values: landscape, portrait, squarish)
        @Query("content_filter") contentFilter: String? = null, // Limit results by content safety. Default: low. Valid values are low and high.
    ): PhotoModel

    @GET("photos/random")
    suspend fun getRandomPhotos(
        @Query("collections") collections: String? = null, //Public collection ID(‘s) to filter selection. If multiple, comma-separated
        @Query("username") username: String? = null, //Limit selection to a single user.
        @Query("query") query: String? = null, //Limit selection to photos matching a search term.
        @Query("orientation") orientation: String? = null, //Filter by photo orientation. (Valid values: landscape, portrait, squarish)
        @Query("content_filter") contentFilter: String? = null, // Limit results by content safety. Default: low. Valid values are low and high.
        @Query("count") count: Int = 1 // The number of photos to return. (Default: 1; max: 30)
    ): List<PhotoModel>

    @POST("photos/{id}/like")
    suspend fun likePhoto(
        @Path("id") id: String
    ): PhotoModel

    @DELETE("photos/{id}/like")
    suspend fun unlikePhoto(
        @Path("id") id: String
    ): PhotoModel

}