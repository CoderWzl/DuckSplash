package wzl.android.ducksplash.repository

import androidx.lifecycle.MutableLiveData
import wzl.android.ducksplash.api.SearchService
import wzl.android.ducksplash.api.getNetworkService
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.model.SearchModel
import wzl.android.ducksplash.model.UserModel

/**
 *Created on 2021/1/18
 *@author zhilin
 */
class SearchRepository(private val service: SearchService) {

    val searchPhotoResult = MutableLiveData<SearchModel<PhotoModel>>()

    val searchCollectionResult = MutableLiveData<SearchModel<CollectionModel>>()

    val searchUserResult = MutableLiveData<SearchModel<UserModel>>()

    suspend fun searchPhotoList(
        query: String,
        page: Int,
        perPage: Int = 30,
        orderBy: String? = null,
        collections: String? = null,
        contentFilter: String? = null,
        color: String? = null,
        orientation: String? = null
    ) {
        val result = service.searchPhotos(query, page, perPage, orderBy, collections, contentFilter, color, orientation)
        searchPhotoResult.value = result
    }

    suspend fun searchCollectionList(
        query: String,
        page: Int,
        perPage: Int = 30
    ) {
        searchCollectionResult.value = service.searchCollections(query, page, perPage)
    }

    suspend fun searchUserList(
        query: String,
        page: Int,
        perPage: Int = 30
    ) {
        searchUserResult.value = service.searchUsers(query, page, perPage)
    }
}