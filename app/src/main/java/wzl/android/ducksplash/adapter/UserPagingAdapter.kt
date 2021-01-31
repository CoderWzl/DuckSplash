package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.databinding.ItemUserListBinding
import wzl.android.ducksplash.model.PhotoModel
import wzl.android.ducksplash.model.UserModel
import wzl.android.ducksplash.util.dp2px
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadRoundedPhotoUrl

/**
 *Created on 2021/1/22
 *@author zhilin
 */
class UserPagingAdapter(
    diffCallback: DiffUtil.ItemCallback<UserModel>
) : PagingDataAdapter<UserModel, UserViewHolder>(diffCallback) {

    var onPhotoClickListener: ((photo: PhotoModel) -> Unit)? = null

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            onPhotoClickListener,
            ItemUserListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}

class UserViewHolder(
    private val photoClickListener: ((photo: PhotoModel) -> Unit) ?,
    val viewBinding: ItemUserListBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: UserModel) {
        viewBinding.apply {
            userHead.loadCirclePhotoUrl(item.profileImage.large)
            userName.text = "${item.firstName} ${item.lastName ?: ""}"
            userInsName.text = "@${item.username}"
            image1.aspectRatio = 4 / 3.0
            image2.aspectRatio = 4 / 3.0
            image3.aspectRatio = 4 / 3.0
            if (item.photos == null || item.photos.size < 3) {
                imageContainer.visibility = View.GONE
            } else {
                imageContainer.visibility = View.VISIBLE
                val img1 = item.photos[0].urls.raw + "&w=300&q=80&fm=webp"
                val img2 = item.photos[1].urls.raw + "&w=300&q=80&fm=webp"
                val img3 = item.photos[2].urls.raw + "&w=300&q=80&fm=webp"
                val corner = itemView.context.dp2px(5f)
                image1.loadRoundedPhotoUrl(img1, corner)
                image2.loadRoundedPhotoUrl(img2, corner)
                image3.loadRoundedPhotoUrl(img3, corner)
                val onClickListener = View.OnClickListener{view: View ->
                    val photo: PhotoModel =
                        when (view) {
                            image1 -> item.photos[0]
                            image2 -> item.photos[1]
                            else -> item.photos[2]
                        }
                    photoClickListener?.invoke(photo)
                }
                image1.setOnClickListener(onClickListener)
                image2.setOnClickListener(onClickListener)
                image3.setOnClickListener(onClickListener)
            }
        }
    }

}