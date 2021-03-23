package wzl.android.ducksplash.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 *Created on 3/22/21
 *@author zhilin
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class CollectionPhotoResult(
    val photo: PhotoModel?,
    val collection: CollectionModel?
): Parcelable
