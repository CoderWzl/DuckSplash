package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.ItemAddCollectionBinding
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail
import javax.inject.Inject

/**
 *Created on 3/21/21
 *@author zhilin
 * 我的界面添加图集
 */
class AddCollectionPagingAdapter @Inject constructor(
    diffCallback: CollectionDiffCallback
): PagingDataAdapter<CollectionModel, AddCollectionVH>(diffCallback) {

    override fun onBindViewHolder(holder: AddCollectionVH, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bindView(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCollectionVH {
        return AddCollectionVH(
            ItemAddCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}

class AddCollectionVH(
    private val viewBinding: ItemAddCollectionBinding
): RecyclerView.ViewHolder(viewBinding.root) {

    fun bindView(item: CollectionModel) {
        viewBinding.collectionName.text = item.title
        viewBinding.photoCount.text = itemView.context.getString(
            R.string.photo_number,
            item.totalPhotos?:0
        )
        viewBinding.lockSign.isVisible = item.private?: false
        item.coverPhoto?.let { photo ->
            val imageUrl = photo.urls.raw + "&w=800&q=80&fm=webp"
            val thumbUrl = photo.urls.raw + "&w=200&q=80&fm=webp"
            viewBinding.cover.loadPhotoUrlWithThumbnail(
                url = imageUrl,
                color = photo.color,
                thumbnailUrl = thumbUrl
            )
        }
    }
}