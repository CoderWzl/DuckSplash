package wzl.android.ducksplash.repository

import androidx.lifecycle.MutableLiveData
import wzl.android.ducksplash.api.getNetworkService
import wzl.android.ducksplash.model.PhotoModel

/**
 *Created on 2021/1/11
 *@author zhilin
 * Repository 用来执行获取数据的逻辑，数据源可以是 数据库 也可以是 网络
 */
class PhotoListRepository {

    val photoList = MutableLiveData<List<PhotoModel>>()

    suspend fun loadPhotoList() {
        val photos = getNetworkService().getPhotoList()

    }
}