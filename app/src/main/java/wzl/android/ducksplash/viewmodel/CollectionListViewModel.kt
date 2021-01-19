package wzl.android.ducksplash.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wzl.android.ducksplash.repository.PhotoListRepository
import java.lang.Exception

class CollectionListViewModel : ViewModel() {

    private val repository = PhotoListRepository()

    val collectionList = repository.collectionList

    private val loading = MutableLiveData<Boolean>(false)

    private var curPage = 0

    var inited = false

    fun loadCollectionList() {
        if (loading.value == true) {
            return
        }
        viewModelScope.launch {
            loading.value = true
            try {
                repository.loadCollectionList(curPage)
                curPage++
            }catch (e: Exception) {
                e.printStackTrace()
            }
            loading.value = false
        }
    }
}