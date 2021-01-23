package wzl.android.ducksplash.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import wzl.android.ducksplash.api.CollectionService
import wzl.android.ducksplash.data.CollectionPagingSource
import wzl.android.ducksplash.model.CollectionModel
import javax.inject.Inject

/**
 *Created on 2021/1/22
 *@author zhilin
 *加载图集相关
 */
class CollectionRepository @Inject constructor(
    private val service: CollectionService
) {

    fun getCollections() : Flow<PagingData<CollectionModel>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { CollectionPagingSource(service) }
        ).flow
    }
}