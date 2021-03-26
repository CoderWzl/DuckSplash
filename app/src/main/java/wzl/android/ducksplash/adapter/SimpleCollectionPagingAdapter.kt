package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.ItemCollectionListBinding
import wzl.android.ducksplash.databinding.ItemSimpleCollectionListBinding
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail
import javax.inject.Inject

/**
 *Created on 2021/1/22
 *@author zhilin
 */
class SimpleCollectionPagingAdapter @Inject constructor(
        callback: CollectionDiffCallback
) : PagingDataAdapter<CollectionModel, SimpleCollectionPagingViewHolder>(callback) {

    var onCollectionClickListener: ((collection: CollectionModel) -> Unit)? = null

    override fun onBindViewHolder(holder: SimpleCollectionPagingViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleCollectionPagingViewHolder {
        return SimpleCollectionPagingViewHolder(
            onCollectionClickListener,
                ItemSimpleCollectionListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

}

class SimpleCollectionPagingViewHolder(
        private val itemClickListener: ((collection : CollectionModel) -> Unit)?,
        private val viewBinding: ItemSimpleCollectionListBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: CollectionModel) {
        with(viewBinding) {
            description.text = item.title
            photoCount.text = photoCount.context.getString(R.string.photo_number, item.totalPhotos)
            var imageUrl = ""
            var thumbUrl = ""
            var color = "#808080"
            item.coverPhoto?.let {
                imageUrl = item.coverPhoto?.urls?.raw + "&w=1200&q=80&fm=webp"
                thumbUrl = item.coverPhoto?.urls?.raw + "&w=200&q=80&fm=webp"
                color = item.coverPhoto?.color?:"#E0E0E0"
            }
            collectionCover.aspectRatio = 3 / 4.0
            collectionCover.loadPhotoUrlWithThumbnail(
                imageUrl,
                thumbUrl,
                color
            )
        }
        itemView.setOnClickListener {
            itemClickListener?.invoke(item)
        }
    }
}