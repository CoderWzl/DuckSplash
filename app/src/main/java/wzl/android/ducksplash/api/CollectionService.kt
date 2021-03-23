package wzl.android.ducksplash.api

import retrofit2.http.*
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.CollectionPhotoResult

/**
 *Created on 2021/1/21
 *@author zhilin
 */
interface CollectionService {

    @GET("collections")
    suspend fun getCollections(
            @Query("page") page: Int?,
            @Query("per_page") perPage: Int?
    ): List<CollectionModel>

    @POST("collections/{collection_id}/add")
    suspend fun addPhotoToCollection(
        @Path("collection_id") collectionId: Int,
        @Field("photo_id") photoId: String
    ): CollectionPhotoResult

}