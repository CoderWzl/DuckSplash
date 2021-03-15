package wzl.android.ducksplash.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import wzl.android.ducksplash.LoginPreferences
import wzl.android.ducksplash.api.login.TokenProtoProvider

/**
 *Created on 2021/3/9
 *@author zhilin
 *
 * MainActivity
 */
class MainViewModel @ViewModelInject constructor(
    private val tokenProvider: TokenProtoProvider
) : ViewModel() {

    val loginPrefs: LiveData<LoginPreferences> = tokenProvider.loginPreferences.asLiveData()

    val isLogin: Boolean
        get() {
            var isAuthorized = false;
            viewModelScope.launch {
                val prefs = tokenProvider.loginPreferences.firstOrNull()
                isAuthorized = !prefs?.accessToken.isNullOrEmpty()
            }
            return isAuthorized
        }

    fun updateAccessToken(token: String) {
        viewModelScope.launch {
            tokenProvider.saveAccessToken(token)
        }
    }

    fun unsplashAuthCallback(code: String) {
        tokenProvider.emitUnsplashAuthCode(code)
    }

    fun logout() {
        viewModelScope.launch {
            tokenProvider.reset()
        }
    }
}