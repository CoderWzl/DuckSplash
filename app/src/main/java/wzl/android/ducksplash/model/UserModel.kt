package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName

/**
 *Created on 2021/1/11
 *@author zhilin
 */
data class UserModel(
    val id: String,
    @SerializedName("updated_at") val updatedTime: String,
    val username: String,
    val name: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("twitter_username") val twitterName: String,
    @SerializedName("portfolio_url") val portfolioUrl: String,
    val bio: String,
    val location: String,
    val links: UserLinksModel,
    @SerializedName("profile_image") val profileImage: ProfileImageModel,
    @SerializedName("instagram_username") val instagramName: String,
    @SerializedName("total_collections") val totalCollections: Int,
    @SerializedName("total_likes") val totalLikes: Int,
    @SerializedName("total_photos") val totalPhotos: Int,
    @SerializedName("accepted_tos") val acceptedTos: Boolean,
)