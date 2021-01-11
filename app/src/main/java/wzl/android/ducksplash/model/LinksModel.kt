package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName

/**
 *Created on 2021/1/11
 *@author zhilin
 */
data class LinksModel(
    val self: String,
    val html: String,
    val download: String,
    @SerializedName("download_location") val downloadLocation: String
)