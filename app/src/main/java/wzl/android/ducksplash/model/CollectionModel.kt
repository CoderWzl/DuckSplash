package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName

/**
 *Created on 1/17/21
 *@author zhilin
 */
data class CollectionModel(
    val id: Int,
    val title: String,
    val description: String,
    @SerializedName("published_at") val publishedTime: String,
    @SerializedName("updated_at") val updatedTime: String,
    @SerializedName("last_collected_at") val lastCollectedTime: String,
    @SerializedName("total_photos") val totalPhotos: Int,
    val private: Boolean,
    @SerializedName("share_key") val shareKey: String,
    @SerializedName("cover_photo") val coverPhoto: PhotoModel
)
