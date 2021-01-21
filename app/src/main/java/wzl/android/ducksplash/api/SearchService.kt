package wzl.android.ducksplash.api

import retrofit2.http.GET
import retrofit2.http.Query
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.model.SearchModel
import wzl.android.ducksplash.model.UserModel

/**
 *Created on 2021/1/21
 *@author zhilin
 */
interface SearchService {

    @GET("search/photos")
    suspend fun searchPhotos(
            @Query("query") query: String,
            @Query("page") page: Int?,
            @Query("per_page") per_page: Int?,
            @Query("order_by") order_by: String?,
            @Query("collections") collections: String?,
            @Query("content_filter") contentFilter: String?,
            @Query("color") color: String?,
            @Query("orientation") orientation: String?
    ): SearchModel<PhotoModel>

    @GET("search/collections")
    suspend fun searchCollections(
            @Query("query") query: String,
            @Query("page") page: Int?,
            @Query("per_page") per_page: Int?
    ): SearchModel<CollectionModel>

    @GET("search/users")
    suspend fun searchUsers(
            @Query("query") query: String,
            @Query("page") page: Int?,
            @Query("per_page") per_page: Int?
    ): SearchModel<UserModel>

}