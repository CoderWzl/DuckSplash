package wzl.android.ducksplash

import android.app.ActivityManager
import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 *Created on 2021/1/13
 *@author zhilin
 */
@GlideModule
class DuckGlideModel : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val cacheSizeBytes: Long = 1024 * 1024 * 500 // 500M 缓存位置
        // InternalCacheDiskCacheFactory ， ExternalPreferredCacheDiskCacheFactory 适配Android 10+ 不允许使用外部存储的时候使用内部存储
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context,"ImageCache", cacheSizeBytes))
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        if (activityManager?.isLowRamDevice == true) {
            builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
        }
    }
}