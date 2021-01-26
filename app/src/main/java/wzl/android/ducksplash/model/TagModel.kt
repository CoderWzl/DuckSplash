package wzl.android.ducksplash.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *Created on 1/25/21
 *@author zhilin
 */
@Parcelize
data class TagModel(
    val title: String?
): Parcelable
