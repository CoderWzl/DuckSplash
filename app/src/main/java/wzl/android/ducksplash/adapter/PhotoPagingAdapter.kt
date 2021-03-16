package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.IMAGE_LARGE_SUFFIX
import wzl.android.ducksplash.IMAGE_THUMB_SUFFIX
import wzl.android.ducksplash.databinding.ItemPhotoListBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.model.UserModel
import wzl.android.ducksplash.util.computerAspectRatio
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail
import javax.inject.Inject

/**
 *Created on 2021/1/21
 *@author zhilin
 */
class PhotoPagingAdapter @Inject constructor(
    diffCallback: PhotoDiffCallback
) : PagingDataAdapter<PhotoModel, PhotoViewHolder>(diffCallback) {

    var onUserClickListener: ((use: UserModel?) -> Unit)? = null
    var onPhotoClickListener: ((photo: PhotoModel?) -> Unit)? = null

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
            ),
            onUserClickListener,
            onPhotoClickListener
        )
    }

}

class PhotoViewHolder(
    private val viewBinding: ItemPhotoListBinding,
    private val onUserClickListener: ((user: UserModel?) -> Unit)? = null,
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
        item.user?.apply {
            viewBinding.userName.text = "${firstName?:""} ${lastName?:""}"
            viewBinding.userHead.loadCirclePhotoUrl(profileImage?.large)
            viewBinding.userHead.setOnClickListener {
                onUserClickListener?.invoke(this)
            }
            viewBinding.userName.setOnClickListener {
                onUserClickListener?.invoke(this)
            }
        }
    }
}