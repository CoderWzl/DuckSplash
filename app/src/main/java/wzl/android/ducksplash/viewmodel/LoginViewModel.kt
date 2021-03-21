package wzl.android.ducksplash.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wzl.android.ducksplash.api.ApiState
import wzl.android.ducksplash.api.login.TokenProtoProvider
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.model.TokenModel
import wzl.android.ducksplash.repository.LoginRepository
import wzl.android.ducksplash.repository.PhotoRepository
import java.lang.Exception

/**
 *Created on 2021/3/11
 *@author zhilin
 */
class LoginViewModel @ViewModelInject constructor(
    private val tokenProvider: TokenProtoProvider,
    private val photoRepository: PhotoRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _photoData = MutableLiveData<PhotoModel>()

    private val _loginState = MutableLiveData<ApiState<TokenModel>>()

    val photoData: LiveData<PhotoModel> = _photoData

    val loginUrl = tokenProvider.loginUrl

    val authCode = tokenProvider.unsplashAuthCode

    val loginState: LiveData<ApiState<TokenModel>> = _loginState

    fun getRandomPhoto() {
        viewModelScope.launch {
            try {
                _photoData.postValue(
                    photoRepository.getRandomPhoto()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startLogin(code: String) {
        viewModelScope.launch {
            _loginState.value = ApiState.Loading
            try {
                val accessToken = loginRepository.getAccessToken(
                    tokenProvider.clientId,
                    tokenProvider.clientSecret,
                    code
                )
                Log.i("wzl", "startLogin: $accessToken")
                accessToken.accessToken?.let { token ->
                    tokenProvider.saveAccessToken(token)
                    val me = loginRepository.getMe()
                    Log.i("wzl", "startLogin: $me")
                    tokenProvider.saveUserProfile(me)
                }
                _loginState.value = ApiState.Success(accessToken)
            } catch (e: Exception) {
                tokenProvider.reset()
                _loginState.value = ApiState.Error(message = e.localizedMessage)
            }
        }
    }


}