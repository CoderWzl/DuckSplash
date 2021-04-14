package wzl.android.ducksplash.util

import wzl.android.ducksplash.model.PhotoModel
import java.util.*

/**
 *Created on 2021/4/13
 *@author zhilin
 */
private const val RAW = "raw"
private const val FULL = "full"
private const val REGULAR = "regular"
private const val SMALL = "small"
private const val THUMB = "thumb"

fun getPhotoUrl(photo: PhotoModel, quality: String?): String {
    return when (quality) {
        RAW -> photo.urls.raw
        FULL -> photo.urls.full
        REGULAR -> photo.urls.regular
        SMALL -> photo.urls.small
        THUMB -> photo.urls.thumb
        else -> photo.urls.regular
    }
}

val PhotoModel.fileName: String
    get() = "${this.user?.name?.toLowerCase(Locale.ROOT)?.replace(" ", "-")}-${this.id}.jpg"