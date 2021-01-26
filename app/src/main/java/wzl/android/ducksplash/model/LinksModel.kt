package wzl.android.ducksplash.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *Created on 2021/1/11
 *@author zhilin
 */
@Parcelize
data class LinksModel(
    val self: String,
    val html: String,
    val download: String,
    @SerializedName("download_location") val downloadLocation: String
): Parcelable