package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 *Created on 2021/1/18
 *@author zhilin
 */
@JsonClass(generateAdapter = true)
data class SearchModel<T>(
    val total: Int,
    @Json(name = "total_pages") val totalPages: Int,
    val results: List<T>
)