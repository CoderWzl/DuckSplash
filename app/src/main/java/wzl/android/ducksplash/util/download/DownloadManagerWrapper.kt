package wzl.android.ducksplash.util.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.jeremyliao.liveeventbus.LiveEventBus
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import wzl.android.ducksplash.util.DUCKSPLASH_DIRECTORY
import wzl.android.ducksplash.util.error
import wzl.android.ducksplash.util.eventbus.DownloadEvent
import wzl.android.ducksplash.util.eventbus.EVENT_KEY_DOWNLOAD_PHOTO
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 *Created on 4/13/21
 *@author zhilin
 */
@Singleton
class DownloadManagerWrapper @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val downloadManager: DownloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    init {
        val downloadStatusReceiver = DownloadStatusReceiver()
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(downloadStatusReceiver, intentFilter)
    }

    fun downloadPhoto(url: String, fileName: String): Long {
        val request = createRequest(url, fileName, true)
        return downloadManager.enqueue(request)
    }

    private fun createRequest(
        url: String,
        fileName: String,
        showCompleteNotification: Boolean
    ): DownloadManager.Request {
        val destination = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Environment.DIRECTORY_PICTURES
        } else {
            Environment.DIRECTORY_DOWNLOADS
        }
        val subPath = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            "$DUCKSPLASH_DIRECTORY${File.separator}$fileName"
        } else {
            fileName
        }
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDestinationInExternalPublicDir(destination, subPath)
            .setNotificationVisibility(
                if (showCompleteNotification) {
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                } else {
                    DownloadManager.Request.VISIBILITY_VISIBLE
                }
            )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            request.setVisibleInDownloadsUi(true)
            request.allowScanningByMediaScanner()
        }
        return request
    }

    private inner class DownloadStatusReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0) ?: 0
            val query = DownloadManager.Query().apply { setFilterById(id) }
            val cursor = downloadManager.query(query)
            if (!cursor.moveToFirst()) {
                onError(cursor, id, "Cursor empty, this should't happened")
                return
            }
            val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if (cursor.getInt(statusIndex) != DownloadManager.STATUS_SUCCESSFUL) {
                onError(cursor, id, "Download Fail")
            } else {
                onSuccess(
                    cursor = cursor,
                    id = id,
                    uri = downloadManager.getUriForDownloadedFile(id)
                )
            }
        }

        private fun onError(cursor: Cursor, id: Long, message: String) {
            error("onError: $message")
            cursor.close()
            downloadManager.remove(id)
            // 下载完，发送事件。这里使用 LiveEventBus，也可以使用 LocalBroadcastManager
            LiveEventBus.get(EVENT_KEY_DOWNLOAD_PHOTO)
                .post(DownloadEvent(
                    result = false,
                    message = message
                ))
        }

        private fun onSuccess(
            cursor: Cursor,
            id: Long,
            uri: Uri
        ) {
            cursor.close()
            LiveEventBus.get(EVENT_KEY_DOWNLOAD_PHOTO)
                .post(DownloadEvent(
                    result = true,
                    message = "Download success",
                    uri = uri
                ))
        }

    }
}