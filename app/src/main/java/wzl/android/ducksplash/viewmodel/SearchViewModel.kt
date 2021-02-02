package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.model.UserModel
import wzl.android.ducksplash.repository.SearchRepository

class SearchViewModel @ViewModelInject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _queryLiveData = MutableLiveData("")

    val queryLiveData: LiveData<String> = _queryLiveData

    private var currentPhotos: LiveData<PagingData<PhotoModel>>? = null
    private var curPhotoQuery: String? = null
    private var currentCollections: LiveData<PagingData<CollectionModel>>? = null
    private var curCollectionQuery: String? = null
    private var currentUsers: LiveData<PagingData<UserModel>>? = null
    private var curUserQuery: String? = null

    fun updateQuery(query: String) {
        _queryLiveData.postValue(query)
    }

    fun searchPhotos(query: String): LiveData<PagingData<PhotoModel>> {
        if (curPhotoQuery == query && currentPhotos != null) {
            return currentPhotos!!
        }
        curPhotoQuery = query
        return repository.searchPhotos(query)
                .cachedIn(viewModelScope)
                .also {
                    currentPhotos = it
                }
    }

    fun searchCollections(query: String): LiveData<PagingData<CollectionModel>> {
        if (curCollectionQuery == query && currentCollections != null) {
            return currentCollections!!
        }
        curCollectionQuery = query
        return repository.searchCollections(query)
                .cachedIn(viewModelScope)
                .also {
                    currentCollections = it
                }
    }

    fun searchUsers(query: String): LiveData<PagingData<UserModel>> {
        if (curUserQuery == query && currentUsers != null) {
            return currentUsers!!
        }
        curUserQuery = query
        return repository.searchUsers(query)
                .cachedIn(viewModelScope)
                .also {
                    currentUsers = it
                }
    }

}