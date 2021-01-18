package wzl.android.ducksplash.model

import com.google.gson.annotations.SerializedName

/**
 *Created on 2021/1/18
 *@author zhilin
 */
data class SearchModel<T>(
    val total: Int,
    @SerializedName("total_pages") val totalPages: Int,
    val results: List<T>
)