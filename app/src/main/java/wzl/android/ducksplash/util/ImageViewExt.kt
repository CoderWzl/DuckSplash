package wzl.android.ducksplash.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
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

fun ImageView.loadRoundedPhotoUrl(
    url: String,
    cornerRadius: Float = 0f,
    color: String? = null,
    requestListener: RequestListener<Drawable>? = null
) {
    val requestBuilder = GlideApp.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .listener(requestListener)
    val drawable: GradientDrawable = ContextCompat.getDrawable(
        context,
        R.drawable.drawable_rounded_image_placeholder
    ) as GradientDrawable
    color?.let {
        drawable.setColor(Color.parseColor(color))
    }
    if (cornerRadius > 0) {
        drawable.cornerRadius = cornerRadius
        requestBuilder
            .transform(CenterCrop(), RoundedCorners(cornerRadius.toInt()))
            .placeholder(drawable)
            .into(this)
            .clearOnDetach()
    }
    requestBuilder
        .placeholder(drawable)
        .into(this)
        .clearOnDetach()
}

/**
 * 加载圆形图片
 */
fun ImageView.loadCirclePhotoUrl(
    url: String?,
    @DrawableRes placeholder: Int = R.drawable.drawable_circle_image_placeholder
) {
    GlideApp.with(context)
        .load(url)
        .circleCrop()
        .placeholder(placeholder)
        .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
        .into(this)
        .clearOnDetach()
}

fun ImageView.loadBlurredImage(
    url: String?,
    color: String?,
    requestListener: RequestListener<Drawable>? = null
) {
    color?.let { setBackgroundColor(Color.parseColor(it)) }
    GlideApp.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
        .addListener(requestListener)
        .apply(RequestOptions.bitmapTransform(BlurTransformation()))
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