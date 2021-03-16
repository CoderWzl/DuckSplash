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
data class LocationModel(
    val city: String?,
    val country: String?,
    val position: Position?
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Position(
    val latitude: Double?,
    val longitude: Double?
): Parcelable
