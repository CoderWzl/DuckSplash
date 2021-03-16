package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.IMAGE_LARGE_SUFFIX
import wzl.android.ducksplash.IMAGE_THUMB_SUFFIX
import wzl.android.ducksplash.databinding.ItemSimplePhotoListBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.util.computerAspectRatio
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail
import javax.inject.Inject

/**
 *Created on 2021/1/21
 *@author zhilin
 */
class SimplePhotoPagingAdapter @Inject constructor(
    diffCallback: PhotoDiffCallback
) : PagingDataAdapter<PhotoModel, SimplePhotoViewHolder>(diffCallback) {

    var onPhotoClickListener: ((photo: PhotoModel?) -> Unit)? = null

    override fun onBindViewHolder(holder: SimplePhotoViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimplePhotoViewHolder {
        return SimplePhotoViewHolder(
            ItemSimplePhotoListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onPhotoClickListener
        )
    }

}

class SimplePhotoViewHolder(
    private val viewBinding: ItemSimplePhotoListBinding,
    private val onPhotoClickListener: ((photo: PhotoModel?) -> Unit)? = null,
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: PhotoModel) {
        viewBinding.imageView.apply {
            computerAspectRatio(item.width?:0, item.height?:0)
            val imageUrl = item.urls.raw + IMAGE_LARGE_SUFFIX
            val thumbUrl = item.urls.raw + IMAGE_THUMB_SUFFIX
            // 使用 glide 进行图片加载
            loadPhotoUrlWithThumbnail(imageUrl, thumbUrl, item.color,true)
            setOnClickListener {
                onPhotoClickListener?.invoke(item)
            }
        }
    }
}