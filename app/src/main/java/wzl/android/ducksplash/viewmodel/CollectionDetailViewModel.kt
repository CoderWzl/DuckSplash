package wzl.android.ducksplash.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wzl.android.ducksplash.repository.PhotoListRepository

class CollectionDetailViewModel : ViewModel() {
    private val repository = PhotoListRepository()

    private var curPage: Int = 0

    var photoList = repository.photoList

    var loading = MutableLiveData<Boolean>(false)

    fun loadPhotoListWithCollectionId(id: Int) {
        if (loading.value == true) {
            return
        }
        viewModelScope.launch {
            loading.value = true
            repository.loadPhotoListWithCollectionId(id, curPage)
            curPage++
            loading.value = false
        }
    }
}