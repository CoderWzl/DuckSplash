package wzl.android.ducksplash.api

import retrofit2.http.GET
import retrofit2.http.Query
import wzl.android.ducksplash.model.CollectionModel

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

}