package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.repository.UserRepository

/**
 *Created on 2/2/21
 *@author zhilin
 */
class UserPhotoViewModel @ViewModelInject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private var photos: Flow<PagingData<PhotoModel>>? = null

    fun listUserPhotos(username: String): Flow<PagingData<PhotoModel>> {
        if (photos != null) {
            return photos!!
        }
        return repository.listUserPhotos(username)
            .cachedIn(viewModelScope)
            .also { photos = it }
    }
}