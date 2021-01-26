package wzl.android.ducksplash.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *Created on 2021/1/11
 *@author zhilin
 */
@Parcelize
data class PhotoModel(
    val id: String,
    @SerializedName("created_at")
    val createdTime: String,
    @SerializedName("updated_at")
    val updatedTime: String,
    @SerializedName("promoted_at")
    val promotedTime: String,
    val width: Int,
    val height: Int,
    val color: String,
    @SerializedName("blur_hash")
    val blurHash: String,
    val description: String,
    val user: UserModel,
    @SerializedName("alt_description")
    val altDescription: String,
    val urls: UrlsModel,
    val links: LinksModel,
    val likes: Int,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean,
    val downloads: Int,
    val exif: ExifModel?,
    val location: LocationModel?,
    val tags: List<TagModel>?,
    @SerializedName("current_user_collections")
    val currentUserCollections: List<CollectionModel>?
): Parcelable