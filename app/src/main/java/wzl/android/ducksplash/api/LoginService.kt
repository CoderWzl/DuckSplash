package wzl.android.ducksplash.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import wzl.android.ducksplash.model.TokenModel

/**
 *Created on 2021/3/12
 *@author zhilin
 * https://unsplash.com/oauth/token
 */
interface LoginService {

    @POST("oauth/token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String, //Your application’s access key.
        @Field("client_secret") clientSecret: String, //Your application’s secret key.
        @Field("redirect_uri") redirectUri: String, //Your application’s redirect URI.
        @Field("code") code: String, //The authorization code supplied to the callback by Unsplash.
        @Field("grant_type") grantType: String, //Value “authorization_code”.
    ): TokenModel

}