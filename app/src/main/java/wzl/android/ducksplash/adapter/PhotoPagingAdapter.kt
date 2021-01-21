package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.databinding.ItemPhotoListBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.util.computerAspectRatio
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail

/**
 *Created on 2021/1/21
 *@author zhilin
 */
class PhotoPagingAdapter(diffCallback: DiffUtil.ItemCallback<PhotoModel>) :
    PagingDataAdapter<PhotoModel, PhotoViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemPhotoListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}

class PhotoViewHolder(private val viewBinding: ItemPhotoListBinding)
    : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: PhotoModel) {
        viewBinding.imageView.apply {
            computerAspectRatio(item.width, item.height)
            val imageUrl = item.urls.raw + "&w=1200&q=80&fm=webp"
            val thumbUrl = item.urls.raw + "&w=200&q=80&fm=webp"
            // 使用 glide 进行图片加载
            loadPhotoUrlWithThumbnail(imageUrl, thumbUrl, item.color,true)
        }
        with(item.user) {
            viewBinding.userName.text = "$firstName ${lastName?:""}"
            viewBinding.userHead.loadCirclePhotoUrl(profileImage.large)
        }
    }

}