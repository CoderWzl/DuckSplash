package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.ItemCollectionListBinding
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.model.UserModel
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail
import javax.inject.Inject

/**
 *Created on 2021/1/22
 *@author zhilin
 */
class CollectionPagingAdapter @Inject constructor(
        callback: CollectionDiffCallback
) : PagingDataAdapter<CollectionModel, CollectionPagingViewHolder>(callback) {

    var onCollectionClickListener: ((collection: CollectionModel) -> Unit)? = null
    var onUserClickListener: ((user: UserModel) -> Unit)? = null

    override fun onBindViewHolder(holder: CollectionPagingViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionPagingViewHolder {
        return CollectionPagingViewHolder(
            onCollectionClickListener,
            onUserClickListener,
                ItemCollectionListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

}

class CollectionPagingViewHolder(
        private val itemClickListener: ((collection : CollectionModel) -> Unit)?,
        private val onUserClickListener: ((user : UserModel) -> Unit)?,
        private val viewBinding: ItemCollectionListBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: CollectionModel) {
        val fullName = if (item.coverPhoto?.user?.lastName == null) {
            item.coverPhoto?.user?.firstName
        } else {
            item.coverPhoto?.user?.firstName + " " + item.coverPhoto?.user?.lastName
        }
        with(viewBinding) {
            description.text = item.title
            photoCount.text = photoCount.context.getString(R.string.photo_number, item.totalPhotos)
            userName.text = fullName
            val imageUrl = item.coverPhoto?.urls?.raw + "&w=1200&q=80&fm=webp"
            val thumbUrl = item.coverPhoto?.urls?.raw + "&w=200&q=80&fm=webp"
            collectionCover.aspectRatio = 3 / 4.0
            collectionCover.loadPhotoUrlWithThumbnail(
                    imageUrl,
                    thumbUrl,
                    item.coverPhoto?.color
            )
            userHead.loadCirclePhotoUrl(
                    item.coverPhoto?.user?.profileImage?.large
            )
            val clickListener = View.OnClickListener{
                item.coverPhoto?.user?.let { user ->
                    onUserClickListener?.invoke(user)
                }

            }
            userHead.setOnClickListener(clickListener)
            userName.setOnClickListener(clickListener)
        }
        itemView.setOnClickListener {
            itemClickListener?.invoke(item)
        }
    }
}