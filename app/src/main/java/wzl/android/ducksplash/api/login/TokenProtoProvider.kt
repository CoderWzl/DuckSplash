package wzl.android.ducksplash.api.login

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import wzl.android.ducksplash.LoginPreferences
import wzl.android.ducksplash.USPLASH_CLIENT_ID
import wzl.android.ducksplash.USPLASH_CLIENT_SECRET
import wzl.android.ducksplash.model.MeModel
import wzl.android.ducksplash.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 *Created on 2021/3/9
 *@author zhilin
 * 提供 token 的 DataStore-proto 实现
 */

private const val TAG = "TokenProtoProvider"

@Singleton
class TokenProtoProvider @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore: DataStore<LoginPreferences> = context.createDataStore(
        fileName = "login_prefs.proto",
        serializer = LoginPreferencesSerializer
    )

    val clientId: String
        get() {
            // TODO: 2021/2/5 ClientId 这种密钥信息不应该出现在代码里
            return USPLASH_CLIENT_ID
        }

    val clientSecret: String
        get() {
            return USPLASH_CLIENT_SECRET
        }

    private val _unsplashAuthCode = MutableLiveData<String>()
    val unsplashAuthCode: LiveData<String> = _unsplashAuthCode
    fun emitUnsplashAuthCode(code: String) = _unsplashAuthCode.postValue(code)

    val loginPreferences = dataStore.data

    suspend fun saveAccessToken(token: String) {
        dataStore.updateData {
            it.toBuilder().setAccessToken(token).build()
        }
    }

    suspend fun saveUserProfile(me: MeModel) {
        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setEmail(me.email)
                .setUserName(me.username)
                .setProfilePicture(me.profileImage?.large)
                .build()
        }
    }

    suspend fun reset() {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setAccessToken("")
                .setProfilePicture("")
                .setUserName("")
                .setEmail("")
                .build()
        }
    }

    val loginUrl: String
        get() = "https://unsplash.com/oauth/authorize" +
                "?client_id=${clientId}" +
                "&redirect_uri=ducksplash%3A%2F%2F${unsplashAuthCallback}" +
                "&response_type=code" +
                "&scope=public+read_user+write_user+read_photos+write_photos" +
                "+write_likes+write_followers+read_collections+write_collections"

    companion object {

        const val unsplashAuthCallback = "unsplash-auth-callback"

        const val redirectUri = "ducksplash://$unsplashAuthCallback"
        const val grantType = "authorization_code"
    }

}