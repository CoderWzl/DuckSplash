package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.repository.UserRepository

/**
 *Created on 2021/2/2
 *@author zhilin
 */
class UserCollectionViewModel @ViewModelInject constructor(
    private val repository: UserRepository
): ViewModel() {

    private var collections: Flow<PagingData<CollectionModel>>? = null

    fun listUserCollections(username: String): Flow<PagingData<CollectionModel>> {
        if (collections != null) {
            return collections!!
        }
        return repository.listUserCollections(username)
            .cachedIn(viewModelScope)
            .also { collections = it }
    }

}