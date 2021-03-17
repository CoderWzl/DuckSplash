package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import wzl.android.ducksplash.api.login.TokenProtoProvider
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.repository.PhotoRepository
import java.io.IOException
import java.lang.Exception

/**
 *Created on 2021/1/26
 *@author zhilin
 */
class PhotoDetailViewModel @ViewModelInject constructor(
    private val repository: PhotoRepository,
    private val tokenProvider: TokenProtoProvider
): ViewModel() {

    private val _photo: MutableLiveData<PhotoModel> = MutableLiveData()
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    private val _error: MutableLiveData<String> = MutableLiveData()

    val photo: LiveData<PhotoModel> = _photo
    val loading: LiveData<Boolean> = _loading
    val error: LiveData<String> = _error
    private var curPhotoId: String? = null

    fun getPhoto(photoId: String) {
        if (curPhotoId != null && curPhotoId == photoId) {
            return
        }
        curPhotoId = photoId
        viewModelScope.launch {
            _loading.postValue(true)
            try {
                val photoModel = repository.getPhoto(photoId)
                _photo.postValue(photoModel)
                _loading.postValue(false)
            } catch (throwable: Throwable) {
                _loading.postValue(false)
                when(throwable) {
                    is IOException -> {
                        _error.postValue("Io exception")
                    }
                    is HttpException -> {
                        _error.postValue("Http exception")
                    }
                    else -> _error.postValue(throwable.localizedMessage)
                }
            }
        }
    }

    fun isUserAuthorized() = runBlocking {
        val prefs = tokenProvider.loginPreferences.first()
        !prefs.accessToken.isNullOrEmpty()
    }

    fun likePhoto(id: String) {
        viewModelScope.launch {
            try {
                repository.likePhoto(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun unlikePhoto(id: String) {
        viewModelScope.launch {
            try {
                repository.likePhoto(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}