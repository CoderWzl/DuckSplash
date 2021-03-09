package wzl.android.ducksplash.api.login

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import wzl.android.ducksplash.LoginPreferences
import wzl.android.ducksplash.USPLASH_CLIENT_ID
import wzl.android.ducksplash.USPLASH_CLIENT_SECRET
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

    val loginPreferences = dataStore.data

    suspend fun saveAccessToken(token: String) {
        dataStore.updateData {
            it.toBuilder().setAccessToken(token).build()
        }
    }

    suspend fun saveUserProfile() {
        // TODO: 2021/3/9 Me
    }

}