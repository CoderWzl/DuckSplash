package wzl.android.ducksplash.data

import androidx.paging.PagingSource
import wzl.android.ducksplash.INITIAL_PAGE
import wzl.android.ducksplash.api.UserService
import wzl.android.ducksplash.model.PhotoModel

/**
 *Created on 2/2/21
 *@author zhilin
 */
class UserLikePhotoPagingSource(
    private val username: String,
    private val service: UserService
): PagingSource<Int, PhotoModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoModel> {
        val page = params.key?: INITIAL_PAGE
        return try {
            val result = service.listUserLikePhotos(
                username,
                page,
                params.loadSize,
                null,
                null
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