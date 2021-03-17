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
data class LinksModel(
    val self: String,
    val html: String,
    val download: String?,
    @Json(name = "download_location") val downloadLocation: String
): Parcelable