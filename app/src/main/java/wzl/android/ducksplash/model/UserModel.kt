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
data class UserModel(
    val id: String,
    @Json(name = "updated_at") val updatedTime: String?,
    val username: String?,
    val name: String?,
    @Json(name = "first_name") val firstName: String?,
    @Json(name = "last_name") val lastName: String?,
    @Json(name = "twitter_username") val twitterName: String?,
    @Json(name = "portfolio_url") val portfolioUrl: String?,
    val bio: String?,
    val location: String?,
    val links: UserLinksModel?,
    @Json(name = "profile_image") val profileImage: ProfileImageModel?,
    @Json(name = "instagram_username") val instagramName: String?,
    @Json(name = "total_collections") val totalCollections: Int?,
    @Json(name = "total_likes") val totalLikes: Int?,
    @Json(name = "total_photos") val totalPhotos: Int?,
    @Json(name = "accepted_tos") val acceptedTos: Boolean?,
    val photos: List<PhotoModel>?
): Parcelable