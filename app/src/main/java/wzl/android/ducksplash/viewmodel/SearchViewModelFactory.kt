package wzl.android.ducksplash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import wzl.android.ducksplash.repository.SearchRepository

/**
 *Created on 2021/1/22
 *@author zhilin
 */
class SearchViewModelFactory(
        val repository: SearchRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("unknow view model")
    }

}