package wzl.android.ducksplash.util.download

import android.content.Context
import androidx.work.*
import java.util.*

/**
 *Created on 2021/4/14
 *@author zhilin
 *使用 Worker 进行图片的下载任务。
 */
class DownloadWorker(
    private val context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        // TODO: 2021/4/14 download
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