package wzl.android.ducksplash.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import wzl.android.ducksplash.GlideApp
import wzl.android.ducksplash.R
import wzl.android.ducksplash.widget.AspectRatioImageView

/**
 *Created on 2021/1/13
 *@author zhilin
 */

const val CROSS_FADE_DURATION = 350

/**
 * 使用 Glide 带有缩略图的图片加载
 */
fun ImageView.loadPhotoUrlWithThumbnail(
        url: String,
        thumbnailUrl: String,
        color: String? = null,
        centerCrop: Boolean = false,
        requestListener: RequestListener<Drawable>? = null
) {
    color?.let {
        background = ColorDrawable(Color.parseColor(it))
    }
    GlideApp.with(context)
            .load(url)
            .thumbnail(
                    if (centerCrop) {
                        GlideApp.with(context).load(thumbnailUrl).centerCrop()
                    } else {
                        GlideApp.with(context).load(thumbnailUrl)
                    }
            )
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .listener(requestListener)
            .into(this)
            .clearOnDetach()
}

/**
 * 加载圆形图片
 */
fun ImageView.loadCirclePhotoUrl(url: String?, @DrawableRes placeholder: Int = R.drawable.drawable_circle_image_placeholder) {
    GlideApp.with(context)
            .load(url)
            .circleCrop()
            .placeholder(placeholder)
            .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
            .into(this)
            .clearOnDetach()
}

/**
 * 通过 width 和 height 计算图片比例
 */
fun AspectRatioImageView.computerAspectRatio(width: Int, height: Int) {
    aspectRatio = if (width != 0) {
        height / (width * 1.0)
    } else {
        9 / 16.0
    }
}