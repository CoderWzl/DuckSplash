package wzl.android.ducksplash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import wzl.android.ducksplash.repository.PhotoRepository

/**
 *Created on 2021/1/21
 *@author zhilin
 */
class PhotoListViewModelFactory(private val repository: PhotoRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}