package wzl.android.ducksplash.util.eventbus

import android.net.Uri
import com.jeremyliao.liveeventbus.core.LiveEvent

/**
 *Created on 4/13/21
 *@author zhilin
 */
const val EVENT_KEY_DOWNLOAD_PHOTO = "download_photo"
data class DownloadEvent(
    val result: Boolean,
    val message: String?,
    val uri: Uri? = null
) : LiveEvent
