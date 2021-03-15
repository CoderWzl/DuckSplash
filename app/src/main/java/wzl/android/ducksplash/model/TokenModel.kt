package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName

/**
 *Created on 2021/3/12
 *@author zhilin
 */
data class TokenModel(
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("token_type") val tokenType: String?,
    val scope: String?,
    @SerializedName("create_at") val createTime: Long?
)
