package wzl.android.ducksplash.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.flow.first
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
import wzl.android.ducksplash.util.download.DownloadManagerWrapper
import wzl.android.ducksplash.util.fileName
import wzl.android.ducksplash.util.getPhotoUrl
import java.lang.Exception

/**
 *Created on 2021/1/26
 *@author zhilin
 */
class PhotoDetailViewModel @ViewModelInject constructor(
    private val repository: PhotoRepository,
    private val tokenProvider: TokenProtoProvider,
    private val userRepository: UserRepository,
    private val collectionRepository: CollectionRepository,
    private val downloadManager: DownloadManagerWrapper
) : ViewModel() {

    // 登录用户包含该图片的所有合集 id 信息
    private val _currentUserCollections = MutableLiveData<MutableList<String>?>()
    private val _userCollections = MutableLiveData<MutableList<CollectionModel>?>()

    val currentUserCollections: LiveData<MutableList<String>?> = _currentUserCollections
    val userCollections: LiveData<MutableList<CollectionModel>?> = _userCollections

    private var _photoModel: PhotoModel? = null

    // liveData 函数不添加 coroutineContext 和添加 coroutineContext 有什么区别
    fun getPhoto(photoId: String) = liveData (viewModelScope.coroutineContext) {
        emit(ApiState.Loading)
        try {
            if (_photoModel == null) {
                _photoModel = repository.getPhoto(photoId)
                _currentUserCollections.postValue(
                    _photoModel?.currentUserCollections?.map { it.id }?.toMutableList()
                )
            }
            emit(ApiState.Success(_photoModel))
        } catch (throwable: Throwable) {
            emit(ApiState.Error(message = throwable.localizedMessage))
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

    fun addPhotoToCollection(collectionId: String, photoId: String, position: Int) =
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

    fun removePhotoFromCollection(collectionId: String, photoId: String, position: Int) =
        liveData<AddState> {
            emit(AddState.Removing(collectionId))
            try {
                val result = collectionRepository.removePhotoFromCollection(
                    collectionId = collectionId,
                    photoId = photoId
                )
                val newList = _currentUserCollections.value ?: mutableListOf()
                newList.remove(collectionId)
                _currentUserCollections.postValue(newList)
                val newCollectionList = _userCollections.value
                result.collection?.let { newCollectionList?.set(position, it) }
                _userCollections.postValue(newCollectionList)
                emit(AddState.NotAdd(collectionId))
            }catch (e: Exception) {
                Log.e("zhilin", "removePhotoFromCollection: ${e.localizedMessage}")
                emit(AddState.Error(collectionId))
            }
        }

    fun createCollection(name: String,
                         description: String?,
                         isPrivate: Boolean,
                         photoId: String) =
        liveData(viewModelScope.coroutineContext) {
            emit(ApiState.Loading)
            try {
                var createResult = collectionRepository.createCollection(
                    name = name,
                    description = description,
                    isPrivate = isPrivate
                )
                val result = collectionRepository.addPhotoToCollection(createResult.id, photoId)
                val newIdList = _currentUserCollections.value ?: mutableListOf()
                newIdList.add(createResult.id)
                _currentUserCollections.postValue(newIdList)

                result.collection?.let { createResult = it }

                val newList = _userCollections.value ?: mutableListOf()
                newList.add(0, createResult)
                _userCollections.postValue(newList)
                emit(ApiState.Success(data = createResult))
            }catch (e: Exception) {
                emit(ApiState.Error(message = e.localizedMessage))
            }
        }

    fun downloadPhoto(photo: PhotoModel): Long {
        val url = getPhotoUrl(photo, "full")
        return downloadManager.downloadPhoto(url, photo.fileName)
    }

}