package wzl.android.ducksplash.util.download

import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.work.*
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.buffer
import okio.sink
import wzl.android.ducksplash.api.DownloadService
import wzl.android.ducksplash.util.*
import wzl.android.ducksplash.util.eventbus.DownloadEvent
import wzl.android.ducksplash.util.eventbus.EVENT_KEY_DOWNLOAD_PHOTO
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 *Created on 2021/4/14
 *@author zhilin
 *使用 Worker 进行图片的下载任务。
 */
class DownloadWorker(
    private val context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    @Inject
    lateinit var downloadService: DownloadService
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_INPUT_URL) ?: return Result.failure()
        val fileName = inputData.getString(KEY_OUTPUT_FILE_NAME) ?: return Result.failure()

        val notificationId = id.hashCode()
        val cancelIntent = WorkManager.getInstance(context).createCancelPendingIntent(id)
        //val notificationBuilder = notificationManager.get
        download(url, fileName)

        return Result.success()
    }

    private suspend fun download(
        url: String,
        fileName: String
    ) = withContext(Dispatchers.IO) {
        try {
            val responseBody = downloadService.downloadFile(url)
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                responseBody.saveImage(context, fileName) {
                    // TODO: 2021/4/15 report download progress
                }
            } else {
                responseBody.saveImageLegacy(context, fileName) {
                    // TODO: 2021/4/15 report download progress
                }
            }

            if (uri != null) {
                onSuccess(fileName, uri)
                inputData.getString(KEY_PHOTO_ID)?.let {
                    try {
                        downloadService.trackDownload(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                onError(fileName, Exception("Failed writing to file"), STATUS_FAILED, true)
            }
        } catch (e: CancellationException) {
            onError(fileName, e, STATUS_CANCELLED, false)
        } catch (e: Exception) {
            onError(fileName, e, STATUS_FAILED, true)
        }

    }

    private fun onSuccess(
        fileName: String,
        uri: Uri
    ) {
        info("onSuccess: $fileName - $uri")
        // TODO: 2021/4/15 Download success notification
        LiveEventBus.get(EVENT_KEY_DOWNLOAD_PHOTO)
            .post(DownloadEvent(
                result = true,
                message = "Download Success",
                uri = uri
            ))
    }

    private fun onError(
        fileName: String,
        exception: Exception,
        status: Int,
        showNotification: Boolean
    ) {
        error("onError: $fileName", exception)
        // TODO: 2021/4/15 Download success notification
        LiveEventBus.get(EVENT_KEY_DOWNLOAD_PHOTO)
            .post(DownloadEvent(
                result = false,
                message = exception.localizedMessage,
            ))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun ResponseBody.saveImage(
        context: Context,
        fileName: String,
        onProgress: ((Int) -> Unit)?
    ): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.TITLE, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.Images.Media.SIZE, contentLength())
            put(MediaStore.Images.Media.RELATIVE_PATH, DUCKSPLASH_RELATIVE_PATH)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            val complete = resolver.openOutputStream(uri)?.use { outputStream ->
                writeToSink(outputStream.sink().buffer(), onProgress)
            } ?: false

            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
            if (!complete) {
                resolver.delete(uri, null, null)
                throw CancellationException("Cancelled by user")
            }
        }
        return uri
    }

    private fun ResponseBody.saveImageLegacy(
        context: Context,
        fileName: String,
        onProgress: ((Int) -> Unit)?
    ): Uri? {
        val path = File(DUCKSPLASH_LEGACY_PATH)
        if (!path.exists()) {
            if (!path.mkdirs()) return null
        }
        val file = File(path, fileName)
        val complete = writeToSink(file.sink().buffer(), onProgress)
        if (!complete && file.exists()) {
            file.delete()
            throw CancellationException("Cancelled by user")
        }

        MediaScannerConnection.scanFile(
            context, arrayOf(file.absolutePath),
            arrayOf("image/jpeg"), null
        )
        return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
    }

    private fun ResponseBody.writeToSink(
        sink: BufferedSink,
        onProgress: ((Int) -> Unit)?
    ): Boolean {
        val fileSize = contentLength()
        var totalBytesRead = 0L
        var progressToReport = 0
        while (true) {
            if (isStopped) return false
            val readCount = source().read(sink.buffer, 8192L)
            if (readCount == -1L) break
            sink.emit()
            totalBytesRead += readCount
            val progress = (100.0 * totalBytesRead / fileSize)
            if (progress - progressToReport >= 10) {
                progressToReport = progress.toInt()
                onProgress?.invoke(progressToReport)
            }
        }
        sink.close()
        return true
    }

    companion object {
        const val KEY_DOWNLOAD_ACTION = "KEY_DOWNLOAD_ACTION"
        const val KEY_INPUT_URL = "KEY_INPUT_URL"
        const val KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME"
        const val KEY_PHOTO_ID = "KEY_PHOTO_ID"

        fun enqueueDownload(
            context: Context,
            url: String,
            fileName: String,
            photoId: String?
        ): UUID {
            val inputData = workDataOf(
                KEY_INPUT_URL to url,
                KEY_OUTPUT_FILE_NAME to fileName,
                KEY_PHOTO_ID to photoId
            )
            val request = OneTimeWorkRequestBuilder<DownloadWorker>()
                .setInputData(inputData).build()
            WorkManager.getInstance(context).enqueue(request)
            return request.id
        }
    }
}