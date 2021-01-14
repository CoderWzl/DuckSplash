package wzl.android.ducksplash.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import wzl.android.ducksplash.databinding.ItemPhotoListBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail

/**
 *Created on 2021/1/11
 *@author zhilin
 */

private const val TAG = "PhotoListAdapter"

class PhotoListAdapter :
        ListAdapter<PhotoModel, PhotoListAdapter.ViewHolder>(PhotoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            ItemPhotoListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupView(getItem(position))
    }

    inner class ViewHolder(private val viewBinding: ItemPhotoListBinding) :
            RecyclerView.ViewHolder(viewBinding.root) {

        fun setupView(item: PhotoModel) {
            viewBinding.imageView.apply {
                aspectRatio = if (item.width != 0) {
                    item.height / (item.width * 1.0)
                } else {
                    16 / 9.0
                }
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
}

private class PhotoDiffCallback : DiffUtil.ItemCallback<PhotoModel>() {
    override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem.id == newItem.id
    }
}