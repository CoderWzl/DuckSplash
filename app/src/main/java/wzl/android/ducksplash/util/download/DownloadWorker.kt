package wzl.android.ducksplash.util.download

import android.app.NotificationManager
import android.content.Context
import androidx.work.*
import wzl.android.ducksplash.api.DownloadService
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

    @Inject lateinit var downloadService: DownloadService
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_INPUT_URL) ?: return Result.failure()
        val fileName = inputData.getString(KEY_OUTPUT_FILE_NAME) ?: return Result.failure()

        val notificationId = id.hashCode()
        val cancelIntent = WorkManager.getInstance(context).createCancelPendingIntent(id)
        //val notificationBuilder = notificationManager.get

        return Result.success()
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