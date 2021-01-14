package wzl.android.ducksplash.util

import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.annotation.GlideType
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions

/**
 *Created on 2021/1/14
 *@author zhilin
 * 通过 GlideExtension 和 GlideOption 可以利用 BaseRequestOptions 自定义Option
 * 操作，方便再 GlideApp 的调用链中使用。
 * 通过 @GlideType 可以生成一个 RequestManager 操作。
 */
class GlideExtension private constructor(){
    // Size of mini thumb in pixels.

    @GlideExtension
    companion object {

        private const val MINI_THUMB_SIZE = 100
        private val DECODE_TYPE_GIF = RequestOptions() // 需要实现一个转换成 GifDrawable 的Options

        @GlideOption
        fun miniThumb(options: BaseRequestOptions<*>): BaseRequestOptions<*> {
            return options.fitCenter().override(MINI_THUMB_SIZE)
        }

        @GlideType(GifDrawable::class)
        fun asTestGif(requestBuilder: RequestBuilder<GifDrawable>): RequestBuilder<GifDrawable> {
            return requestBuilder
                .transition(DrawableTransitionOptions())
                .apply(DECODE_TYPE_GIF)
        }
    }
}