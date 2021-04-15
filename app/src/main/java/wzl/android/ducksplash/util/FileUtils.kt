package wzl.android.ducksplash.util

import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import wzl.android.ducksplash.BuildConfig
import wzl.android.ducksplash.R
import java.io.File

/**
 *Created on 2021/4/13
 *@author zhilin
 */
const val DUCKSPLASH_DIRECTORY = "DuckSplash"

const val FILE_PROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.fileprovider"

val DUCKSPLASH_RELATIVE_PATH = "${Environment.DIRECTORY_PICTURES}${File.separator}$DUCKSPLASH_DIRECTORY"

val DUCKSPLASH_LEGACY_PATH = "${Environment.getExternalStoragePublicDirectory(
    Environment.DIRECTORY_PICTURES)}${File.separator}$DUCKSPLASH_DIRECTORY"

fun Context.fileExists(fileName: String, downloader: String): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        val selection = "${MediaStore.MediaColumns.RELATIVE_PATH} like ? and " +
                "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
        val relativePath = if (downloader == DOWNLOADER_SYSTEM) {
            Environment.DIRECTORY_DOWNLOADS
        } else {
            DUCKSPLASH_RELATIVE_PATH
        }
        val selectionArgs = arrayOf("%${relativePath}%", fileName)
        val uri = if (downloader == DOWNLOADER_SYSTEM) {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        contentResolver.query(uri, projection, selection, selectionArgs, null)?.use {
            return it.count > 0
        } ?: return false
    } else {
        return File(DUCKSPLASH_LEGACY_PATH, fileName).exists()
    }
}

fun showFileExistsDialog(context: Context, action: () -> Unit) {
    MaterialAlertDialogBuilder(context)
        .setTitle(R.string.file_exists_title)
        .setMessage(R.string.file_exists_message)
        .setPositiveButton(R.string.yes) {_, _ -> action.invoke() }
        .setNegativeButton(R.string.no, null)
        .show()
}