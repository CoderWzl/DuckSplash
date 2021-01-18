package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName

/**
 *Created on 1/18/21
 *@author zhilin
 */
data class UserPhotoModel(
    val id: String,
    val urls: UrlsModel,
    @SerializedName("created_at") val createdTime: String,
    @SerializedName("updated_at") val updatedTime: String,
    @SerializedName("blur_hash") val blurHash: String,
)
