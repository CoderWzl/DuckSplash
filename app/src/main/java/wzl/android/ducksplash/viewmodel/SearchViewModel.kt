package wzl.android.ducksplash.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wzl.android.ducksplash.repository.SearchRepository

private const val TAG = "SearchViewModel"
class SearchViewModel : ViewModel() {

    private val repository = SearchRepository()
    private val _queryLiveData = MutableLiveData("")

    val queryLiveData: LiveData<String> = _queryLiveData

    val photoSearchResult = repository.searchPhotoResult

    val collectionSearchResult = repository.searchCollectionResult

    val userSearchResult = repository.searchUserResult

    fun updateQuery(query: String) {
        Log.d(TAG, "updateQuery: $query")
        _queryLiveData.postValue(query)
    }

    fun searchPhotoList(query: String) {
        if (query.isBlank()) {
            return
        }
        viewModelScope.launch {
            repository.searchPhotoList(query, 0)
        }
    }

    fun searchCollectionList(query: String) {
        if (query.isBlank()) {
            return
        }
        viewModelScope.launch {
            repository.searchCollectionList(query, 0)
        }
    }

    fun searchUserList(query: String) {
        if (query.isBlank()) {
            return
        }
        viewModelScope.launch {
            repository.searchUserList(query, 0)
        }
    }
}