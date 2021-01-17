package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.databinding.ItemCollectionListBinding
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.ui.MainFragmentDirections
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail

/**
 *Created on 1/17/21
 *@author zhilin
 */
class CollectionListAdapter: ListAdapter<CollectionModel, CollectionListAdapter.ViewHolder>(DiffCallback()) {


    inner class ViewHolder(private val viewBinding: ItemCollectionListBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

            fun setupView(item: CollectionModel) {
                with(viewBinding) {
                    description.text = item.title
                    photoCount.text = "${item.totalPhotos} 张照片"
                    userName.text = "${item.coverPhoto.user.firstName} ${item.coverPhoto.user.lastName?:""}"
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
                    val action = MainFragmentDirections.actionMainFragmentToCollectionDetailFragment(item.id, item.title)
                    it.findNavController().navigate(action)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCollectionListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupView(getItem(position))
    }
}

private class DiffCallback: DiffUtil.ItemCallback<CollectionModel>() {
    override fun areItemsTheSame(oldItem: CollectionModel, newItem: CollectionModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CollectionModel, newItem: CollectionModel): Boolean {
        return oldItem == newItem
    }

}