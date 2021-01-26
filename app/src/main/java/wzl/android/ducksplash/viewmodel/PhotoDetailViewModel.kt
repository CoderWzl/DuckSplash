package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import wzl.android.ducksplash.repository.PhotoRepository

/**
 *Created on 2021/1/26
 *@author zhilin
 */
class PhotoDetailViewModel @ViewModelInject constructor(
    private val repository: PhotoRepository
): ViewModel() {

}