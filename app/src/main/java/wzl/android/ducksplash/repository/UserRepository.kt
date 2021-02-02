package wzl.android.ducksplash.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import wzl.android.ducksplash.api.UserService
import wzl.android.ducksplash.data.UserCollectionPagingSource
import wzl.android.ducksplash.data.UserLikePhotoPagingSource
import wzl.android.ducksplash.data.UserPhotoPagingSource
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.PhotoModel
import javax.inject.Inject

/**
 *Created on 2/2/21
 *@author zhilin
 */
class UserRepository @Inject constructor(
    val service: UserService
) {

    fun listUserPhotos(username: String): Flow<PagingData<PhotoModel>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UserPhotoPagingSource(username, service) }
        ).flow
    }

    fun listUserLikePhotos(username: String): Flow<PagingData<PhotoModel>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UserLikePhotoPagingSource(username, service) }
        ).flow
    }

    fun listUserCollections(username: String): Flow<PagingData<CollectionModel>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UserCollectionPagingSource(username, service) }
        ).flow
    }

}