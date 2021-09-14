package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.repository.PhotoRepository

class CollectionDetailViewModel @ViewModelInject constructor(
    private val repository: PhotoRepository
) : ViewModel() {

    private var currentPhoto: Flow<PagingData<PhotoModel>>? = null
    private var currentId: String = ""

    fun getCollectionPhoto(id: String): Flow<PagingData<PhotoModel>> {
        if (id == currentId && currentPhoto != null) {
            return currentPhoto!!
        }
        currentId = id
        val newPhoto = repository.getCollectionPhotos(id)
                .cachedIn(viewModelScope)
        currentPhoto = newPhoto
        return newPhoto
    }

}