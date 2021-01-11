package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName

/**
 *Created on 2021/1/11
 *@author zhilin
 */
data class PhotoModel(
    val id: String,
    @SerializedName("created_at") val createdTime: String,
    @SerializedName("updated_at") val updatedTime: String,
    @SerializedName("promoted_at") val promotedTime: String,
    val width: Int,
    val height: Int,
    val color: String,
    @SerializedName("blur_hash") val blurHash: String,
    val description: String,
    @SerializedName("alt_description") val altDescription: String,
    val urls: UrlsModel,
    val links: LinksModel,
    val likes: Int,
    @SerializedName("liked_by_user") val likedByUser: Boolean,
    val sponsorship: String
)