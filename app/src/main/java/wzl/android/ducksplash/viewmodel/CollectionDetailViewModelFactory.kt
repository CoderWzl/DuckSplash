package wzl.android.ducksplash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import wzl.android.ducksplash.repository.PhotoRepository

/**
 *Created on 2021/1/22
 *@author zhilin
 */
class CollectionDetailViewModelFactory(
        private val repository: PhotoRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectionDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CollectionDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("unknow view model")
    }

}