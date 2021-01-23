package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.repository.PhotoRepository

class PhotoListViewModel @ViewModelInject constructor(
    private val repository: PhotoRepository
) : ViewModel() {

    private var currentPhotos: Flow<PagingData<PhotoModel>>? = null

    fun getPhotos(): Flow<PagingData<PhotoModel>> {
        return currentPhotos?: repository.getPhotos()
            .cachedIn(viewModelScope)
            .also { currentPhotos = it }
    }
}