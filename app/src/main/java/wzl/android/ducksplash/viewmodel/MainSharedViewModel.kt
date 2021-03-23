package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import wzl.android.ducksplash.adapter.AddState
import wzl.android.ducksplash.api.login.TokenProtoProvider
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.CollectionPhotoResult
import wzl.android.ducksplash.repository.CollectionRepository
import wzl.android.ducksplash.repository.UserRepository
import java.lang.Exception

/**
 *Created on 2021/3/19
 *@author zhilin
 * 共享 ViewModel 生命周期不依赖 Fragment 生命周期，其依赖 Fragment 所依附的 Activity 的生命周期
 * 实现 Fragment 之间共享数据。
 */
class MainSharedViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private var _userCollections: Flow<PagingData<CollectionModel>>? = null
    private var _userName: String? = null

    fun getUserCollections(name: String?): Flow<PagingData<CollectionModel>> {
        if (_userName.isNullOrEmpty() || !_userName.equals(name)) {
            _userCollections = null
        }
        if (_userCollections == null && !name.isNullOrEmpty()) {
            _userCollections = userRepository.listUserCollections(name)
                .cachedIn(viewModelScope)
            _userName = name
        }
        return _userCollections!!
    }

    fun addPhotoToCollection(collectionId: Int, photoId: String) =
        liveData(viewModelScope.coroutineContext) {
            emit(AddState.Adding(collectionId))
            try {
                val result = collectionRepository.addPhotoToCollection(
                    collectionId,
                    photoId
                )
                emit(AddState.Added(result))
            } catch (e: Exception) {
                emit(AddState.NotAdd(collectionId))
            }
        }

}