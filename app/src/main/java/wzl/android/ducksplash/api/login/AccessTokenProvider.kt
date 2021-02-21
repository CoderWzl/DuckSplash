package wzl.android.ducksplash.api.login

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import wzl.android.ducksplash.USPLASH_CLIENT_ID
import wzl.android.ducksplash.USPLASH_CLIENT_SECRET
import wzl.android.ducksplash.util.getDefaultDataStoreName
import javax.inject.Inject
import javax.inject.Singleton

/**
 *Created on 2021/2/5
 *@author zhilin
 * author token 凭证
 */
@Singleton
class AccessTokenProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(getDefaultDataStoreName(context))

    val clientId: String
    get() {
        // TODO: 2021/2/5 ClientId 这种密钥信息不应该出现在代码里
        return USPLASH_CLIENT_ID
    }

    val clientSecret: String
    get() {
        return USPLASH_CLIENT_SECRET
    }

    val accessToken: Flow<String?>
    get() = dataStore.data.map {
        it[ACCESS_TOKEN_KEY]
    }

    val username: Flow<String?>
    get() = dataStore.data.map {
        it[USERNAME_KEY]
    }

    val email: Flow<String?>
    get() = dataStore.data.map {
        it[EMAIL_KEY]
    }

    val profilePicture: Flow<String?>
    get() = dataStore.data.map {
        it[PROFILE_PICTURE_KEY]
    }

    val isAuthorized: Flow<Boolean>
    get() = dataStore.data.map {
        it[ACCESS_TOKEN_KEY] != null
    }

    suspend fun saveAccessToken(accessToken: String) = dataStore.edit {
        it[ACCESS_TOKEN_KEY] = accessToken
    }



    companion object {

        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val USERNAME_KEY = stringPreferencesKey("user_name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PROFILE_PICTURE_KEY = stringPreferencesKey("profile_picture")

    }

}