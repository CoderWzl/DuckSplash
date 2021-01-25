package wzl.android.ducksplash.model

import kotlinx.parcelize.Parcelize

/**
 *Created on 1/25/21
 *@author zhilin
 */
@Parcelize
data class LocationModel(
    val city: String?,
    val country: String?,
    val position: Position?
)

@Parcelize
data class Position(
    val latitude: Double,
    val longitude: Double
)
