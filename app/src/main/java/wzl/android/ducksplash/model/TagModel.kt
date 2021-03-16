package wzl.android.ducksplash.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 *Created on 1/25/21
 *@author zhilin
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class TagModel(
    val type: String?,
    val title: String?
): Parcelable
