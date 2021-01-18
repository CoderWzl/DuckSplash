package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import wzl.android.ducksplash.R
import wzl.android.ducksplash.databinding.ItemCollectionListBinding
import wzl.android.ducksplash.model.CollectionModel
import wzl.android.ducksplash.ui.MainFragmentDirections
import wzl.android.ducksplash.ui.SearchCollectionFragment
import wzl.android.ducksplash.ui.SearchFragmentDirections
import wzl.android.ducksplash.util.loadCirclePhotoUrl
import wzl.android.ducksplash.util.loadPhotoUrlWithThumbnail
import wzl.android.ducksplash.util.navigateSafe

/**
 *Created on 1/17/21
 *@author zhilin
 */
class CollectionListAdapter(val fragment: Fragment): ListAdapter<CollectionModel, CollectionListAdapter.ViewHolder>(DiffCallback()) {


    inner class ViewHolder(private val viewBinding: ItemCollectionListBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

            fun setupView(item: CollectionModel) {
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
                    val action = if (fragment is SearchCollectionFragment) {
                        SearchFragmentDirections.actionSearchFragmentToCollectionDetailFragment(
                            item.id,
                            item.title,
                            item.totalPhotos,
                            item.description,
                            fullName
                        )
                    } else {
                        MainFragmentDirections.actionMainFragmentToCollectionDetailFragment(
                            item.id,
                            item.title,
                            item.totalPhotos,
                            item.description,
                            fullName
                        )
                    }
                    it.findNavController().navigateSafe(action)
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