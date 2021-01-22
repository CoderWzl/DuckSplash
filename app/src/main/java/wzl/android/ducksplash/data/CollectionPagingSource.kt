package wzl.android.ducksplash.data

import androidx.paging.PagingSource
import retrofit2.HttpException
import wzl.android.ducksplash.INITIAL_PAGE
import wzl.android.ducksplash.api.CollectionService
import wzl.android.ducksplash.model.CollectionModel
import java.io.IOException

/**
 *Created on 2021/1/22
 *@author zhilin
 * 分页数据加载
 */
class CollectionPagingSource(private val service: CollectionService) :
        PagingSource<Int, CollectionModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionModel> {
        val page = params.key?: INITIAL_PAGE
        return try {
            val pageSize = if (page == INITIAL_PAGE) params.loadSize / 3 else params.loadSize
            val response = service.getCollections(page, pageSize)
            val prevKey = if (page == INITIAL_PAGE) null else page - 1
            val nextKey = if (response.isEmpty()) null else page + 1
            LoadResult.Page(
                    data = response,
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