package wzl.android.ducksplash.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import wzl.android.ducksplash.api.SearchService
import wzl.android.ducksplash.data.SearchCollectionPagingSource
import wzl.android.ducksplash.data.SearchPhotoPagingSource
import wzl.android.ducksplash.data.SearchUserPagingSource
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.model.UserModel
import javax.inject.Inject

/**
 *Created on 2021/1/18
 *@author zhilin
 */
class SearchRepository @Inject constructor(private val service: SearchService) {

    fun searchPhotos(
            query: String,
            orderBy: String? = null,
            collections: String? = null,
            contentFilter: String? = null,
            color: String? = null,
            orientation: String? = null
    ): LiveData<PagingData<PhotoModel>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { SearchPhotoPagingSource(query, service) }
        ).flow.asLiveData()
    }

    fun searchCollections(query: String): LiveData<PagingData<CollectionModel>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { SearchCollectionPagingSource(query, service) }
        ).flow.asLiveData()
    }

    fun searchUsers(query: String): LiveData<PagingData<UserModel>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { SearchUserPagingSource(query, service) }
        ).flow.asLiveData()
    }

}