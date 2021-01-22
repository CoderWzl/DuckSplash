package wzl.android.ducksplash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import wzl.android.ducksplash.repository.CollectionRepository

/**
 *Created on 2021/1/22
 *@author zhilin
 */
class CollectionListViewModelFactory(private val repository: CollectionRepository):
        ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectionListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CollectionListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow view model")
    }

}