package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.repository.CollectionRepository

class CollectionListViewModel @ViewModelInject constructor(
    private val repository: CollectionRepository
) : ViewModel() {

    private var currentCollections: Flow<PagingData<CollectionModel>>? = null

    fun getCollections(): Flow<PagingData<CollectionModel>> {
        return currentCollections?: repository.getCollections()
                .cachedIn(viewModelScope)
                .also { currentCollections = it }
    }

}