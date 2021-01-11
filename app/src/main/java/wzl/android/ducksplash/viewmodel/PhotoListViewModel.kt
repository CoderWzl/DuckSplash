package wzl.android.ducksplash.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wzl.android.ducksplash.api.getNetworkService

private const val TAG = "PhotoListViewModel"

class PhotoListViewModel : ViewModel() {

    fun test() {
        viewModelScope.launch {
            val result = getNetworkService().getPhotoList()
            Log.d(TAG, "test: $result")
        }
    }
}