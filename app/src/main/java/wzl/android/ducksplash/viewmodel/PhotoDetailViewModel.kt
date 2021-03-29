package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import wzl.android.ducksplash.DEFAULT_PER_PAGE_SIZE
import wzl.android.ducksplash.adapter.AddState
import wzl.android.ducksplash.api.ApiState
import wzl.android.ducksplash.api.login.TokenProtoProvider
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.repository.CollectionRepository
import wzl.android.ducksplash.repository.PhotoRepository
import wzl.android.ducksplash.repository.UserRepository
import java.lang.Exception

/**
 *Created on 2021/1/26
 *@author zhilin
 */
class PhotoDetailViewModel @ViewModelInject constructor(
    private val repository: PhotoRepository,
    private val tokenProvider: TokenProtoProvider,
    private val userRepository: UserRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    // 登录用户包含该图片的所有合集 id 信息
    private val _currentUserCollections = MutableLiveData<MutableList<Int>?>()
    private val _userCollections = MutableLiveData<MutableList<CollectionModel>?>()

    val currentUserCollections: LiveData<MutableList<Int>?> = _currentUserCollections
    val userCollections: LiveData<MutableList<CollectionModel>?> = _userCollections

    // liveData 函数不添加 coroutineContext 和添加 coroutineContext 有什么区别
    fun getPhoto(photoId: String) = liveData (viewModelScope.coroutineContext) {
        emit(ApiState.Loading)
        try {
            val photoModel = repository.getPhoto(photoId)
            emit(ApiState.Success(photoModel))
        } catch (throwable: Throwable) {
            emit(ApiState.Error(message = throwable.localizedMessage))
        }
    }

    // 获取包含所有这张图片的用户图集 id 信息，用来判断是否被用户收藏
    fun setCurrentUserCollections(photo: PhotoModel) {
        _currentUserCollections.postValue(
            photo.currentUserCollections?.map { it.id }?.toMutableList()
        )
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

    // load user collections

    private var page = 1
    var isLoading = false
    var onLastPage = false

    fun loadMoreUserCollections() {
        if (isLoading || onLastPage) {
            return
        }
        viewModelScope.launch {
            isLoading = true
            val username = tokenProvider.loginPreferences.first().userName?:return@launch
            try {
                val result = userRepository.listUserCollections(username, page)
                val newList = _userCollections.value ?: mutableListOf()
                newList.addAll(result)
                _userCollections.postValue(newList)
                onLastPage = result.isEmpty() || result.size < DEFAULT_PER_PAGE_SIZE
                page++
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun addPhotoToCollection(collectionId: Int, photoId: String, position: Int) =
        liveData {
            emit(AddState.Adding(collectionId))
            try {
                val result = collectionRepository.addPhotoToCollection(collectionId, photoId)
                val newList = _currentUserCollections.value ?: mutableListOf()
                newList.add(collectionId)
                _currentUserCollections.postValue(newList)

                val newCollectionList = _userCollections.value
                result.collection?.let { newCollectionList?.set(position, it) }
                _userCollections.postValue(newCollectionList)
                emit(AddState.Added(result))
            } catch (e: Exception) {
                emit(AddState.NotAdd(collectionId))
            }
        }

}