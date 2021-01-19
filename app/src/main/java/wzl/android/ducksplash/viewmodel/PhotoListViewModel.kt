package wzl.android.ducksplash.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wzl.android.ducksplash.repository.PhotoListRepository
import java.lang.Exception

private const val TAG = "PhotoListViewModel"

class PhotoListViewModel : ViewModel() {

    private val repository = PhotoListRepository()

    private val loading = MutableLiveData(false)

    val photoList = repository.photoList

    var inited = false

    private var curPage: Int = -1

    // 加载图片列表
    fun loadPhotoList() {
        if (loading.value as Boolean) {
            return
        }
        viewModelScope.launch {
            loading.value = true
            try {
                repository.loadPhotoList(curPage + 1)
                curPage += 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
            loading.value = false
        }
    }
}