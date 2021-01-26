package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.repository.PhotoRepository
import java.io.IOException

/**
 *Created on 2021/1/26
 *@author zhilin
 */
class PhotoDetailViewModel @ViewModelInject constructor(
    private val repository: PhotoRepository
): ViewModel() {

    private val _photo: MutableLiveData<PhotoModel> = MutableLiveData()
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    private val _error: MutableLiveData<String> = MutableLiveData()

    val photo: LiveData<PhotoModel> = _photo
    val loading: LiveData<Boolean> = _loading
    val error: LiveData<String> = _error

    fun getPhoto(photoId: String) {
        viewModelScope.launch {
            _loading.postValue(true)
            try {
                val photoModel = repository.getPhoto(photoId)
                _photo.postValue(photoModel)
            } catch (throwable: Throwable) {
                when(throwable) {
                    is IOException -> {
                        _error.postValue("Io exception")
                    }
                    is HttpException -> {
                        _error.postValue("Http exception")
                    }
                    else -> _error.postValue(throwable.localizedMessage)
                }
            } finally {
                _loading.postValue(false)
            }
        }
    }

}