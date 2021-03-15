package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wzl.android.ducksplash.api.ApiState
import wzl.android.ducksplash.model.UserModel
import wzl.android.ducksplash.repository.UserRepository
import java.lang.Exception

/**
 *Created on 2021/3/15
 *@author zhilin
 */
class UserViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loadUserState = MutableLiveData<ApiState<UserModel>>()

    val loadUserState: LiveData<ApiState<UserModel>> = _loadUserState

    fun getUserPublicProfile(userName: String) {
        viewModelScope.launch {
            _loadUserState.postValue(ApiState.Loading)
            try {
                val userModel = userRepository.getUserPublicProfile(userName)
                _loadUserState.postValue(ApiState.Success(userModel))
            } catch (e : Exception) {
                e.printStackTrace()
                _loadUserState.postValue(ApiState.Error(message = e.localizedMessage))
            }
        }
    }

}