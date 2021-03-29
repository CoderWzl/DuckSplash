package wzl.android.ducksplash.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import wzl.android.ducksplash.adapter.AddState
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.repository.CollectionRepository
import wzl.android.ducksplash.repository.UserRepository
import java.lang.Exception

/**
 *Created on 2021/3/19
 *@author zhilin
 * 共享 ViewModel 生命周期不依赖 Fragment 生命周期，其依赖 Fragment 所依附的 Activity 的生命周期
 * 实现 Fragment 之间共享数据。
 * 在 PhotoDetailFragment 中添加图片到图集的时候，用来共享用户所有的图集信息，后面没有应用这个方案
 * 选择直接将 PhotoDetailFragment 的 ViewModel 传递到AddCollectionBottomSheet 的方法
 */
class MainSharedViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private var _userCollections: Flow<PagingData<CollectionModel>>? = null
    private var _userName: String? = null

    fun getUserCollections(name: String?): Flow<PagingData<CollectionModel>>? {
        if (_userName.isNullOrEmpty() || !_userName.equals(name)) {
            _userCollections = null
        }
        if (_userCollections == null && !name.isNullOrEmpty()) {
            _userCollections = userRepository.listUserCollections(name)
                .cachedIn(viewModelScope)
            _userName = name
        }
        return _userCollections
    }

    fun addPhotoToCollection(collectionId: Int, photoId: String) =
        liveData(viewModelScope.coroutineContext) {
            emit(AddState.Adding(collectionId))
            try {
                val result = collectionRepository.addPhotoToCollection(
                    collectionId,
                    photoId
                )
                // NOTE：使用 Paging3 进行数据加载的时候，遇到需要变更列表某一项信息的时候（例如在现在的业务
                // 场景下，用户将图片成功收藏到图集后需要更新图集的封面为最新收藏的图片，不知道该如何操作），
                // 不知道该如何操作网上搜索得知可结合 Room 通过修改数据源的方式更新数据项。没有使用 Room 的
                // 话还是没有找到很好的解决办法。
                emit(AddState.Added(result))
            } catch (e: Exception) {
                Log.d("zhilin", "addPhotoToCollection: ${e.localizedMessage}")
                emit(AddState.NotAdd(collectionId))
            }
        }

}