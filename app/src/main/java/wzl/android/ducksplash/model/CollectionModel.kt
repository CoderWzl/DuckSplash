package wzl.android.ducksplash.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 *Created on 1/17/21
 *@author zhilin
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class CollectionModel(
    val id: String,
    val title: String,
    val description: String?,
    @Json(name = "published_at") val publishedTime: String?,
    @Json(name = "updated_at") val updatedTime: String?,
    @Json(name = "last_collected_at") val lastCollectedTime: String?,
    @Json(name = "total_photos") val totalPhotos: Int?,
    val private: Boolean?,
    @Json(name = "share_key") val shareKey: String?,
    @Json(name = "cover_photo") var coverPhoto: PhotoModel?
): Parcelable
