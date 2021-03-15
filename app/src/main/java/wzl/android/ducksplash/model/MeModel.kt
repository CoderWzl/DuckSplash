package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName

/**
 *Created on 2021/3/12
 *@author zhilin
 */
data class MeModel(
    val id: String,
    @SerializedName("updated_at") val updatedTime: String,
    val username: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("twitter_username") val twitterName: String?,
    @SerializedName("portfolio_url") val portfolioUrl: String?,
    val bio: String?,
    val location: String?,
    val links: LinksModel?,
    @SerializedName("profile_image") val profileImage: ProfileImageModel?,
    @SerializedName("total_likes") val totalLikes: Int?,
    @SerializedName("total_photos") val totalPhotos: Int?,
    @SerializedName("total_collections") val totalCollections: Int?,
    val photos: List<PhotoModel>?,
    @SerializedName("followed_by_user") val followedByUser: Boolean?,
    @SerializedName("followers_count") val followersCount: Int?,
    @SerializedName("following_count") val followingCount: Int?,
    val downloads: Int?,
    @SerializedName("uploads_remaining") val uploadsRemaining: Int?,
    val email: String?
)
