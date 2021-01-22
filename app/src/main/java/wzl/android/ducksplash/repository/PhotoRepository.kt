package wzl.android.ducksplash.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import wzl.android.ducksplash.api.PhotoService
import wzl.android.ducksplash.api.getNetworkService
import wzl.android.ducksplash.data.CollectionPhotoPagingSource
import wzl.android.ducksplash.data.PhotoPagingSource
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.PhotoModel

/**
 *Created on 2021/1/11
 *@author zhilin
 * Repository 用来执行获取数据的逻辑，数据源可以是 数据库 也可以是 网络
 */
class PhotoRepository(private val service: PhotoService) {

    fun getPhotos(): Flow<PagingData<PhotoModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PhotoPagingSource(service) }
        ).flow
    }

    fun getCollectionPhotos(id: Int): Flow<PagingData<PhotoModel>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { CollectionPhotoPagingSource(id, service) }
        ).flow
    }

}