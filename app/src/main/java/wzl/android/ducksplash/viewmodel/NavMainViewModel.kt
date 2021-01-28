package wzl.android.ducksplash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import wzl.android.ducksplash.util.SingleLiveEvent

/**
 *Created on 2021/1/28
 *@author zhilin
 */
class NavMainViewModel: ViewModel() {

    private val _photoIso = SingleLiveEvent<String>()
    @Suppress("UNCHECKED_CAST")
    val photoIso: LiveData<String> = _photoIso as LiveData<String>

    fun sendPhotoIso(iso: String) {
        _photoIso.postValue(iso)
    }
}