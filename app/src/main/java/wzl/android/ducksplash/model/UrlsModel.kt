package wzl.android.ducksplash.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *Created on 2021/1/11
 *@author zhilin
 */
@Parcelize
data class UrlsModel(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
): Parcelable