package wzl.android.ducksplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import wzl.android.ducksplash.databinding.ItemCollectionListBinding
import wzl.android.ducksplash.model.CollectionModel

/**
 *Created on 2021/1/29
 *@author zhilin
 */
class PhotoDetailRelatedCollectionsAdapter(
    callback: DiffUtil.ItemCallback<CollectionModel>,
    private val itemClickListener: (collection: CollectionModel) -> Unit
) : ListAdapter<CollectionModel, CollectionPagingViewHolder>(callback) {

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

    override fun onBindViewHolder(holder: CollectionPagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

