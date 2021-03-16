package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 *Created on 2021/3/12
 *@author zhilin
 */
@JsonClass(generateAdapter = true)
data class MeModel(
    val id: String,
    @Json(name = "updated_at") val updatedTime: String,
    val username: String?,
    @Json(name = "first_name") val firstName: String?,
    @Json(name = "last_name") val lastName: String?,
    @Json(name = "twitter_username") val twitterName: String?,
    @Json(name = "portfolio_url") val portfolioUrl: String?,
    val bio: String?,
    val location: String?,
    val links: LinksModel?,
    @Json(name = "profile_image") val profileImage: ProfileImageModel?,
    @Json(name = "total_likes") val totalLikes: Int?,
    @Json(name = "total_photos") val totalPhotos: Int?,
    @Json(name = "total_collections") val totalCollections: Int?,
    val photos: List<PhotoModel>?,
    @Json(name = "followed_by_user") val followedByUser: Boolean?,
    @Json(name = "followers_count") val followersCount: Int?,
    @Json(name = "following_count") val followingCount: Int?,
    val downloads: Int?,
    @Json(name = "uploads_remaining") val uploadsRemaining: Int?,
    val email: String?
)
