package wzl.android.ducksplash.util

import wzl.android.ducksplash.model.PhotoModel
import java.util.*

/**
 *Created on 2021/4/13
 *@author zhilin
 */
val PhotoModel.fileName: String
    get() = "${this.user?.name?.toLowerCase(Locale.ROOT)?.replace(" ", "-")}-${this.id}.jpg"