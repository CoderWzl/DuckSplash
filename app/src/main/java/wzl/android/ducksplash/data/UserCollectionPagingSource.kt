package wzl.android.ducksplash.data

import androidx.paging.PagingSource
import wzl.android.ducksplash.INITIAL_PAGE
import wzl.android.ducksplash.api.UserService
import wzl.android.ducksplash.model.CollectionModel

/**
 *Created on 2/2/21
 *@author zhilin
 */
class UserCollectionPagingSource(
    private val username: String,
    private val service: UserService
): PagingSource<Int, CollectionModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionModel> {
        val page = params.key?: INITIAL_PAGE
        return try {
            val result = service.listUserCollections(
                username,
                page,
                params.loadSize
            )
            val nextKey = if (result.isEmpty()) null else page + 1
            val prevKey = if (page == INITIAL_PAGE) null else page - 1
            LoadResult.Page(
                data = result,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }
}