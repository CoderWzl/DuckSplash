package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 *Created on 2021/3/12
 *@author zhilin
 */
@JsonClass(generateAdapter = true)
data class TokenModel(
    @Json(name = "access_token") val accessToken: String?,
    @Json(name = "token_type") val tokenType: String?,
    val scope: String?,
    @Json(name = "create_at") val createTime: Long?
)
