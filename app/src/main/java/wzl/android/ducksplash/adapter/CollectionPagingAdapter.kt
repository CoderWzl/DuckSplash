package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.ItemCollectionListBinding
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail

/**
 *Created on 2021/1/22
 *@author zhilin
 */
class CollectionPagingAdapter(
        callback: DiffUtil.ItemCallback<CollectionModel>,
        private val itemClickListener: (collection : CollectionModel) -> Unit
) : PagingDataAdapter<CollectionModel, CollectionPagingViewHolder>(callback) {

    override fun onBindViewHolder(holder: CollectionPagingViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionPagingViewHolder {
        return CollectionPagingViewHolder(
                itemClickListener,
                ItemCollectionListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

}

class CollectionPagingViewHolder(
        private val itemClickListener: (collection : CollectionModel) -> Unit,
        private val viewBinding: ItemCollectionListBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: CollectionModel) {
        val fullName = if (item.coverPhoto.user.lastName == null) {
            item.coverPhoto.user.firstName
        } else {
            item.coverPhoto.user.firstName + " " + item.coverPhoto.user.lastName
        }
        with(viewBinding) {
            description.text = item.title
            photoCount.text = photoCount.context.getString(R.string.photo_number, item.totalPhotos)
            userName.text = fullName
            val imageUrl = item.coverPhoto.urls.raw + "&w=1200&q=80&fm=webp"
            val thumbUrl = item.coverPhoto.urls.raw + "&w=200&q=80&fm=webp"
            collectionCover.aspectRatio = 3 / 4.0
            collectionCover.loadPhotoUrlWithThumbnail(
                    imageUrl,
                    thumbUrl,
                    item.coverPhoto.color
            )
            userHead.loadCirclePhotoUrl(
                    item.coverPhoto.user.profileImage.large
            )
        }
        itemView.setOnClickListener {
            itemClickListener.invoke(item)
        }
    }
}