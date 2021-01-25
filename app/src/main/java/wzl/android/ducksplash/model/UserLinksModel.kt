package wzl.android.ducksplash.model

import kotlinx.parcelize.Parcelize

/**
 *Created on 2021/1/11
 *@author zhilin
 */
@Parcelize
data class UserLinksModel(
    val self: String,
    val html: String,
    val photos: String,
    val likes: String,
    val portfolio: String,
    val following: String,
    val followers: String
)