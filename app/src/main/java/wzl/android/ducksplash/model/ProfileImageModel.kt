package wzl.android.ducksplash.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 *Created on 2021/1/11
 *@author zhilin
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class ProfileImageModel(
    val small: String,
    val medium: String,
    val large: String
): Parcelable