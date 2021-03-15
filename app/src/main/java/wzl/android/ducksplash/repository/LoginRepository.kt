package wzl.android.ducksplash.repository

import wzl.android.ducksplash.api.LoginService
import wzl.android.ducksplash.api.UserService
import wzl.android.ducksplash.api.login.TokenProtoProvider
import javax.inject.Inject

/**
 *Created on 2021/3/11
 *@author zhilin
 * https://unsplash.com/documentation/user-authentication-workflow
 */
class LoginRepository @Inject constructor(
    private val loginService: LoginService,
    private val userService: UserService
) {

    suspend fun getAccessToken(
        client: String,
        clientSecret: String,
        code: String
    ) = loginService.getAccessToken(
        clientId = client,
        clientSecret = clientSecret,
        redirectUri = TokenProtoProvider.redirectUri,
        code = code,
        grantType = TokenProtoProvider.grantType
    )

    suspend fun getMe() = userService.getUserPrivateProfile()

}