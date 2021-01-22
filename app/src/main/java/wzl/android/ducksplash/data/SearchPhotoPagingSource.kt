package wzl.android.ducksplash.data

import androidx.paging.PagingSource
import retrofit2.HttpException
import wzl.android.ducksplash.INITIAL_PAGE
import wzl.android.ducksplash.api.SearchService
import wzl.android.ducksplash.model.PhotoModel
import java.io.IOException

/**
 *Created on 2021/1/22
 *@author zhilin
 */
class SearchPhotoPagingSource(
        private val query: String,
        private val service: SearchService
): PagingSource<Int, PhotoModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoModel> {
        val page = params.key?: INITIAL_PAGE
        // TODO: 2021/1/22 add other condition
        return try {
            val pageSize = if (page == INITIAL_PAGE) params.loadSize / 3 else params.loadSize
            val response = service.searchPhotos(
                    query,
                    page,
                    pageSize,
                    null,
                    null,
                    null,
                    null,
                    null
            )
            val prevKey = if (page == INITIAL_PAGE) null else page - 1
            val nextKey = if (response.results.isEmpty()) null else page + 1
            LoadResult.Page(
                    data = response.results,
                    prevKey = prevKey,
                    nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}