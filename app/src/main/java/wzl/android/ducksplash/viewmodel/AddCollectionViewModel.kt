package wzl.android.ducksplash.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
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
 *Created on 2021/3/26
 *@author zhilin
 */
class AddCollectionViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    // 用户图集
    private var _userCollections: Flow<PagingData<CollectionModel>>? = null
    private var _userName: String? = null

    // 获取用户图集信息。
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
                _userCollections?.map { it.map { if (it.id == collectionId) it.coverPhoto = result.photo } }?.collect()
                emit(AddState.Added(result))
            } catch (e: Exception) {
                Log.d("zhilin", "addPhotoToCollection: ${e.localizedMessage}")
                emit(AddState.NotAdd(collectionId))
            }
        }
}