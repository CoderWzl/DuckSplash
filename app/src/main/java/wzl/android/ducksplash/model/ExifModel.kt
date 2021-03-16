package wzl.android.ducksplash.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 *Created on 1/25/21
 *@author zhilin
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class ExifModel(
    // 设备制造商
    val make: String?,
    // 设备型号
    val model: String?,
    // 快门时间
    @Json(name = "exposure_time") val exposureTime: String?,
    // 光圈
    val aperture: String?,
    // 焦距
    @Json(name = "focal_length") val focalLength: String?,
    // 曝光度
    val iso: Int?
): Parcelable
