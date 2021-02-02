package wzl.android.ducksplash.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.PhotoModel

/**
 *Created on 2/1/21
 *@author zhilin
 */
interface UserService {

    @GET("users/{username}/photos")
    suspend fun listUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("order_by") orderBy: String?,
        @Query("stats") stats: Boolean?,
        @Query("resolution") resolution: String?,
        @Query("quantity") quantity: Int?,
        @Query("orientation") orientation: String?
    ): List<PhotoModel>

    @GET("users/{username}/likes")
    suspend fun listUserLikePhotos(
        @Path("username") username: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("order_by") orderBy: String?,
        @Query("orientation") orientation: String?
    ): List<PhotoModel>

    @GET("users/{username}/collections")
    suspend fun listUserCollections(
        @Path("username") username: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
    ): List<CollectionModel>

}