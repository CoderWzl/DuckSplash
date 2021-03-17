package wzl.android.ducksplash.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 *Created on 2021/1/11
 *@author zhilin
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class PhotoModel(
    val id: String,
    @Json(name = "created_at")
    val createdTime: String?,
    @Json(name = "updated_at")
    val updatedTime: String?,
    @Json(name = "promoted_at")
    val promotedTime: String?,
    val width: Int?,
    val height: Int?,
    val color: String? = "#E0E0E0",
    @Json(name = "blur_hash")
    val blurHash: String?,
    val description: String?,
    val user: UserModel?,
    @Json(name = "alt_description")
    val altDescription: String?,
    val urls: UrlsModel,
    val links: LinksModel?,
    val likes: Int?,
    @Json(name = "liked_by_user")
    var likedByUser: Boolean?,
    val downloads: Int?,
    val exif: ExifModel?,
    val location: LocationModel?,
    val tags: List<TagModel>?,
    @Json(name = "current_user_collections")
    val currentUserCollections: List<CollectionModel>?,
    @Json(name = "related_collections")
    val relatedCollections: RelatedCollectionsModel?,
    val views: Int?
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class RelatedCollectionsModel(
    val total: Int,
    val type:String?,
    val results: List<CollectionModel>?
): Parcelable