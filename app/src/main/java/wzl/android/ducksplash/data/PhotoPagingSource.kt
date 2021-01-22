package wzl.android.ducksplash.data

import android.util.Log
import androidx.paging.PagingSource
import retrofit2.HttpException
import wzl.android.ducksplash.INITIAL_PAGE
import wzl.android.ducksplash.api.PhotoService
import wzl.android.ducksplash.model.PhotoModel
import java.io.IOException

/**
 *Created on 2021/1/21
 *@author zhilin
 */
private const val TAG = "PhotoPagingSource"
class PhotoPagingSource(private val service: PhotoService):
    PagingSource<Int, PhotoModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoModel> {
        val key = params.key?: INITIAL_PAGE
        return try {
            Log.d(TAG, "load: ${params.loadSize}")
            val loadSize = if (key == INITIAL_PAGE) params.loadSize / 3 else params.loadSize
            val response = service.getPhotos(key, loadSize, null)
            val prevKey = if (key == INITIAL_PAGE) null else key - 1
            val nextKey = if (response.isEmpty()) null else key + 1
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