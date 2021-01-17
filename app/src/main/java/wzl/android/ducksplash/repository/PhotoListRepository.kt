package wzl.android.ducksplash.repository

import androidx.lifecycle.MutableLiveData
import wzl.android.ducksplash.api.getNetworkService
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.PhotoModel

/**
 *Created on 2021/1/11
 *@author zhilin
 * Repository 用来执行获取数据的逻辑，数据源可以是 数据库 也可以是 网络
 */
class PhotoListRepository {

    val photoList = MutableLiveData<List<PhotoModel>>()
    val collectionList = MutableLiveData<List<CollectionModel>>()

    suspend fun loadPhotoList(page: Int, perPage: Int = 30) {
        val photos = getNetworkService().getPhotoList(page, perPage)
        photoList.value = photos
    }

    suspend fun loadCollectionList(page: Int, perPage: Int = 30) {
        val collections = getNetworkService().getCollectionList(page, perPage)
        collectionList.value = collections
    }

    suspend fun loadPhotoListWithCollectionId(id: Int, page: Int, perPage: Int = 30) {
        val photos = getNetworkService().getPhotoListWithCollectionId(id, page, perPage)
        photoList.value = photos
    }
}