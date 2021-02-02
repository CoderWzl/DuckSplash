package wzl.android.ducksplash.data

import android.util.Log
import androidx.paging.PagingSource
import wzl.android.ducksplash.INITIAL_PAGE
import wzl.android.ducksplash.api.UserService
import wzl.android.ducksplash.model.PhotoModel

/**
 *Created on 2/1/21
 *@author zhilin
 */
class UserPhotoPagingSource(
    private val username: String,
    private val service: UserService
): PagingSource<Int, PhotoModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoModel> {
        val page = params.key?: INITIAL_PAGE
        Log.d("zhilin", "load: $page")
        return try {
            val result = service.listUserPhotos(
                username,
                page,
                params.loadSize,
                null,
                null,
                null,
                null,
                null,
            )
            val nextKey = if (result.isEmpty()) null else page + 1
            val prevKey = if (page == INITIAL_PAGE) null else page - 1
            Log.d("zhilin", "load: $result")
            LoadResult.Page(
                data = result,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (throwable: Throwable) {
            Log.d("zhilin", "load: $throwable")
            LoadResult.Error(throwable)
        }
    }

}